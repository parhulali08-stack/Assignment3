package repositories;

import models.Loan;
import models.Book;
import exceptions.DatabaseOperationException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LoanRepository implements CrudRepository<Loan> {
    private Connection connection;
    private BookRepository bookRepository;
    
    public LoanRepository() {
        this.connection = database.DatabaseConnection.getConnection();
        this.bookRepository = new BookRepository();
    }
    
    @Override
    public Loan save(Loan loan) {
        String sql = "INSERT INTO loans (book_id, borrower_name, loan_date, return_date) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loan.getBook().getId());
            stmt.setString(2, loan.getBorrowerName());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            
            if (loan.getReturnDate() != null) {
                stmt.setDate(4, Date.valueOf(loan.getReturnDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                loan.setId(rs.getInt("id"));
            }
            return loan;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save loan", e);
        }
    }
    
    @Override
    public Optional<Loan> findById(int id) {
        String sql = "SELECT l.*, b.name as book_name FROM loans l " +
                    "JOIN books b ON l.book_id = b.id WHERE l.id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Optional<Book> bookOpt = bookRepository.findById(rs.getInt("book_id"));
                if (bookOpt.isEmpty()) {
                    return Optional.empty();
                }
                
                Loan loan = new Loan(
                    rs.getInt("id"),
                    rs.getString("book_name") + " loan",
                    bookOpt.get(),
                    rs.getString("borrower_name"),
                    rs.getDate("loan_date").toLocalDate()
                );
                
                Date returnDate = rs.getDate("return_date");
                if (returnDate != null) {
                    loan.setReturnDate(returnDate.toLocalDate());
                }
                
                return Optional.of(loan);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find loan", e);
        }
    }
    
    @Override
    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.name as book_name FROM loans l " +
                    "JOIN books b ON l.book_id = b.id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Optional<Book> bookOpt = bookRepository.findById(rs.getInt("book_id"));
                if (bookOpt.isEmpty()) continue;
                
                Loan loan = new Loan(
                    rs.getInt("id"),
                    rs.getString("book_name") + " loan",
                    bookOpt.get(),
                    rs.getString("borrower_name"),
                    rs.getDate("loan_date").toLocalDate()
                );
                
                Date returnDate = rs.getDate("return_date");
                if (returnDate != null) {
                    loan.setReturnDate(returnDate.toLocalDate());
                }
                
                loans.add(loan);
            }
            return loans;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch loans", e);
        }
    }
    
    @Override
    public void update(Loan loan) {
        String sql = "UPDATE loans SET book_id = ?, borrower_name = ?, loan_date = ?, return_date = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loan.getBook().getId());
            stmt.setString(2, loan.getBorrowerName());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            
            if (loan.getReturnDate() != null) {
                stmt.setDate(4, Date.valueOf(loan.getReturnDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setInt(5, loan.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update loan", e);
        }
    }
    
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete loan", e);
        }
    }
    
    @Override
    public List<Loan> findByCondition(Predicate<Loan> condition) {
        List<Loan> allLoans = findAll();
        List<Loan> result = new ArrayList<>();
        
        for (Loan loan : allLoans) {
            if (condition.test(loan)) {
                result.add(loan);
            }
        }
        return result;
    }
}
