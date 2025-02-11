// src/test/java/controller/LibraryControllerTest.java

//package main.controller;

import controller.LibraryController;
import model.dao.impl.BaseDao;
import model.entity.Book;
import model.entity.Reservation;
import model.entity.User;
import org.mockito.ArgumentCaptor;
import service.AuthorService;
import service.BookService;
import service.ReservationService;
import service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

public class LibraryControllerTest extends BaseDao {
    private LibraryController libraryController;
    private UserService userService;
    private ReservationService reservationService;
    private AuthorService authorService;
    private BookService bookService;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mock the connection
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/library_db", "library_user", "library_password");
        BaseDao.setConnection(connection);

        userService = Mockito.mock(UserService.class);
        reservationService = Mockito.mock(ReservationService.class);
        authorService = Mockito.mock(AuthorService.class);
        bookService = Mockito.mock(BookService.class);
        libraryController = new LibraryController();

        // Inject the mocked services into the libraryController
        libraryController.setUserService(userService);
        libraryController.setReservationService(reservationService);
        libraryController.setAuthorService(authorService);
        libraryController.setBookService(bookService);
    }

    @Test
    public void testRegisterUser() {
        String username = "testUser";
        String password = "testPass";
        String email = "test@example.com";
        String phone = "1234567890";
        String role = "user";
        int bookCount = 5;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime deletedAt = null;

        libraryController.registerUser(username, password, email, phone, role, bookCount, createdAt, deletedAt);
        print("registerUser");
    }

    @Test
    public void testReserveBook() {
        Long userId = 1L;
        Long bookId = 2L;

        // Mock the User object
        User user = new User();
        user.setUserId(userId);
        user.setRole("student");
        user.setBookCount(3);

        // Mock the behavior of userService.getUserById
        Mockito.when(userService.getUserById(userId)).thenReturn(user);

        // Call the method to be tested
        libraryController.reserveBook(userId, bookId);

        // Verify that the reservationService.createReservation method was called
        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationService).createReservation(reservationCaptor.capture());

        Reservation capturedReservation = reservationCaptor.getValue();
        assertEquals(userId, capturedReservation.getUserId());
        assertEquals(bookId, capturedReservation.getBookId());

        // Verify that the user's book count was updated
        assertEquals(4, user.getBookCount());
        verify(userService).updateUser(user);
    }


    @Test
    public void extendDueDateTest() {
        Long reservationId = 1L;
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(7);

        // Mock the behavior of reservationService.getReservationById
        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        Mockito.when(reservationService.getReservationById(reservationId)).thenReturn(reservation);

        // Call the method to be tested
        libraryController.extendDueDate(reservationId, newDueDate);

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationService).updateReservation(reservationCaptor.capture());

        Reservation capturedReservation = reservationCaptor.getValue();
        assertEquals(reservationId, capturedReservation.getReservationId());
        assertEquals(newDueDate, capturedReservation.getDueDate());
    }

    @Test
    public void testSearchBooksByAuthor() {
        String authorFirstName = "John";
        String authorLastName = "Doe";

        // Mock the list of books
        List<Book> books = Arrays.asList(new Book(), new Book());

        // Mock the behavior of authorService.getBooksByAuthor
        Mockito.when(authorService.getBooksByAuthor(authorFirstName, authorLastName)).thenReturn(books);

        // Call the method to be tested
        List<Book> result = libraryController.searchBooksByAuthor(authorFirstName, authorLastName);

        // Verify the result
        assertEquals(books, result);
        verify(authorService).getBooksByAuthor(authorFirstName, authorLastName);
    }

    @Test
    public void testSearchBooksByTitle() {
        String title = "The Great Gatsby";

        // Mock the list of books
        List<Book> books = Arrays.asList(new Book(), new Book());

        // Mock the behavior of authorService.getBooksByAuthor
        Mockito.when(bookService.getBooksByTitle(title)).thenReturn(books);

        // Call the method to be tested
        List<Book> result = libraryController.searchBooksByTitle(title);

        // Verify the result
        assertEquals(books, result);
        verify(bookService).getBooksByTitle(title);
    }

    @Test
    public void testSearchBooksByCategory() {
        String category = "Fiction";

        // Mock the list of books
        List<Book> books = Arrays.asList(new Book(), new Book());

        // Mock the behavior of authorService.getBooksByAuthor
        Mockito.when(bookService.getBooksByCategory(category)).thenReturn(books);

        // Call the method to be tested
        List<Book> result = libraryController.searchBooksByCategory(category);

        // Verify the result
        assertEquals(books, result);
        verify(bookService).getBooksByCategory(category);
    }

    @Test
    public void testSearchBooksByLanguage() {
        String language = "English";

        // Mock the list of books
        List<Book> books = Arrays.asList(new Book(), new Book());

        // Mock the behavior of authorService.getBooksByAuthor
        Mockito.when(bookService.getBooksByLanguage(language)).thenReturn(books);

        // Call the method to be tested
        List<Book> result = libraryController.searchBooksByLanguage(language);

        // Verify the result
        assertEquals(books, result);
        verify(bookService).getBooksByLanguage(language);
    }

    @Test
    public void testGetUserBookCount() {
        Long userId = 1L;

        // Mock the User object
        User user = new User();
        user.setUserId(userId);
        user.setRole("student");
        user.setBookCount(3);

        // Mock the behavior of userService.getUserBookCount
        Mockito.when(userService.getUserBookCount(userId)).thenReturn(user.getBookCount());


        // Call the method to be tested
        int bookCount = libraryController.getUserBookCount(userId);

        // Verify that the userService.getUserBookCount method was called
        verify(userService).getUserBookCount(userId);

        // Verify the returned book count
        assertEquals(3, bookCount);
    }

    @Test
    public void testGetMyBookings() {
        Long userId = 1L;

        // Mock the list of reservations
        List<Reservation> reservations = Arrays.asList(
                new Reservation(),
                new Reservation()
        );

        // Mock the behavior of reservationService.getReservationsByUserId
        Mockito.when(reservationService.getReservationsByUserId(userId)).thenReturn(reservations);

        // Call the method to be tested
        List<Reservation> result = libraryController.getMyBookings(userId);

        // Verify that the reservationService.getReservationsByUserId method was called
        verify(reservationService).getReservationsByUserId(userId);

    }

    @Test
    public void testUpdateUserInfo() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setEmail("test@test.com");
        user.setPhone("1234567890");
        user.setRole("user");
        user.setBookCount(5);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeletedAt(null);


        // Mock the behavior of userService.updateUser
        Mockito.doNothing().when(userService).updateUser(user);
        libraryController.updateUserInfo(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).updateUser(userCaptor.capture());

        // Verify the captured user
        User capturedUser = userCaptor.getValue();
        assertEquals(user.getUserId(), capturedUser.getUserId());
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.getPassword(), capturedUser.getPassword());
        assertEquals(user.getEmail(), capturedUser.getEmail());
        assertEquals(user.getPhone(), capturedUser.getPhone());
        assertEquals(user.getRole(), capturedUser.getRole());
        assertEquals(user.getBookCount(), capturedUser.getBookCount());
        assertEquals(user.getCreatedAt(), capturedUser.getCreatedAt());
        assertEquals(user.getDeletedAt(), capturedUser.getDeletedAt());

    }



}