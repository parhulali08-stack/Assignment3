package repositories;

import models.Loan;
import exceptions.DatabaseOperationException;

import java.sql.*;
import java.util.*;

public class LoanRepository {

    // CREATE
    public int create(Loan loan) throws DatabaseOperationException {
        String sql = "INSERT INTO loans (book_id, borrower_name, loan_date, return_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, loan.getBookId());
            pstmt.setString(2, loan.getBorrowerName());
            pstmt.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            pstmt.setDate(4, loan.getReturnDate() != null ?
                    java.sql.Date.valueOf(loan.getReturnDate()) : null);

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
            throw new DatabaseOperationException("Ошибка при создании записи о выдаче: " + e.getMessage());
        }
    }

    // READ ALL
    public List<Loan> getAll() throws DatabaseOperationException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("borrower_name"),
                        rs.getDate("loan_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                );
                loans.add(loan);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при получении записей о выдаче: " + e.getMessage());
        }
        return loans;
    }

    // READ BY ID
    public Loan getById(int id) throws DatabaseOperationException {
        String sql = "SELECT * FROM loans WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("borrower_name"),
                        rs.getDate("loan_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при поиске записи о выдаче: " + e.getMessage());
        }
    }

    // UPDATE
    public boolean update(int id, Loan loan) throws DatabaseOperationException {
        String sql = "UPDATE loans SET book_id = ?, borrower_name = ?, loan_date = ?, return_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loan.getBookId());
            pstmt.setString(2, loan.getBorrowerName());
            pstmt.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            pstmt.setDate(4, loan.getReturnDate() != null ?
                    java.sql.Date.valueOf(loan.getReturnDate()) : null);
            pstmt.setInt(5, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при обновлении записи о выдаче: " + e.getMessage());
        }
    }

    // DELETE
    public boolean delete(int id) throws DatabaseOperationException {
        String sql = "DELETE FROM loans WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при удалении записи о выдаче: " + e.getMessage());
        }
    }

    // GET LOANS BY BOOK ID
    public List<Loan> getByBookId(int bookId) throws DatabaseOperationException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE book_id = ? ORDER BY loan_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("borrower_name"),
                        rs.getDate("loan_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                );
                loans.add(loan);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при получении выдач книги: " + e.getMessage());
        }
        return loans;
    }

    // GET ACTIVE LOANS (не возвращенные)
    public List<Loan> getActiveLoans() throws DatabaseOperationException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE return_date IS NULL ORDER BY loan_date";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("borrower_name"),
                        rs.getDate("loan_date").toLocalDate(),
                        null
                );
                loans.add(loan);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Ошибка при получении активных выдач: " + e.getMessage());
        }
        return loans;
    }
}