package model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

/**
 * Represents the relationship between a book and an author
 * in the library system.
 */
@Entity
@Table(name = "writes")
public final class Writes {

    /**
     * The unique identifier for this Writes record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The book associated with this author-book relationship.
     */
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * The author associated with this author-book relationship.
     */
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    /**
     * Default constructor required by JPA.
     */
    public Writes() {
    }

    /**
     * Constructs a new Writes relationship between a book and an author.
     *
     * @param bookRef   the book in the relationship
     * @param authorRef the author in the relationship
     */
    public Writes(final Book bookRef, final Author authorRef) {
        this.book = bookRef;
        this.author = authorRef;
    }

    /**
     * Returns the ID of this Writes record.
     *
     * @return the ID
     */
    public Long getWritesId() {
        return id;
    }

    /**
     * Sets the ID of this Writes record.
     *
     * @param idValue the ID to set
     */
    public void setWritesId(final Long idValue) {
        this.id = idValue;
    }

    /**
     * Returns the book associated with this Writes relationship.
     *
     * @return the book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book for this Writes relationship.
     *
     * @param bookRef the book to set
     */
    public void setBook(final Book bookRef) {
        this.book = bookRef;
    }

    /**
     * Returns the author associated with this Writes relationship.
     *
     * @return the author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Sets the author for this Writes relationship.
     *
     * @param authorRef the author to set
     */
    public void setAuthor(final Author authorRef) {
        this.author = authorRef;
    }
}
