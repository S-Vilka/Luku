package model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private LocalDate publicationDate;
    private String description;
    private String availabilityStatus;
    private String category;
    private String language;
    private String isbn;
    private String location;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Writes> writes = new HashSet<>();

    // Default constructor
    public Book() {}

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Writes> getWrites() {
        return writes;
    }

    public void setWrites(Set<Writes> writes) {
        this.writes = writes;
    }

    public void setAuthors(Set<Author> authors) {
        this.writes.clear();
        for (Author author : authors) {
            Writes writes = new Writes(this, author);
            this.writes.add(writes);
        }
    }

    public Set<Author> getAuthors() {
        Set<Author> authors = new HashSet<>();
        for (Writes writes : this.writes) {
            authors.add(writes.getAuthor());
        }
        return authors;
    }
}