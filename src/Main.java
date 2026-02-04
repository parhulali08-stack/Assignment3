import models.*;
import services.*;
import repositories.*;
import controller.LibraryController;
import utils.ReflectionUtils;
import utils.SortingUtils;
import interfaces.Validatable;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Library Management System SOLID Version ===\n");
            
            CrudRepository<Author> authorRepo = new AuthorRepository();
            CrudRepository<Book> bookRepo = new BookRepository();
            CrudRepository<Loan> loanRepo = new LoanRepository();
            
            AuthorService authorService = new AuthorService(authorRepo);
            BookService bookService = new BookService(bookRepo, authorRepo);
            LoanService loanService = new LoanService(loanRepo, bookRepo);
            
            System.out.println("=== DEMONSTRATION OF SOLID PRINCIPLES ===\n");
            
            demonstrateOCPAndLSP(authorService, bookService);
            demonstrateISP();
            demonstrateDIP(authorService, bookService);
            demonstrateGenerics();
            demonstrateLambdas();
            demonstrateReflection();
            
            System.out.println("\n=== STARTING INTERACTIVE CONTROLLER ===\n");
            
            LibraryController controller = new LibraryController(authorService, bookService, loanService);
            controller.start();
            
        } catch (Exception e) {
            System.out.println("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void demonstrateOCPAndLSP(AuthorService authorService, BookService bookService) {
        System.out.println("1. OCP & LSP Demonstration:");
        
        try {
            Author author = authorService.createAuthor("George Orwell", "UK");
            
            Book book = bookService.addBook("1984", "9780451524935", 
                                           author.getId(), 1949);
            
            BaseEntity entity1 = author;
            BaseEntity entity2 = book;
            
            System.out.println("  Using BaseEntity reference for Author: " + entity1.getDescription());
            System.out.println("  Using BaseEntity reference for Book: " + entity2.getDescription());
            System.out.println("  Author isValid: " + entity1.isValid());
            System.out.println("  Book isValid: " + entity2.isValid());
            
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void demonstrateISP() {
        System.out.println("2. ISP Demonstration:");
        
        Book testBook = new Book(999, "Test Book", "1234567890123", 
                                new Author(999, "Test Author", "Testland"), 2023);
        
        Validatable<Book> validator = new BookService(null, null);
        System.out.println("  Validation rules: " + validator.getValidationRules());
        System.out.println("  Static check - isNotNull: " + Validatable.isNotNull(testBook));
        System.out.println();
    }
    
    private static void demonstrateDIP(AuthorService authorService, BookService bookService) {
        System.out.println("3. DIP Demonstration:");
        System.out.println("  AuthorService depends on CrudRepository<Author> interface");
        System.out.println("  BookService depends on CrudRepository<Book> interface");
        System.out.println("  No direct dependency on concrete repository classes");
        System.out.println();
    }
    
    private static void demonstrateGenerics() {
        System.out.println("4. Generics Demonstration:");
        
        AuthorRepository authorRepo = new AuthorRepository();
        System.out.println("  AuthorRepository implements CrudRepository<Author>");
        System.out.println("  BookRepository implements CrudRepository<Book>");
        System.out.println("  LoanRepository implements CrudRepository<Loan>");
        System.out.println("  All use generic type T");
        System.out.println();
    }
    
    private static void demonstrateLambdas() {
        System.out.println("5. Lambdas Demonstration:");
        SortingUtils.demonstrateLambdaSorting();
        System.out.println();
    }
    
    private static void demonstrateReflection() {
        System.out.println("6. Reflection Demonstration:");
        Book sampleBook = new Book(1000, "Sample", "1111111111111", 
                                  new Author(1000, "Sample Author", "Country"), 2000);
        ReflectionUtils.inspectClass(sampleBook);
    }
}
