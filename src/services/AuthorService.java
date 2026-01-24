package services;

import models.Author;
import repositories.AuthorRepository;
import exceptions.*;

import java.util.List;

public class AuthorService {
    private AuthorRepository authorRepository = new AuthorRepository();

    // CREATE
    public void createAuthor(Author author) throws InvalidInputException,
            DatabaseOperationException {

        if (author == null) {
            throw new InvalidInputException("Автор не может быть null");
        }

        if (!author.isValid()) {
            throw new InvalidInputException("Данные автора невалидны");
        }

        int newId = authorRepository.create(author);
        if (newId > 0) {
            author.setId(newId);
        }
    }

    // READ ALL
    public List<Author> getAllAuthors() throws DatabaseOperationException {
        return authorRepository.getAll();
    }

    // READ BY ID
    public Author getAuthorById(int id) throws ResourceNotFoundException,
            DatabaseOperationException {
        Author author = authorRepository.getById(id);
        if (author == null) {
            throw new ResourceNotFoundException("Автор с ID=" + id + " не найден");
        }
        return author;
    }

    // ПОЛИМОРФИЗМ: метод для BaseEntity
    public void printEntityInfo(Author author) {
        if (author != null) {
            System.out.println("📝 " + author.getFullInfo());
            System.out.println("   " + author.getDescription());
        }
    }
}