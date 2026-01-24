package services;

import models.Loan;
import models.Book;
import repositories.LoanRepository;
import repositories.BookRepository;
import exceptions.*;

import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private LoanRepository loanRepository = new LoanRepository();
    private BookRepository bookRepository = new BookRepository();

    // CREATE выдачи книги
    public void createLoan(Loan loan) throws InvalidInputException,
            ResourceNotFoundException,
            DatabaseOperationException {

        if (loan == null) {
            throw new InvalidInputException("Выдача не может быть null");
        }

        if (loan.getBorrowerName() == null || loan.getBorrowerName().trim().isEmpty()) {
            throw new InvalidInputException("Имя заемщика обязательно");
        }

        // Проверить существование книги
        Book book = bookRepository.getById(loan.getBookId());
        if (book == null) {
            throw new ResourceNotFoundException("Книга с ID=" + loan.getBookId() + " не найдена");
        }

        // Проверить доступность книги
        if (!book.isAvailable()) {
            throw new InvalidInputException("Книга уже выдана");
        }

        // Бизнес-логика: дата выдачи не может быть в будущем
        if (loan.getLoanDate().isAfter(LocalDate.now())) {
            throw new InvalidInputException("Дата выдачи не может быть в будущем");
        }

        // Создать выдачу
        int newId = loanRepository.create(loan);
        if (newId > 0) {
            loan.setId(newId);

            // Обновить статус книги
            book.setAvailable(false);
            bookRepository.update(book.getId(), book);
        }
    }

    // Возврат книги
    public void returnBook(int loanId) throws InvalidInputException,
            ResourceNotFoundException,
            DatabaseOperationException {

        Loan loan = loanRepository.getById(loanId);
        if (loan == null) {
            throw new ResourceNotFoundException("Выдача с ID=" + loanId + " не найдена");
        }

        if (loan.getReturnDate() != null) {
            throw new InvalidInputException("Книга уже возвращена");
        }

        // Установить дату возврата
        loan.setReturnDate(LocalDate.now());
        loanRepository.update(loanId, loan);

        // Обновить статус книги
        Book book = bookRepository.getById(loan.getBookId());
        if (book != null) {
            book.setAvailable(true);
            bookRepository.update(book.getId(), book);
        }
    }

    // READ ALL
    public List<Loan> getAllLoans() throws DatabaseOperationException {
        return loanRepository.getAll();
    }

    // GET ACTIVE LOANS
    public List<Loan> getActiveLoans() throws DatabaseOperationException {
        return loanRepository.getActiveLoans();
    }
}