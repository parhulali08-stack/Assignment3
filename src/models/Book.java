package models;

import interfaces.*;

public class Book extends BaseEntity implements Validatable, Displayable {
    private int authorId;
    private int year;
    private String isbn;
    private boolean available;

   
    private Author author;

    public Book(int id, String title, int authorId, int year, String isbn, boolean available) {
        super(id, title);
        this.authorId = authorId;
        this.year = year;
        this.isbn = isbn;
        this.available = available;
    }

    @Override
    public String getDescription() {
        return "Книга: " + getName() + " (" + year + ")";
    }

    @Override
    public boolean isValid() {
        return getName() != null && !getName().trim().isEmpty()
                && year > 0;
    }

    
    @Override
    public boolean validate() { return isValid(); }

    
    @Override
    public void display() {
        System.out.println("📖 " + getDescription());
    }

    
    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
}
