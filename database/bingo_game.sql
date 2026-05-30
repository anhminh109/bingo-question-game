IF DB_ID(N'bingo_game') IS NULL
BEGIN
    CREATE DATABASE bingo_game;
END
GO

USE bingo_game;
GO

IF OBJECT_ID(N'dbo.questions', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.questions (
        id INT IDENTITY(1,1) PRIMARY KEY,
        cell_number INT NOT NULL UNIQUE,
        question_text NVARCHAR(MAX) NULL
    );
END
GO

MERGE dbo.questions AS target
USING (VALUES
    (1, N'Em hãy giới thiệu ngắn gọn về bản thân.'),
    (2, N'Môn học em yêu thích nhất là gì? Vì sao?'),
    (3, N'Kể tên một việc tốt em đã làm trong tuần này.'),
    (4, N'Nếu có một điều ước, em sẽ ước gì?'),
    (5, N'Em thích làm việc nhóm hay làm việc một mình hơn?'),
    (6, N'Kể tên một cuốn sách hoặc bộ phim em yêu thích.'),
    (7, N'Điều gì làm em cảm thấy tự hào?'),
    (8, N'Em thường làm gì khi gặp bài khó?'),
    (9, N'Hãy nói một lời cảm ơn với một bạn trong lớp.'),
    (10, N'Nếu được đi du lịch, em muốn đến đâu?'),
    (11, N'Kể tên một kỹ năng em muốn học.'),
    (12, N'Em thích buổi sáng hay buổi tối hơn?'),
    (13, N'Hãy chia sẻ một kỷ niệm vui ở trường.'),
    (14, N'Em nghĩ thế nào là một người bạn tốt?'),
    (15, N'Món ăn yêu thích của em là gì?'),
    (16, N'Em thường giải trí bằng cách nào?'),
    (17, N'Nếu làm lớp trưởng một ngày, em sẽ làm gì?'),
    (18, N'Hãy nêu một mục tiêu học tập của em.'),
    (19, N'Em thích môn thể thao nào?'),
    (20, N'Kể tên một người truyền cảm hứng cho em.'),
    (21, N'Điều gì khiến em cười nhiều nhất?'),
    (22, N'Em muốn cải thiện thói quen nào?'),
    (23, N'Hãy đặt một câu hỏi cho cả lớp.'),
    (24, N'Một ngày cuối tuần lý tưởng của em là như thế nào?'),
    (25, N'Hãy nói một lời chúc dành cho lớp.')
) AS source (cell_number, question_text)
ON target.cell_number = source.cell_number
WHEN MATCHED THEN
    UPDATE SET question_text = source.question_text
WHEN NOT MATCHED THEN
    INSERT (cell_number, question_text)
    VALUES (source.cell_number, source.question_text);
GO