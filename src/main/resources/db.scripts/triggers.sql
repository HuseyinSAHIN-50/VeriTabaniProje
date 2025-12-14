-- triggers.sql



-- Eğer trigger daha önce varsa güncellemek için ALTER, yoksa CREATE yapar

CREATE OR ALTER TRIGGER trg_CalculatePenalty

ON Loans

AFTER UPDATE

AS

BEGIN

    -- Değişken tanımları

    DECLARE @DailyPenalty DECIMAL(10, 2) = 50.00; -- Günlük ceza miktarı (Örn: 5 TL)



    -- Sadece 'is_returned' alanı 0'dan 1'e döndüyse (Kitap iade edildiyse) çalışır

    IF UPDATE(is_returned)

    BEGIN

        -- Penalties tablosuna otomatik kayıt ekle

        INSERT INTO Penalties (loan_id, penalty_amount, payment_status, created_at)

        SELECT

            i.loan_id,

            -- Ceza Hesaplama: (İade Tarihi - Son Teslim Tarihi) * Günlük Ceza

            DATEDIFF(day, i.due_date, i.return_date) * @DailyPenalty,

            0, -- Ödenmedi olarak işaretle

            GETDATE()

        FROM

            inserted i

            INNER JOIN deleted d ON i.loan_id = d.loan_id

        WHERE

            i.is_returned = 1       -- Şu an iade edildi

            AND d.is_returned = 0   -- Az önce iade edilmemişti (Tekrarı önlemek için)

            AND i.return_date > i.due_date; -- Ve geç getirilmiş

    END

END;

