package repositories;

import models.Author;
import exceptions.DatabaseOperationException;

import java.sql.*;
import java.util.*;

public class AuthorRepository {

    public int create(Author author) throws DatabaseOperationException {
        String sql = "INSERT INTO authors (name, country) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getCountry());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return -1;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при добавлении автора: " + e.getMessage());
        }
    }

    public List<Author> getAll() throws DatabaseOperationException {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Author author = new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country")
                );
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при получении авторов: " + e.getMessage());
        }
        return authors;
    }

    public Author getById(int id) throws DatabaseOperationException {
        String sql = "SELECT * FROM authors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("country")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при поиске автора: " + e.getMessage());
        }
    }
}