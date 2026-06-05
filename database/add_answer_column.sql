ALTER TABLE questions
    ADD COLUMN IF NOT EXISTS answer VARCHAR(500);

UPDATE questions
SET answer = CASE cell_number
    WHEN 1 THEN 'Tự giới thiệu rõ ràng, đủ ý.'
    WHEN 2 THEN 'Nêu được môn học và lý do phù hợp.'
    WHEN 3 THEN 'Nêu được một việc tốt cụ thể.'
    WHEN 4 THEN 'Nêu một điều ước và giải thích ngắn gọn.'
    WHEN 5 THEN 'Nêu lựa chọn và lý do.'
    WHEN 6 THEN 'Nêu tên sách hoặc phim yêu thích.'
    WHEN 7 THEN 'Nêu được một điều khiến bản thân tự hào.'
    WHEN 8 THEN 'Trao đổi với bạn, hỏi thầy cô, hoặc tự tìm tài liệu.'
    WHEN 9 THEN 'Nói lời cảm ơn chân thành với một bạn.'
    WHEN 10 THEN 'Nêu địa điểm muốn đến.'
    WHEN 11 THEN 'Nêu một kỹ năng cụ thể.'
    WHEN 12 THEN 'Nêu lựa chọn và lý do.'
    WHEN 13 THEN 'Chia sẻ một kỷ niệm cụ thể.'
    WHEN 14 THEN 'Biết lắng nghe, giúp đỡ và tôn trọng bạn bè.'
    WHEN 15 THEN 'Nêu món ăn yêu thích.'
    WHEN 16 THEN 'Nêu một hoạt động giải trí lành mạnh.'
    WHEN 17 THEN 'Nêu một việc có ích cho lớp.'
    WHEN 18 THEN 'Nêu mục tiêu học tập cụ thể.'
    WHEN 19 THEN 'Nêu môn thể thao yêu thích.'
    WHEN 20 THEN 'Nêu tên người truyền cảm hứng.'
    WHEN 21 THEN 'Nêu một tình huống hoặc điều làm em vui.'
    WHEN 22 THEN 'Nêu một thói quen muốn cải thiện.'
    WHEN 23 THEN 'Đặt một câu hỏi rõ ràng cho cả lớp.'
    WHEN 24 THEN 'Mô tả ngắn gọn một ngày cuối tuần lý tưởng.'
    WHEN 25 THEN 'Nói một lời chúc tích cực dành cho lớp.'
    ELSE answer
END
WHERE cell_number BETWEEN 1 AND 25;
