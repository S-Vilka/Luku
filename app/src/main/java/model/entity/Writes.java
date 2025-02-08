package model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "writes")
public class Writes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @ManyToMany
//    @JoinTable(
//            name = "writes_books",
//            joinColumns = @JoinColumn(name = "writes_id"),
//            inverseJoinColumns = @JoinColumn(name = "book_id")
//    )
//    private Set<Book> books;
//
//    @ManyToMany
//    @JoinTable(
//            name = "writes_authors",
//            joinColumns = @JoinColumn(name = "writes_id"),
//            inverseJoinColumns = @JoinColumn(name = "author_id")
//    )
//    private Set<Author> authors;
//
//    // Default constructor
//    public Writes() {}
//
//    // Constructor with id
//    public Writes(Long id) {
//        this.id = id;
//    }
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;


    public Writes() {
    }

    public Writes(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

    // Getters and Setters
    public Long getWritesId() {
        return id;
    }

    public void setWritesId(Long id) {
        this.id = id;
    }


    public Author getAuthor() {
        return author;
    }

    public Book getBook() {
        return book;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}