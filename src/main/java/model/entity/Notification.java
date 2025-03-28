package model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;


    private String message_en;
    private String message_ur;
    private String message_ru;

    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessageEnglish() {
        return message_en;
    }

    public void setMessageEnglish(String message) {
        this.message_en = message;
    }


    public String getMessageUrdu() {
        return message_ur;
    }

    public void setMessageUrdu(String message) {
        this.message_ur = message;
    }

    public String getMessageRussian() {
        return message_ru;
    }

    public void setMessageRussian(String message) {
        this.message_ru = message;
    }

    // New method to get message based on current language
    public String getMessage(String currentLanguage) {
        switch (currentLanguage) {
            case "Русский":
                return getMessageRussian();
            case "اردو":
                return getMessageUrdu();
            case "English":
            default:
                return getMessageEnglish();
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }


    public Long getReservationId() {
        return this.reservation.getReservationId();
    }

}