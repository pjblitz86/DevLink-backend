package ca.javau11.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.javau11.entities.User;
import ca.javau11.services.UserService;
import ca.javau11.utils.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("testuser");
        mockUser.setEmail("testuser@example.com");
    }

    @Test
    void createUser_Success() {
        Response<User> response = new Response<>("User registered", mockUser);
        when(userService.addUser(any(User.class))).thenReturn(response);

        ResponseEntity<Response<User>> result = userController.createUser(mockUser);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("User registered", result.getBody().getMessage());
        assertEquals(mockUser, result.getBody().getData());
    }

    @Test
    void loginUser_Success() {
        Response<User> response = new Response<>("Login successful", mockUser);
        when(userService.loginUser(any(User.class))).thenReturn(response);

        ResponseEntity<Response<User>> result = userController.loginUser(mockUser);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Login successful", result.getBody().getMessage());
    }

    @Test
    void getUser_Success() {
        when(userService.getUserById(1L)).thenReturn(mockUser);

        User result = userController.getUser(1L);

        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    @Test
    void deleteUser_Success() {
        when(userService.deleteUserById(1L)).thenReturn(true);

        boolean result = userController.deleteUser(1L);

        assertTrue(result);
    }
}
