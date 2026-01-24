package repositories;

import models.Book;
import models.Author;
import exceptions.DatabaseOperationException;

import java.util.*;

public class BookRepository {
    private static List<Book> books = new ArrayList<>();
    private static List<Author> authors = new ArrayList<>();
    private static int nextBookId = 3;
    private static int nextAuthorId = 3;

    static {
        
        authors.add(new Author(1, "Абай Құнанбаев", "Қазақстан"));
        authors.add(new Author(2, "Мұхтар Әуезов", "Қазақстан"));

        Book book1 = new Book(1, "Қара сөздер", 1, 1890, "ISBN-001", true);
        Book book2 = new Book(2, "Абай жолы", 2, 1945, "ISBN-002", true);

        book1.setAuthor(authors.get(0));
        book2.setAuthor(authors.get(1));

        books.add(book1);
        books.add(book2);
    }

   
    public int create(Book book) {
        book.setId(nextBookId++);
        books.add(book);
        System.out.println("✅ Книга создана (в памяти)");
        return book.getId();
    }

    public List<Book> getAll() {
        return new ArrayList<>(books);
    }

    public Book getById(int id) {
        return books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

   
}
