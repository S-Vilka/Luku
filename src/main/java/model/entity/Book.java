package model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books") // Corrected table name
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id") // Ensure it matches the database column
    private Long bookId;

    @Column(name = "title_en", nullable = false)
    private String title_en;

    @Column(name = "title_ur", nullable = false)
    private String title_ur;

    @Column(name = "title_ru", nullable = false)
    private String title_ru;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "description")
    private String description;

    @Column(name = "availability_status")
    private String availabilityStatus;

    @Column(name = "category")
    private String category;

    @Column(name = "language")
    private String language;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "location")
    private String location;

    @Column(name = "cover_image") // Ensures column is mapped correctly
    private String coverImage;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Writes> writes = new HashSet<>();

    // Default constructor
    public Book() {
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitleEn() {
        return title_en;
    }

    public void setTitleEn(String title) {
        this.title_en = title;
    }


    public String getTitleUr() {
        return title_ur;
    }

    public void setTitleUr(String title) {
        this.title_ur = title;
    }


    public String getTitleRu() {
        return title_ru;
    }

    public void setTitleRu(String title) {
        this.title_ru = title;
    }

    // New method to get message based on current language
    public String getTitle(String currentLanguage) {
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

    public void setTitle(String title, String currentLanguage) {
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
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