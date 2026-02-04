package services;

import models.Book;
import models.Author;
import interfaces.Validatable;
import repositories.CrudRepository;
import exceptions.InvalidInputException;
import exceptions.DuplicateResourceException;
import java.util.List;
import java.util.Comparator;

public class BookService implements Validatable<Book> {
    private final CrudRepository<Book> bookRepository;
    private final CrudRepository<Author> authorRepository;
    
    public BookService(CrudRepository<Book> bookRepository, CrudRepository<Author> authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }
    
    @Override
    public boolean validate(Book book) {
        return book.isValid();
    }
    
    public Book addBook(String name, String isbn, int authorId, int year) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Book name cannot be empty");
        }
        
        if (isbn == null || isbn.length() != 13) {
            throw new InvalidInputException("ISBN must be 13 characters");
        }
        
        var authorOpt = authorRepository.findById(authorId);
        if (authorOpt.isEmpty()) {
            throw new InvalidInputException("Author not found with id: " + authorId);
        }
        
        List<Book> existingBooks = bookRepository.findByCondition(b -> 
            b.getIsbn().equals(isbn));
        
        if (!existingBooks.isEmpty()) {
            throw new DuplicateResourceException("Book with ISBN '" + isbn + "' already exists");
        }
        
        Book book = new Book(0, name, isbn, authorOpt.get(), year);
        
        if (!validate(book)) {
            throw new InvalidInputException("Book validation failed");
        }
        
        return bookRepository.save(book);
    }
    
    public List<Book> getAllBooksSortedByName() {
        List<Book> books = bookRepository.findAll();
        books.sort(Comparator.comparing(Book::getName));
        return books;
    }
    
    public List<Book> getBooksByAuthor(int authorId) {
        return bookRepository.findByCondition(b -> b.getAuthor().getId() == authorId);
    }
    
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByCondition(b -> 
            b.getName().toLowerCase().contains(keyword.toLowerCase()) ||
            b.getAuthor().getName().toLowerCase().contains(keyword.toLowerCase()));
    }
}
