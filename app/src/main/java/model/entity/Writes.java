package model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "writes")
public class Writes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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