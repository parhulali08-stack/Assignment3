package services;

import models.Loan;
import models.Book;
import repositories.CrudRepository;
import exceptions.InvalidInputException;
import exceptions.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final CrudRepository<Loan> loanRepository;
    private final CrudRepository<Book> bookRepository;
    
    public LoanService(CrudRepository<Loan> loanRepository, CrudRepository<Book> bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }
    
    public Loan borrowBook(int bookId, String borrowerName) {
        var bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new ResourceNotFoundException("Book not found with id: " + bookId);
        }
        
        List<Loan> activeLoans = loanRepository.findByCondition(l -> 
            l.getBook().getId() == bookId && l.getReturnDate() == null);
        
        if (!activeLoans.isEmpty()) {
            throw new InvalidInputException("Book is already borrowed");
        }
        
        Loan loan = new Loan(0, "Loan for " + bookOpt.get().getName(), 
                            bookOpt.get(), borrowerName, LocalDate.now());
        
        if (!loan.isValid()) {
            throw new InvalidInputException("Invalid loan data");
        }
        
        return loanRepository.save(loan);
    }
    
    public void returnBook(int loanId) {
        var loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new ResourceNotFoundException("Loan not found with id: " + loanId);
        }
        
        Loan loan = loanOpt.get();
        if (loan.isReturned()) {
            throw new InvalidInputException("Book already returned");
        }
        
        loan.setReturnDate(LocalDate.now());
        loanRepository.update(loan);
    }
    
    public List<Loan> getActiveLoans() {
        return loanRepository.findByCondition(l -> l.getReturnDate() == null);
    }
    
    public List<Loan> getLoansByBorrower(String borrowerName) {
        return loanRepository.findByCondition(l -> 
            l.getBorrowerName().equalsIgnoreCase(borrowerName));
    }
}
