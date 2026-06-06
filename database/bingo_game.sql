CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (username, password)
VALUES ('admin', 'admin123')
ON CONFLICT (username) DO NOTHING;

CREATE TABLE IF NOT EXISTS questions (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    cell_number INT NOT NULL,
    question_text TEXT,
    answer VARCHAR(500)
);

ALTER TABLE questions
    ADD COLUMN IF NOT EXISTS user_id INT;

ALTER TABLE questions
    ADD COLUMN IF NOT EXISTS answer VARCHAR(500);

UPDATE questions
SET user_id = users.id
FROM users
WHERE questions.user_id IS NULL
  AND users.username = 'admin';

DO $$
DECLARE
    constraint_record record;
BEGIN
    FOR constraint_record IN
        SELECT con.conname
        FROM pg_constraint con
        JOIN pg_class rel ON rel.oid = con.conrelid
        JOIN pg_namespace nsp ON nsp.oid = rel.relnamespace
        WHERE rel.relname = 'questions'
          AND nsp.nspname = current_schema()
          AND con.contype = 'u'
          AND array_length(con.conkey, 1) = 1
          AND (
              SELECT att.attname
              FROM pg_attribute att
              WHERE att.attrelid = rel.oid
                AND att.attnum = con.conkey[1]
          ) = 'cell_number'
    LOOP
        EXECUTE format('ALTER TABLE questions DROP CONSTRAINT %I', constraint_record.conname);
    END LOOP;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'questions_user_id_fkey'
          AND conrelid = 'questions'::regclass
    ) THEN
        ALTER TABLE questions
            ADD CONSTRAINT questions_user_id_fkey
            FOREIGN KEY (user_id) REFERENCES users(id);
    END IF;
END $$;

ALTER TABLE questions
    ALTER COLUMN user_id SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_questions_user_cell
    ON questions (user_id, cell_number);

INSERT INTO questions (user_id, cell_number, question_text, answer)
SELECT users.id, cells.cell_number, '', ''
FROM users
CROSS JOIN generate_series(1, 25) AS cells(cell_number)
WHERE users.username = 'admin'
ON CONFLICT (user_id, cell_number) DO NOTHING;
