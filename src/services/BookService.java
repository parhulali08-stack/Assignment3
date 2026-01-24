package services;

import models.Book;
import repositories.BookRepository;
import exceptions.*;

import java.util.List;

public class BookService {
    private BookRepository bookRepository = new BookRepository();

    // CREATE с валидацией
    public void createBook(Book book) throws InvalidInputException,
            DuplicateResourceException,
            DatabaseOperationException {

        // 1. Валидация (требование)
        if (book == null) {
            throw new InvalidInputException("Книга не может быть null");
        }

        if (!book.isValid()) {
            throw new InvalidInputException("Данные книги невалидны");
        }

        // 2. Бизнес-логика: ISBN должен быть уникальным
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateResourceException("ISBN " + book.getIsbn() + " уже существует");
        }

        // 3. Бизнес-логика: год издания не может быть в будущем
        int currentYear = java.time.Year.now().getValue();
        if (book.getYear() > currentYear) {
            throw new InvalidInputException("Год издания не может быть больше " + currentYear);
        }

        // 4. Сохранить в БД
        int newId = bookRepository.create(book);
        if (newId > 0) {
            book.setId(newId);
        }
    }

    // READ ALL
    public List<Book> getAllBooks() throws DatabaseOperationException {
        return bookRepository.getAll();
    }

    // READ BY ID
    public Book getBookById(int id) throws ResourceNotFoundException,
            DatabaseOperationException {
        Book book = bookRepository.getById(id);
        if (book == null) {
            throw new ResourceNotFoundException("Книга с ID=" + id + " не найдена");
        }
        return book;
    }

    // UPDATE
    public void updateBook(int id, Book book) throws InvalidInputException,
            ResourceNotFoundException,
            DatabaseOperationException {
        // 1. Проверить существование
        if (bookRepository.getById(id) == null) {
            throw new ResourceNotFoundException("Книга с ID=" + id + " не найдена");
        }

        // 2. Валидация
        if (!book.isValid()) {
            throw new InvalidInputException("Данные книги невалидны");
        }

        // 3. Обновить
        boolean updated = bookRepository.update(id, book);
        if (!updated) {
            throw new DatabaseOperationException("Не удалось обновить книгу");
        }
    }

    // DELETE
    public void deleteBook(int id) throws ResourceNotFoundException,
            DatabaseOperationException {
        // 1. Проверить существование
        if (bookRepository.getById(id) == null) {
            throw new ResourceNotFoundException("Книга с ID=" + id + " не найдена");
        }

        // 2. Удалить
        boolean deleted = bookRepository.delete(id);
        if (!deleted) {
            throw new DatabaseOperationException("Не удалось удалить книгу");
        }
    }

    // ПОЛИМОРФИЗМ пример: метод принимает BaseEntity
    public void displayEntityInfo(Book book) {
        if (book != null) {
            System.out.println(book.getFullInfo());  // метод из BaseEntity
            book.display();  // метод из интерфейса Displayable
        }
    }
}