package model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

/*
 * Reservation class representing a reservation entity in the database.
 * It contains fields for reservation ID, user, book, borrow date, and due date.
 * The class is annotated with JPA annotations to map it to the database table.
 */
@Entity
@Table(name = "reservation")
public class Reservation {
    /**
     * Unique identifier for the reservation.
     */
    @Id
    /* reservationId: Unique identifier for the reservation.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    /**
     * user: The user who made the reservation.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * book: The book being reserved.
     */
    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * borrowDate: The date and time when the book was borrowed.
     */
    private LocalDateTime borrowDate;


    /**
     * Default constructor for the Reservation class.
     * Initializes the user and book fields to new instances of User and Book.
     */
    public Reservation() {
        this.user = new User(); // Initialize the user field
        this.book = new Book(); // Initialize the book field
    }


    /**
     *  local date variable.
     */
    private LocalDateTime dueDate;

    /**
     * @return the reservation ID.
     */
    public Long getReservationId() {
        return reservationId;
    }

    /**
     * Sets the reservation ID.
     * @param reservationIdNumber the reservation ID to set.
     */
    public void setReservationId(final Long reservationIdNumber) {
        this.reservationId = reservationIdNumber;
    }

    /**
     * Getters and setters for the user, book, borrow date, and due date fields.
     * @return the user associated with the reservation.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the reservation.
     * @param userSelected the user to set.
     */
    public void setUser(final User userSelected) {
        this.user = userSelected;
    }

    /**
     * Getters and setters for the book field.
     * @return the book associated with the reservation.
     */

    public Book getBook() {
        return book;
    }

    /**
     * Sets the book associated with the reservation.
     * @param bookSelected the book to set.
     */
    public void setBook(final Book bookSelected) {
        this.book = bookSelected;
    }

    /**
     * @return the borrow date of the reservation.
     */
    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    /**
     * Sets the borrow date of the reservation.
     * @param borrowDateSelected the borrow date to set.
     */
    public void setBorrowDate(final LocalDateTime borrowDateSelected) {
        this.borrowDate = borrowDateSelected;
    }

    /**
     * Getters and setters for the due date field.
     * @return the due date of the reservation.
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of the reservation.
     * @param dueDateSelected the due date to set.
     */
    public void setDueDate(final LocalDateTime dueDateSelected) {
        this.dueDate = dueDateSelected;
    }

    /**
     * Getters and setters for the user ID and book ID fields.
     * @return the user ID associated with the reservation.
     */
    public Long getUserId() {
        return user.getUserId();
    }

    /**
     * @return the book ID associated with the reservation.
     */
    public Long getBookId() {
        return book.getBookId();
    }

    /**
     * @return the username of the user associated with the reservation.
     */
    public String getUserName() {
        return user.getUsername();
    }

    /**
     * @param userId the user ID to set.
     */
    public void setUserId(final Long userId) {
        this.user.setUserId(userId);
    }

    /**
     * Getters and setters for the book ID field.
     * @param bookId book ID associated with the reservation.
     */
    public void setBookId(final Long bookId) {
        this.book.setBookId(bookId);
    }
}
