package model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime borrowDate;


    public Reservation() {
        this.user = new User(); // Initialize the user field
        this.book = new Book(); // Initialize the book field
    }


    private LocalDateTime dueDate;

    // Getters and Setters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }


    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Long getUserId() {
        return user.getUserId();
    }

    public Long getBookId() {
        return book.getBookId();
    }

    public String getUserName() {
        return user.getUsername();
    }

    public void setUserId(Long userId) {
        this.user.setUserId(userId);
    }

    public void setBookId(Long bookId) {
        this.book.setBookId(bookId);
    }

}