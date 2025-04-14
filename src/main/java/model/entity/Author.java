package model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an author entity in the system.
 */
@Entity
@Table(name = "author")
public final class Author {

    /**
     * The unique identifier for the author.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    /**
     * The first name of the author.
     */
    private String firstName;

    /**
     * The last name of the author.
     */
    private String lastName;

    /**
     * A brief description of the author.
     */
    private String description;

    /**
     * The date of birth of the author.
     */
    private LocalDate dateOfBirth;

    /**
     * The place of birth of the author.
     */
    private String placeOfBirth;

    /**
     * The profile image of the author.
     */
    private String profileImage;

    /**
     * The set of writings associated with the author.
     */
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Writes> writes = new HashSet<>();

    /**
     * Default constructor.
     */
    public Author() {
    }

    /**
     * Gets the unique identifier for the author.
     *
     * @return the author ID
     */
    public Long getAuthorId() {
        return authorId;
    }

    /**
     * Sets the unique identifier for the author.
     *
     * @param authorIdNo the author ID to set
     */
    public void setAuthorId(final Long authorIdNo) {
        this.authorId = authorIdNo;
    }

    /**
     * Gets the first name of the author.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the author.
     *
     * @param authorFirstName the first name to set
     */
    public void setFirstName(final String authorFirstName) {
        this.firstName = authorFirstName;
    }

    /**
     * Gets the last name of the author.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the author.
     *
     * @param authorLastName the last name to set
     */
    public void setLastName(final String authorLastName) {
        this.lastName = authorLastName;
    }

    /**
     * Gets the description of the author.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the author.
     *
     * @param bookDescription the description to set
     */
    public void setDescription(final String bookDescription) {
        this.description = bookDescription;
    }

    /**
     * Gets the date of birth of the author.
     *
     * @return the date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the author.
     *
     * @param authorDateOfBirth the date of birth to set
     */
    public void setDateOfBirth(final LocalDate authorDateOfBirth) {
        this.dateOfBirth = authorDateOfBirth;
    }

    /**
     * Gets the place of birth of the author.
     *
     * @return the place of birth
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the place of birth of the author.
     *
     * @param authorPlaceOfBirth the place of birth to set
     */
    public void setPlaceOfBirth(final String authorPlaceOfBirth) {
        this.placeOfBirth = authorPlaceOfBirth;
    }

    /**
     * Gets the profile image of the author.
     *
     * @return the profile image
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * Sets the profile image of the author.
     *
     * @param authorProfileImage the profile image to set
     */
    public void setProfileImage(final String authorProfileImage) {
        this.profileImage = authorProfileImage;
    }

    /**
     * Gets the set of writings associated with the author.
     *
     * @return the set of writings
     */
    public Set<Writes> getWrites() {
        return writes;
    }

    /**
     * Sets the set of writings associated with the author.
     *
     * @param authorWrites the set of writings to set
     */
    public void setWrites(final Set<Writes> authorWrites) {
        this.writes = authorWrites;
    }
}
