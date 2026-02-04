package repositories;

import models.Author;
import exceptions.DatabaseOperationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AuthorRepository implements CrudRepository<Author> {
    private Connection connection;
    
    public AuthorRepository() {
        this.connection = database.DatabaseConnection.getConnection();
    }
    
    @Override
    public Author save(Author author) {
        String sql = "INSERT INTO authors (name, country) VALUES (?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author.getName());
            stmt.setString(2, author.getCountry());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                author.setId(rs.getInt("id"));
            }
            return author;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save author", e);
        }
    }
    
    @Override
    public Optional<Author> findById(int id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Author author = new Author(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("country")
                );
                return Optional.of(author);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find author", e);
        }
    }
    
    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Author author = new Author(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("country")
                );
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch authors", e);
        }
    }
    
    @Override
    public void update(Author author) {
        String sql = "UPDATE authors SET name = ?, country = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author.getName());
            stmt.setString(2, author.getCountry());
            stmt.setInt(3, author.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update author", e);
        }
    }
    
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete author", e);
        }
    }
    
    @Override
    public List<Author> findByCondition(Predicate<Author> condition) {
        List<Author> allAuthors = findAll();
        List<Author> result = new ArrayList<>();
        
        for (Author author : allAuthors) {
            if (condition.test(author)) {
                result.add(author);
            }
        }
        return result;
    }
}
