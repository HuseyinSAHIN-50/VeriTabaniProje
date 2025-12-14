-- Kütüphane İstatistiklerini Getiren Prosedür
CREATE OR ALTER PROCEDURE sp_GetLibraryStats
AS
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM Books) AS TotalBooks,
        (SELECT COUNT(*) FROM Users) AS TotalUsers,
        (SELECT COUNT(*) FROM Loans WHERE is_returned = 0) AS ActiveLoans;
END;