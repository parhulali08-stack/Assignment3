package services;

import models.Author;
import repositories.CrudRepository;
import exceptions.InvalidInputException;
import exceptions.DuplicateResourceException;
import java.util.List;

public class AuthorService {
    private final CrudRepository<Author> authorRepository;
    
    public AuthorService(CrudRepository<Author> authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    public Author createAuthor(String name, String country) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Author name cannot be empty");
        }
        
        List<Author> existingAuthors = authorRepository.findByCondition(a -> 
            a.getName().equalsIgnoreCase(name));
        
        if (!existingAuthors.isEmpty()) {
            throw new DuplicateResourceException("Author '" + name + "' already exists");
        }
        
        Author author = new Author(0, name, country);
        return authorRepository.save(author);
    }
    
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    
    public List<Author> searchAuthorsByName(String keyword) {
        return authorRepository.findByCondition(a -> 
            a.getName().toLowerCase().contains(keyword.toLowerCase()));
    }
    
    public void deleteAuthor(int id) {
        authorRepository.delete(id);
    }
}
