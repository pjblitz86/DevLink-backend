package ca.javau11.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.javau11.entities.User;
import ca.javau11.exceptions.AuthenticationException;
import ca.javau11.exceptions.UserNotFoundException;
import ca.javau11.repositories.UserRepository;
import ca.javau11.utils.JwtUtils;
import ca.javau11.utils.Response;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("password123");
    }

    @Test
    void loginUser_SuccessfulLogin() {
        when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(testUser)).thenReturn("fake-jwt-token");

        Response<User> response = userService.loginUser(testUser);

        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("fake-jwt-token", response.getToken());
    }

    @Test
    void loginUser_UserNotFound_ShouldThrowException() {
        when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            userService.loginUser(testUser);
        });

        assertEquals("User email is required" + testUser.getEmail(), exception.getMessage());
    }


    @Test
    void loginUser_IncorrectPassword_ShouldThrowException() {
        when(userRepo.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), eq(testUser.getPassword()))).thenReturn(false); // Fix

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> userService.loginUser(testUser));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void getUserById_UserExists_ShouldReturnUser() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));

        User retrievedUser = userService.getUserById(1L);

        assertNotNull(retrievedUser);
        assertEquals(testUser.getId(), retrievedUser.getId());
        assertEquals(testUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void getUserById_UserNotFound_ShouldThrowException() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteUserById_UserExists_ShouldReturnTrue() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepo).delete(testUser);

        boolean isDeleted = userService.deleteUserById(1L);

        assertTrue(isDeleted);
        verify(userRepo, times(1)).delete(testUser);
    }

    @Test
    void deleteUserById_UserNotFound_ShouldReturnFalse() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        boolean isDeleted = userService.deleteUserById(1L);

        assertFalse(isDeleted);
        verify(userRepo, never()).delete(any(User.class));
    }
}
