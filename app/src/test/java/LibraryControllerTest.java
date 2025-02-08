// src/test/java/controller/LibraryControllerTest.java

//package main.controller;

import controller.LibraryController;
import model.dao.impl.BaseDao;
import model.entity.Reservation;
import model.entity.User;
import org.mockito.ArgumentCaptor;
import service.ReservationService;
import service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

public class LibraryControllerTest extends BaseDao {
    private LibraryController libraryController;
    private UserService userService;
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mock the connection
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/library_db", "library_user", "library_password");
        BaseDao.setConnection(connection);

        userService = Mockito.mock(UserService.class);
        reservationService = Mockito.mock(ReservationService.class);
        libraryController = new LibraryController();
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

//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setEmail(email);
//        user.setPhone(phone);
//        user.setRole(role);
//        user.setBookCount(bookCount);
//        user.setCreatedAt(createdAt);
//        user.setDeletedAt(deletedAt);
//
//
//
//        verify(userService).registerUser(any(User.class));
    }

    @Test
    public void testReserveBook() {
        Long userId = 1L;
        Long bookId = 2L;

        libraryController.reserveBook(userId, bookId);

        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationService).createReservation(reservationCaptor.capture());

        Reservation capturedReservation = reservationCaptor.getValue();
        assertEquals(userId, capturedReservation.getUserId());
        assertEquals(bookId, capturedReservation.getBookId());
    }

}