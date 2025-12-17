-- triggers.sql (DAKİKALIK CEZA TEST VERSİYONU)

DROP TRIGGER IF EXISTS trg_CalculatePenalty;
GO

CREATE TRIGGER trg_CalculatePenalty
ON Loans
AFTER UPDATE
AS
BEGIN
    -- Dakika başı ceza miktarı (Örn: Dakikası 10 TL olsun ki belli olsun)
    DECLARE @DakikaBasiCeza DECIMAL(10, 2) = 10.00; 

    DECLARE @LoanID INT;
    DECLARE @DueDate DATETIME;
    DECLARE @ReturnDate DATETIME;
    DECLARE @IsReturned BIT;

    SELECT 
        @LoanID = i.loan_id, 
        @DueDate = i.due_date, 
        @ReturnDate = i.return_date,
        @IsReturned = i.is_returned
    FROM inserted i;

    -- Eğer iade edildiyse VE teslim tarihi geçtiyse
    IF (@IsReturned = 1) AND (@ReturnDate > @DueDate)
    BEGIN
        -- DİKKAT: Burada 'DAY' yerine 'MINUTE' kullanıyoruz
        DECLARE @GecikenDakika INT = DATEDIFF(MINUTE, @DueDate, @ReturnDate);
        
        -- Cezayı hesapla (Dakika * Miktar)
        DECLARE @ToplamCeza DECIMAL(10, 2) = @GecikenDakika * @DakikaBasiCeza;

        INSERT INTO Penalties (loan_id, penalty_amount, payment_status, created_at)
        VALUES (@LoanID, @ToplamCeza, 0, GETDATE());
    END
END;