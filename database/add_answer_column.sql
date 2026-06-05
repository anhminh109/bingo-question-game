USE bingo_game;
GO

IF COL_LENGTH(N'dbo.questions', N'answer') IS NULL
BEGIN
    ALTER TABLE dbo.questions
    ADD answer NVARCHAR(500) NULL;
END
GO

UPDATE dbo.questions
SET answer = CASE cell_number
    WHEN 1 THEN N'Tự giới thiệu rõ ràng, đủ ý.'
    WHEN 2 THEN N'Nêu được môn học và lý do phù hợp.'
    WHEN 3 THEN N'Nêu được một việc tốt cụ thể.'
    WHEN 4 THEN N'Nêu một điều ước và giải thích ngắn gọn.'
    WHEN 5 THEN N'Nêu lựa chọn và lý do.'
    WHEN 6 THEN N'Nêu tên sách hoặc phim yêu thích.'
    WHEN 7 THEN N'Nêu được một điều khiến bản thân tự hào.'
    WHEN 8 THEN N'Trao đổi với bạn, hỏi thầy cô, hoặc tự tìm tài liệu.'
    WHEN 9 THEN N'Nói lời cảm ơn chân thành với một bạn.'
    WHEN 10 THEN N'Nêu địa điểm muốn đến.'
    WHEN 11 THEN N'Nêu một kỹ năng cụ thể.'
    WHEN 12 THEN N'Nêu lựa chọn và lý do.'
    WHEN 13 THEN N'Chia sẻ một kỷ niệm cụ thể.'
    WHEN 14 THEN N'Biết lắng nghe, giúp đỡ và tôn trọng bạn bè.' 
    WHEN 15 THEN N'Nêu món ăn yêu thích.'
    WHEN 16 THEN N'Nêu một hoạt động giải trí lành mạnh.'
    WHEN 17 THEN N'Nêu một việc có ích cho lớp.'
    WHEN 18 THEN N'Nêu mục tiêu học tập cụ thể.'
    WHEN 19 THEN N'Nêu môn thể thao yêu thích.'
    WHEN 20 THEN N'Nêu tên người truyền cảm hứng.'
    WHEN 21 THEN N'Nêu một tình huống hoặc điều làm em vui.'
    WHEN 22 THEN N'Nêu một thói quen muốn cải thiện.'
    WHEN 23 THEN N'Đặt một câu hỏi rõ ràng cho cả lớp.'
    WHEN 24 THEN N'Mô tả ngắn gọn một ngày cuối tuần lý tưởng.'
    WHEN 25 THEN N'Nói một lời chúc tích cực dành cho lớp.'
    ELSE answer
END
WHERE cell_number BETWEEN 1 AND 25;
GO
