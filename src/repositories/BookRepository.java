package repositories;

import models.Book;
import models.Author;
import exceptions.DatabaseOperationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BookRepository implements CrudRepository<Book> {
    private Connection connection;
    private AuthorRepository authorRepository;
    
    public BookRepository() {
        this.connection = database.DatabaseConnection.getConnection();
        this.authorRepository = new AuthorRepository();
    }
    
    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (name, isbn, author_id, year) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getIsbn());
            stmt.setInt(3, book.getAuthor().getId());
            stmt.setInt(4, book.getYear());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                book.setId(rs.getInt("id"));
            }
            return book;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save book", e);
        }
    }
    
    @Override
    public Optional<Book> findById(int id) {
        String sql = "SELECT b.*, a.name as author_name, a.country FROM books b " +
                    "JOIN authors a ON b.author_id = a.id WHERE b.id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Author author = new Author(
                    rs.getInt("author_id"),
                    rs.getString("author_name"),
                    rs.getString("country")
                );
                
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("isbn"),
                    author,
                    rs.getInt("year")
                );
                return Optional.of(book);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find book", e);
        }
    }
    
    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, a.name as author_name, a.country FROM books b " +
                    "JOIN authors a ON b.author_id = a.id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Author author = new Author(
                    rs.getInt("author_id"),
                    rs.getString("author_name"),
                    rs.getString("country")
                );
                
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("isbn"),
                    author,
                    rs.getInt("year")
                );
                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch books", e);
        }
    }
    
    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET name = ?, isbn = ?, author_id = ?, year = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getIsbn());
            stmt.setInt(3, book.getAuthor().getId());
            stmt.setInt(4, book.getYear());
            stmt.setInt(5, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update book", e);
        }
    }
    
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete book", e);
        }
    }
    
    @Override
    public List<Book> findByCondition(Predicate<Book> condition) {
        List<Book> allBooks = findAll();
        List<Book> result = new ArrayList<>();
        
        for (Book book : allBooks) {
            if (condition.test(book)) {
                result.add(book);
            }
        }
        return result;
    }
}
