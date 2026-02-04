package controller;

import services.AuthorService;
import services.BookService;
import services.LoanService;
import models.Author;
import models.Book;
import models.Loan;
import java.util.List;
import java.util.Scanner;

public class LibraryController {
    private final AuthorService authorService;
    private final BookService bookService;
    private final LoanService loanService;
    private final Scanner scanner;
    
    public LibraryController(AuthorService authorService, 
                            BookService bookService, 
                            LoanService loanService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.loanService = loanService;
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        boolean running = true;
        
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1 -> addAuthor();
                case 2 -> addBook();
                case 3 -> borrowBook();
                case 4 -> returnBook();
                case 5 -> listAllBooks();
                case 6 -> listActiveLoans();
                case 7 -> searchBooks();
                case 8 -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
        
        scanner.close();
        System.out.println("Library system closed");
    }
    
    private void printMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Add Author");
        System.out.println("2. Add Book");
        System.out.println("3. Borrow Book");
        System.out.println("4. Return Book");
        System.out.println("5. List All Books");
        System.out.println("6. List Active Loans");
        System.out.println("7. Search Books");
        System.out.println("8. Exit");
        System.out.print("Choose option: ");
    }
    
    private void addAuthor() {
        try {
            System.out.print("Author name: ");
            String name = scanner.nextLine();
            System.out.print("Country: ");
            String country = scanner.nextLine();
            
            Author author = authorService.createAuthor(name, country);
            System.out.println("Author added: " + author.getDescription());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void addBook() {
        try {
            System.out.print("Book name: ");
            String name = scanner.nextLine();
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();
            System.out.print("Author ID: ");
            int authorId = scanner.nextInt();
            System.out.print("Year: ");
            int year = scanner.nextInt();
            scanner.nextLine();
            
            Book book = bookService.addBook(name, isbn, authorId, year);
            System.out.println("Book added: " + book.getDescription());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void borrowBook() {
        try {
            System.out.print("Book ID: ");
            int bookId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Borrower name: ");
            String borrower = scanner.nextLine();
            
            Loan loan = loanService.borrowBook(bookId, borrower);
            System.out.println("Book borrowed: " + loan.getDescription());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void listAllBooks() {
        List<Book> books = bookService.getAllBooksSortedByName();
        System.out.println("\n=== All Books ===");
        books.forEach(b -> System.out.println(b.getDescription()));
    }
    
    private void listActiveLoans() {
        List<Loan> loans = loanService.getActiveLoans();
        System.out.println("\n=== Active Loans ===");
        loans.forEach(l -> System.out.println(l.getDescription()));
    }
    
    private void searchBooks() {
        System.out.print("Search keyword: ");
        String keyword = scanner.nextLine();
        
        List<Book> results = bookService.searchBooks(keyword);
        System.out.println("\n=== Search Results ===");
        results.forEach(b -> System.out.println(b.getDescription()));
    }
    
    private void returnBook() {
        try {
            System.out.print("Loan ID: ");
            int loanId = scanner.nextInt();
            scanner.nextLine();
            
            loanService.returnBook(loanId);
            System.out.println("Book returned successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
