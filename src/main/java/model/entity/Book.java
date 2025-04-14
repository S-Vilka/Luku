package model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books") // Corrected table name
public final class Book {

    /**
     * The unique identifier for the book.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id") // Ensure it matches the database column
    private Long bookId;

    /**
     * The English title of the book.
     */
    @Column(name = "title_en", nullable = false)
    private String titleEn;

    /**
     * The Urdu title of the book.
     */
    @Column(name = "title_ur", nullable = false)
    private String titleUr;

    /**
     * The Russian title of the book.
     */
    @Column(name = "title_ru", nullable = false)
    private String titleRu;

    /**
     * The publication date of the book.
     */
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    /**
     * The description of the book.
     */
    @Column(name = "description")
    private String description;

    /**
     * The availability status of the book.
     */
    @Column(name = "availability_status")
    private String availabilityStatus;

    /**
     * The category of the book.
     */
    @Column(name = "category")
    private String category;

    /**
     * The language of the book.
     */
    @Column(name = "language")
    private String language;

    /**
     * The ISBN of the book.
     */
    @Column(name = "isbn")
    private String isbn;

    /**
     * The location of the book.
     */
    @Column(name = "location")
    private String location;

    /**
     * The cover image of the book.
     */
    @Column(name = "cover_image") // Ensures column is mapped correctly
    private String coverImage;

    /**
     * The set of authors who wrote the book.
     */
    @OneToMany(mappedBy = "book",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Writes> writes = new HashSet<>();

    /**
     * Default constructor for JPA.
     */
    public Book() {
    }

    // Getters and Setters
    /**
     * Gets the unique identifier for the book.
     *
     * @return the book ID
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * Sets the unique identifier for the book.
     *
     * @param bookIdno the book ID
     */
    public void setBookId(final Long bookIdno) {
        this.bookId = bookIdno;
    }

    /**
     * Gets the English title of the book.
     *
     * @return the English title
     */
    public String getTitleEn() {
        return titleEn;
    }

    /**
     * Sets the English title of the book.
     *
     * @param btitle the English title
     */
    public void setTitleEn(final String btitle) {
        this.titleEn = btitle;
    }

    /**
     * Gets the Urdu title of the book.
     *
     * @return the Urdu title
     */
    public String getTitleUr() {
        return titleUr;
    }

    /**
     * Sets the Urdu title of the book.
     *
     * @param btitle the Urdu title
     */
    public void setTitleUr(final String btitle) {
        this.titleUr = btitle;
    }

    /**
     * Gets the Russian title of the book.
     *
     * @return the Russian title
     */
    public String getTitleRu() {
        return titleRu;
    }

    /**
     * Sets the Russian title of the book.
     *
     * @param btitle the Russian title
     */
    public void setTitleRu(final String btitle) {
        this.titleRu = btitle;
    }

    // New method to get message based on current language
    /**
     * Gets the title of the book based on the current language.
     *
     * @param currentLanguage the current language
     * @return the title in the specified language
     */
    public String getTitle(final String currentLanguage) {
        switch (currentLanguage) {
            case "Русский":
                return getTitleRu();
            case "اردو":
                return getTitleUr();
            case "english":
            default:
                return getTitleEn();
        }
    }

    /**
     * Sets the title of the book based on the current language.
     *
     * @param title           the title to set
     * @param currentLanguage the current language
     */
    public void setTitle(final String title, final String currentLanguage) {
        switch (currentLanguage) {
            case "Русский":
                setTitleRu(title);
                break;
            case "اردو":
                setTitleUr(title);
                break;
            case "english":
            default:
                setTitleEn(title);
                break;
        }
    }

    /**
     * Gets the publication date of the book.
     *
     * @return the publication date
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the publication date of the book.
     *
     * @param bpublicationDate the publication date
     */
    public void setPublicationDate(final LocalDate bpublicationDate) {
        this.publicationDate = bpublicationDate;
    }

    /**
     * Gets the description of the book.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the book.
     *
     * @param bdescription the description
     */
    public void setDescription(final String bdescription) {
        this.description = bdescription;
    }

    /**
     * Gets the availability status of the book.
     *
     * @return the availability status
     */
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     * Sets the availability status of the book.
     *
     * @param bavailabilityStatus the availability status
     */
    public void setAvailabilityStatus(final String bavailabilityStatus) {
        this.availabilityStatus = bavailabilityStatus;
    }

    /**
     * Gets the category of the book.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the book.
     *
     * @param bcategory the category
     */
    public void setCategory(final String bcategory) {
        this.category = bcategory;
    }

    /**
     * Gets the language of the book.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language of the book.
     *
     * @param blanguage the language
     */
    public void setLanguage(final String blanguage) {
        this.language = blanguage;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param bisbn the ISBN
     */
    public void setIsbn(final String bisbn) {
        this.isbn = bisbn;
    }

    /**
     * Gets the location of the book.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the book.
     *
     * @param blocation the location
     */
    public void setLocation(final String blocation) {
        this.location = blocation;
    }

    /**
     * Gets the cover image of the book.
     *
     * @return the cover image
     */
    public String getCoverImage() {
        return coverImage;
    }

    /**
     * Sets the cover image of the book.
     *
     * @param bcoverImage the cover image
     */
    public void setCoverImage(final String bcoverImage) {
        this.coverImage = bcoverImage;
    }

    /**
     * Gets the set of authors who wrote the book.
     *
     * @return the set of authors
     */
    public Set<Writes> getWrites() {
        return writes;
    }

    /**
     * Sets the set of authors who wrote the book.
     *
     * @param bwrites the set of authors
     */
    public void setWrites(final Set<Writes> bwrites) {
        this.writes = bwrites;
    }

    /**
     * Sets the authors of the book.
     *
     * @param bauthors the set of authors
     */
    public void setAuthors(final Set<Author> bauthors) {
        this.writes.clear();
        for (Author author : bauthors) {
            Writes bwrites = new Writes(this, author);
            this.writes.add(bwrites);
        }
    }

    /**
     * Gets the authors of the book.
     *
     * @return the set of authors
     */
    public Set<Author> getAuthors() {
        Set<Author> authors = new HashSet<>();
        for (Writes bwrites : this.writes) {
            authors.add(bwrites.getAuthor());
        }
        return authors;
    }
}
