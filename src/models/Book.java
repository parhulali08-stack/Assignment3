package models;

public class Book extends BaseEntity {
    private String isbn;
    private Author author;
    private int year;
    
    public Book(int id, String name, String isbn, Author author, int year) {
        super(id, name);
        this.isbn = isbn;
        this.author = author;
        this.year = year;
    }
    
    @Override
    public String getDescription() {
        return getName() + " by " + author.getName() + " (" + year + ")";
    }
    
    @Override
    public boolean isValid() {
        return isbn != null && isbn.length() == 13 && year > 1900;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
}
