CREATE TABLE IF NOT EXISTS questions (
    id SERIAL PRIMARY KEY,
    cell_number INT NOT NULL UNIQUE,
    question_text TEXT,
    answer VARCHAR(500)
);

ALTER TABLE questions
    ADD COLUMN IF NOT EXISTS answer VARCHAR(500);

INSERT INTO questions (cell_number, question_text, answer)
VALUES
    (1, 'Em hãy giới thiệu ngắn gọn về bản thân.', 'Tự giới thiệu rõ ràng, đủ ý.'),
    (2, 'Môn học em yêu thích nhất là gì? Vì sao?', 'Nêu được môn học và lý do phù hợp.'),
    (3, 'Kể tên một việc tốt em đã làm trong tuần này.', 'Nêu được một việc tốt cụ thể.'),
    (4, 'Nếu có một điều ước, em sẽ ước gì?', 'Nêu một điều ước và giải thích ngắn gọn.'),
    (5, 'Em thích làm việc nhóm hay làm việc một mình hơn?', 'Nêu lựa chọn và lý do.'),
    (6, 'Kể tên một cuốn sách hoặc bộ phim em yêu thích.', 'Nêu tên sách hoặc phim yêu thích.'),
    (7, 'Điều gì làm em cảm thấy tự hào?', 'Nêu được một điều khiến bản thân tự hào.'),
    (8, 'Em thường làm gì khi gặp bài khó?', 'Trao đổi với bạn, hỏi thầy cô, hoặc tự tìm tài liệu.'),
    (9, 'Hãy nói một lời cảm ơn với một bạn trong lớp.', 'Nói lời cảm ơn chân thành với một bạn.'),
    (10, 'Nếu được đi du lịch, em muốn đến đâu?', 'Nêu địa điểm muốn đến.'),
    (11, 'Kể tên một kỹ năng em muốn học.', 'Nêu một kỹ năng cụ thể.'),
    (12, 'Em thích buổi sáng hay buổi tối hơn?', 'Nêu lựa chọn và lý do.'),
    (13, 'Hãy chia sẻ một kỷ niệm vui ở trường.', 'Chia sẻ một kỷ niệm cụ thể.'),
    (14, 'Em nghĩ thế nào là một người bạn tốt?', 'Biết lắng nghe, giúp đỡ và tôn trọng bạn bè.'),
    (15, 'Món ăn yêu thích của em là gì?', 'Nêu món ăn yêu thích.'),
    (16, 'Em thường giải trí bằng cách nào?', 'Nêu một hoạt động giải trí lành mạnh.'),
    (17, 'Nếu làm lớp trưởng một ngày, em sẽ làm gì?', 'Nêu một việc có ích cho lớp.'),
    (18, 'Hãy nêu một mục tiêu học tập của em.', 'Nêu mục tiêu học tập cụ thể.'),
    (19, 'Em thích môn thể thao nào?', 'Nêu môn thể thao yêu thích.'),
    (20, 'Kể tên một người truyền cảm hứng cho em.', 'Nêu tên người truyền cảm hứng.'),
    (21, 'Điều gì khiến em cười nhiều nhất?', 'Nêu một tình huống hoặc điều làm em vui.'),
    (22, 'Em muốn cải thiện thói quen nào?', 'Nêu một thói quen muốn cải thiện.'),
    (23, 'Hãy đặt một câu hỏi cho cả lớp.', 'Đặt một câu hỏi rõ ràng cho cả lớp.'),
    (24, 'Một ngày cuối tuần lý tưởng của em là như thế nào?', 'Mô tả ngắn gọn một ngày cuối tuần lý tưởng.'),
    (25, 'Hãy nói một lời chúc dành cho lớp.', 'Nói một lời chúc tích cực dành cho lớp.')
ON CONFLICT (cell_number) DO UPDATE SET
    question_text = EXCLUDED.question_text,
    answer = EXCLUDED.answer;
