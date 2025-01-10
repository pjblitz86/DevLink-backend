package ca.javau11.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import ca.javau11.entities.User;
import ca.javau11.services.UserService;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User mockUser = new User("John Doe", "john.doe@example.com");
        mockUser.setId(1L);
        when(userService.addUser(any(User.class))).thenReturn(mockUser);

        // Act
        User result = userController.createUser(new User("John Doe", "john.doe@example.com"));

        // Assert
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(1L, result.getId());
    }
}
