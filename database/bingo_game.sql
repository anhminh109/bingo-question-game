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
        question_text NVARCHAR(MAX) NULL,
        answer NVARCHAR(500) NULL
    );
END
GO

IF COL_LENGTH(N'dbo.questions', N'answer') IS NULL
BEGIN
    ALTER TABLE dbo.questions
    ADD answer NVARCHAR(500) NULL;
END
GO

MERGE dbo.questions AS target
USING (VALUES
    (1, N'Em hãy giới thiệu ngắn gọn về bản thân.', N'Tự giới thiệu rõ ràng, đủ ý.'),
    (2, N'Môn học em yêu thích nhất là gì? Vì sao?', N'Nêu được môn học và lý do phù hợp.'),
    (3, N'Kể tên một việc tốt em đã làm trong tuần này.', N'Nêu được một việc tốt cụ thể.'),
    (4, N'Nếu có một điều ước, em sẽ ước gì?', N'Nêu một điều ước và giải thích ngắn gọn.'),
    (5, N'Em thích làm việc nhóm hay làm việc một mình hơn?', N'Nêu lựa chọn và lý do.'),
    (6, N'Kể tên một cuốn sách hoặc bộ phim em yêu thích.', N'Nêu tên sách hoặc phim yêu thích.'),
    (7, N'Điều gì làm em cảm thấy tự hào?', N'Nêu được một điều khiến bản thân tự hào.'),
    (8, N'Em thường làm gì khi gặp bài khó?', N'Trao đổi với bạn, hỏi thầy cô, hoặc tự tìm tài liệu.'),
    (9, N'Hãy nói một lời cảm ơn với một bạn trong lớp.', N'Nói lời cảm ơn chân thành với một bạn.'),
    (10, N'Nếu được đi du lịch, em muốn đến đâu?', N'Nêu địa điểm muốn đến.'),
    (11, N'Kể tên một kỹ năng em muốn học.', N'Nêu một kỹ năng cụ thể.'),
    (12, N'Em thích buổi sáng hay buổi tối hơn?', N'Nêu lựa chọn và lý do.'),
    (13, N'Hãy chia sẻ một kỷ niệm vui ở trường.', N'Chia sẻ một kỷ niệm cụ thể.'),
    (14, N'Em nghĩ thế nào là một người bạn tốt?', N'Biết lắng nghe, giúp đỡ và tôn trọng bạn bè.'),
    (15, N'Món ăn yêu thích của em là gì?', N'Nêu món ăn yêu thích.'),
    (16, N'Em thường giải trí bằng cách nào?', N'Nêu một hoạt động giải trí lành mạnh.'),
    (17, N'Nếu làm lớp trưởng một ngày, em sẽ làm gì?', N'Nêu một việc có ích cho lớp.'),
    (18, N'Hãy nêu một mục tiêu học tập của em.', N'Nêu mục tiêu học tập cụ thể.'),
    (19, N'Em thích môn thể thao nào?', N'Nêu môn thể thao yêu thích.'),
    (20, N'Kể tên một người truyền cảm hứng cho em.', N'Nêu tên người truyền cảm hứng.'),
    (21, N'Điều gì khiến em cười nhiều nhất?', N'Nêu một tình huống hoặc điều làm em vui.'),
    (22, N'Em muốn cải thiện thói quen nào?', N'Nêu một thói quen muốn cải thiện.'),
    (23, N'Hãy đặt một câu hỏi cho cả lớp.', N'Đặt một câu hỏi rõ ràng cho cả lớp.'),
    (24, N'Một ngày cuối tuần lý tưởng của em là như thế nào?', N'Mô tả ngắn gọn một ngày cuối tuần lý tưởng.'),
    (25, N'Hãy nói một lời chúc dành cho lớp.', N'Nói một lời chúc tích cực dành cho lớp.')
) AS source (cell_number, question_text, answer)
ON target.cell_number = source.cell_number
WHEN MATCHED THEN
    UPDATE SET question_text = source.question_text,
               answer = source.answer
WHEN NOT MATCHED THEN
    INSERT (cell_number, question_text, answer)
    VALUES (source.cell_number, source.question_text, source.answer);
GO
