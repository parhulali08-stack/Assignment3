package models;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int bookId;
    private String borrowerName;
    private LocalDate loanDate;
    private LocalDate returnDate;

    // Композиция: Loan → Book (требование)
    private Book book;

    public Loan(int id, int bookId, String borrowerName,
                LocalDate loanDate, LocalDate returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.borrowerName = borrowerName;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    // Геттеры/сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) {
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя заемщика не может быть пустым!");
        }
        this.borrowerName = borrowerName;
    }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}