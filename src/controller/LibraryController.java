package controller;

import models.*;
import services.*;
import exceptions.*;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class LibraryController {
    private BookService bookService = new BookService();
    private AuthorService authorService = new AuthorService();
    private LoanService loanService = new LoanService();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("📚 БИБЛИОТЕЧНАЯ СИСТЕМА");
        System.out.println("=======================\n");


        demonstrateOOP();


        showMainMenu();
    }

    private void demonstrateOOP() {
        System.out.println("🎓 ДЕМОНСТРАЦИЯ OOP ПРИНЦИПОВ:");
        System.out.println("============================\n");


        Author author = new Author(1, "Абай Құнанбаев", "Қазақстан");
        Book book = new Book(1, "Қара сөздер", 1, 1890, "ISBN-001", true);

        System.out.println("1. ПОЛИМОРФИЗМ:");
        displayEntityInfo(author);
        displayEntityInfo(book);


        System.out.println("\n2. ИНТЕРФЕЙСЫ:");
        book.display();
        System.out.println("Валидация: " + book.validate());


        System.out.println("\n3. ИНКАПСУЛЯЦИЯ:");
        System.out.println("Название: " + book.getName());
        book.setName("Қара сөздер (новое издание)");
        System.out.println("Новое название: " + book.getName());


        System.out.println("\n4. КОМПОЗИЦИЯ:");
        book.setAuthor(author);
        System.out.println("Автор книги: " + (book.getAuthor() != null ? book.getAuthor().getName() : "не указан"));

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    private void displayEntityInfo(BaseEntity entity) {
        System.out.println("   " + entity.getFullInfo());
        System.out.println("   " + entity.getDescription());
    }

    private void showMainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("📋 ГЛАВНОЕ МЕНЮ:");
            System.out.println("1. Управление книгами");
            System.out.println("2. Управление авторами");
            System.out.println("3. Управление выдачами");
            System.out.println("4. Демонстрация OOP");
            System.out.println("5. Выход");

            System.out.print("\nВыберите (1-5): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showBookMenu();
                case "2" -> showAuthorMenu();
                case "3" -> showLoanMenu();
                case "4" -> demonstrateOOP();
                case "5" -> {
                    System.out.println("\n👋 До свидания!");
                    running = false;
                }
                default -> System.out.println("❌ Неверный выбор!");
            }
        }

        scanner.close();
    }

    private void showBookMenu() {
        System.out.println("\n📖 УПРАВЛЕНИЕ КНИГАМИ:");
        System.out.println("1. Добавить книгу");
        System.out.println("2. Показать все книги");
        System.out.println("3. Найти книгу по ID");
        System.out.println("4. Назад");

        System.out.print("\nВыберите: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> addBook();
            case "2" -> showAllBooks();
            case "3" -> findBookById();
        }
    }

    private void addBook() {
        try {
            System.out.print("Название: ");
            String title = scanner.nextLine();

            System.out.print("ID автора: ");
            int authorId = Integer.parseInt(scanner.nextLine());

            System.out.print("Год издания: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();

            Book book = new Book(0, title, authorId, year, isbn, true);
            bookService.createBook(book);

            System.out.println("✅ Книга добавлена! ID: " + book.getId());

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void showAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();

            if (books.isEmpty()) {
                System.out.println("📭 Список книг пуст");
                return;
            }

            System.out.println("\n📚 ВСЕ КНИГИ:");
            for (Book book : books) {
                System.out.println(book.getFullInfo());
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void findBookById() {
        try {
            System.out.print("Введите ID книги: ");
            int id = Integer.parseInt(scanner.nextLine());

            Book book = bookService.getBookById(id);
            System.out.println("\n✅ НАЙДЕНО:");
            System.out.println("Название: " + book.getName());
            System.out.println("Год: " + book.getYear());
            System.out.println("ISBN: " + book.getIsbn());

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void showAuthorMenu() {
        System.out.println("\n👤 УПРАВЛЕНИЕ АВТОРАМИ:");
        System.out.println("1. Добавить автора");
        System.out.println("2. Показать всех авторов");
        System.out.println("3. Назад");

        System.out.print("\nВыберите: ");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            addAuthor();
        } else if ("2".equals(choice)) {
            showAllAuthors();
        }
    }

    private void addAuthor() {
        try {
            System.out.print("Имя автора: ");
            String name = scanner.nextLine();

            System.out.print("Страна: ");
            String country = scanner.nextLine();

            Author author = new Author(0, name, country);
            authorService.createAuthor(author);

            System.out.println("✅ Автор добавлен! ID: " + author.getId());

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void showAllAuthors() {
        try {
            List<Author> authors = authorService.getAllAuthors();

            if (authors.isEmpty()) {
                System.out.println("📭 Список авторов пуст");
                return;
            }

            System.out.println("\n👤 ВСЕ АВТОРЫ:");
            for (Author author : authors) {
                System.out.println(author.getFullInfo());
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void showLoanMenu() {
        System.out.println("\n📝 УПРАВЛЕНИЕ ВЫДАЧАМИ:");
        System.out.println("1. Выдать книгу");
        System.out.println("2. Вернуть книгу");
        System.out.println("3. Активные выдачи");
        System.out.println("4. Назад");

        System.out.print("\nВыберите: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> createLoan();
            case "2" -> returnLoan();
            case "3" -> showActiveLoans();
        }
    }

    private void createLoan() {
        try {
            System.out.print("ID книги: ");
            int bookId = Integer.parseInt(scanner.nextLine());

            System.out.print("Имя заемщика: ");
            String borrower = scanner.nextLine();

            Loan loan = new Loan(0, bookId, borrower, LocalDate.now(), null);
            loanService.createLoan(loan);

            System.out.println("✅ Книга выдана! ID выдачи: " + loan.getId());

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void returnLoan() {
        try {
            System.out.print("ID выдачи: ");
            int loanId = Integer.parseInt(scanner.nextLine());

            loanService.returnBook(loanId);
            System.out.println("✅ Книга возвращена!");

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private void showActiveLoans() {
        try {
            List<Loan> loans = loanService.getActiveLoans();

            if (loans.isEmpty()) {
                System.out.println("✅ Нет активных выдач");
                return;
            }

            System.out.println("\n📋 АКТИВНЫЕ ВЫДАЧИ:");
            for (Loan loan : loans) {
                System.out.println("ID: " + loan.getId() +
                        ", Книга ID: " + loan.getBookId() +
                        ", Заемщик: " + loan.getBorrowerName());
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }
}