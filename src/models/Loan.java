package models;

import java.time.LocalDate;

public class Loan extends BaseEntity {
    private Book book;
    private String borrowerName;
    private LocalDate loanDate;
    private LocalDate returnDate;
    
    public Loan(int id, String name, Book book, String borrowerName, LocalDate loanDate) {
        super(id, name);
        this.book = book;
        this.borrowerName = borrowerName;
        this.loanDate = loanDate;
        this.returnDate = null;
    }
    
    @Override
    public String getDescription() {
        return book.getName() + " borrowed by " + borrowerName + " on " + loanDate;
    }
    
    @Override
    public boolean isValid() {
        return book != null && 
               borrowerName != null && 
               !borrowerName.trim().isEmpty() &&
               loanDate != null;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public String getBorrowerName() {
        return borrowerName;
    }
    
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
    
    public LocalDate getLoanDate() {
        return loanDate;
    }
    
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public boolean isReturned() {
        return returnDate != null;
    }
}
