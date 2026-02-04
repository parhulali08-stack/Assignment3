package models;

import java.util.ArrayList;
import java.util.List;

public class Author extends BaseEntity {
    private String country;
    private List<Book> books;
    
    public Author(int id, String name, String country) {
        super(id, name);
        this.country = country;
        this.books = new ArrayList<>();
    }
    
    @Override
    public String getDescription() {
        return getName() + " from " + country;
    }
    
    @Override
    public boolean isValid() {
        return getName() != null && !getName().trim().isEmpty();
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    public void addBook(Book book) {
        books.add(book);
    }
}
