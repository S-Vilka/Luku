package model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;

/**
 * Represents a notification entity in the system.
 * Each notification is associated with a user
 * and may be linked to a reservation.
 * It contains messages in multiple languages
 * and a timestamp for when it was created.
 */
@Entity
@Table(name = "notifications")
public class Notification {

    /**
     * Unique identifier for the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    /**
     * The user associated with this notification.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The reservation associated with this notification (optional).
     */
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;


    // Messages in different languages
    /**
     * Message in English.
     */
    private String messageEn;
    /**
     * Message in Urdu.
     */
    private String messageUr;
    /**
     * Message in Russian.
     */
    private String messageRu;
    /**
     * Timestamp when the notification was created.
     */
    private LocalDateTime createdAt;

    /**
     * Default constructor for the Notification class.
     * @return gives the notificationId of the notification.
     */
    public Long getNotificationId() {
        return notificationId;
    }

    /**
     * Sets the unique identifier for the notification.
     *
     * @param id The unique identifier to set.
     */
    public void setNotificationId(final Long id) {
        this.notificationId = id;
    }

    /**
     * Gets the user associated with this notification.
     *
     * @return the specified user.
     */
    public User getUser() {

        return user;
    }

    /**
     * Sets the user associated with this notification.
     *
     * @param userObject The user to set.
     */
    public void setUser(final User userObject) {

        this.user = userObject;
    }

    /**
     * Gets the message in English.
     *
     * @return The message in English.
     */
    public String getMessageEnglish() {
        return messageEn;
    }

    /**
     * Sets the message in English.
     *
     * @param message The message to set in English.
     */
    public void setMessageEnglish(final String message) {
        this.messageEn = message;
    }

    /**
     * Gets the message in Urdu.
     *
     * @return The message in Urdu.
     */
    public String getMessageUrdu() {
        return messageUr;
    }

    /**
     * Sets the message in Urdu.
     *
     * @param message The message to set in Urdu.
     */
    public void setMessageUrdu(final String message) {
        this.messageUr = message;
    }

    /**
     * Gets the message in Russian.
     *
     * @return The message in Russian.
     */
    public String getMessageRussian() {
        return messageRu;
    }

    /**
     * Sets the message in Russian.
     *
     * @param message The message to set in Russian.
     */
    public void setMessageRussian(final String message) {
        this.messageRu = message;
    }

    /**
     * Gets the message in the specified language.
     *
     * @param currentLanguage The current language to get the message in.
     * @return The message in the specified language.
     */
    public String getMessage(final String currentLanguage) {
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

    /**
     * Gets the timestamp when the notification was created.
     *
     * @return The timestamp of creation.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the notification was created.
     *
     * @param createdTime The timestamp to set.
     */
    public void setCreatedAt(final LocalDateTime createdTime) {
        this.createdAt = createdTime;
    }

    /**
     * Gets the reservation associated with this notification.
     *
     * @return The reservation associated with this notification.
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Sets the reservation associated with this notification.
     *
     * @param reservationObject The reservation to set.
     */
    public void setReservation(final Reservation reservationObject) {

        this.reservation = reservationObject;
    }
    /** Gets the unique identifier of the
     * reservation associated with this notification.
     * @return The unique identifier of the reservation.
     */
    public Long getReservationId() {
        return this.reservation.getReservationId();
    }

}
