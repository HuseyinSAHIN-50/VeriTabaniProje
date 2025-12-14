-- schema.sql (GÜNCEL: Soft Delete + Favoriler + Arama)

DROP TABLE IF EXISTS Favorites; -- YENİ
DROP TABLE IF EXISTS Penalties;
DROP TABLE IF EXISTS Loans;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Authors;
DROP TABLE IF EXISTS Categories;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Roles;

-- 1. ROLLER
CREATE TABLE Roles (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    role_name NVARCHAR(50) NOT NULL UNIQUE
);

-- 2. KULLANICILAR
CREATE TABLE Users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    role_id INT,
    created_at DATETIME DEFAULT GETDATE(),
    reset_code NVARCHAR(10),
    reset_expiration DATETIME,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

CREATE TABLE Categories (
    category_id INT PRIMARY KEY IDENTITY(1,1),
    category_name NVARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Authors (
    author_id INT PRIMARY KEY IDENTITY(1,1),
    author_name NVARCHAR(150) NOT NULL,
    biography NVARCHAR(MAX)
);

-- 3. KİTAPLAR (Soft Delete için is_deleted eklendi)
CREATE TABLE Books (
    book_id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(200) NOT NULL,
    isbn NVARCHAR(20) UNIQUE,
    publication_year INT,
    stock_quantity INT DEFAULT 0,
    author_id INT,
    category_id INT,
    is_deleted BIT DEFAULT 0, -- 0: Aktif, 1: Silinmiş
    FOREIGN KEY (author_id) REFERENCES Authors(author_id),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);

CREATE TABLE Loans (
    loan_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date DATETIME DEFAULT GETDATE(),
    return_date DATETIME,
    due_date DATETIME NOT NULL,
    is_returned BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

CREATE TABLE Penalties (
    penalty_id INT PRIMARY KEY IDENTITY(1,1),
    loan_id INT UNIQUE NOT NULL,
    penalty_amount DECIMAL(10, 2) NOT NULL,
    payment_status BIT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (loan_id) REFERENCES Loans(loan_id)
);

-- 4. FAVORİLER (YENİ TABLO)
CREATE TABLE Favorites (
    fav_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

INSERT INTO Roles (role_name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');