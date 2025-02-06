package ca.javau11.controllers;

import ca.javau11.entities.Post;
import ca.javau11.entities.User;
import ca.javau11.repositories.PostRepository;
import ca.javau11.services.PostService;
import ca.javau11.services.UserService;
import ca.javau11.service.CustomUserDetails;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostController postController;

    private Post post;
    private User user;
    private Long userId = 1L;
    private Long postId = 100L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");

        post = new Post();
        post.setId(postId);
        post.setUser(user);
        post.setText("Test post");
        post.setLikes(new ArrayList<>());
    }

    @Test
    void testGetPostById_NotFound() {
        when(postService.getPost(postId)).thenReturn(Optional.empty());

        ResponseEntity<Post> response = postController.getPost(postId);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdatePost_NotFound() {
        when(postService.updatePost(postId, post)).thenReturn(Optional.empty());

        ResponseEntity<Post> response = postController.updatePost(postId, post);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeletePost_Success() {
        CustomUserDetails authenticatedUser = new CustomUserDetails(
            user.getId(), user.getName(), user.getPassword()
        );

        when(postService.deletePost(postId, userId)).thenReturn(true);

        ResponseEntity<String> response = postController.deletePost(postId, authenticatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post deleted successfully.", response.getBody());
    }

    @Test
    void testDeletePost_NotAuthorized() {
        CustomUserDetails authenticatedUser = new CustomUserDetails(
            user.getId(), user.getName(), user.getPassword()
        );

        when(postService.deletePost(postId, userId)).thenReturn(false);

        ResponseEntity<String> response = postController.deletePost(postId, authenticatedUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized to delete this post.", response.getBody());
    }

    @Test
    void testDeletePost_Unauthenticated() {
        ResponseEntity<String> response = postController.deletePost(postId, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized: No authenticated user.", response.getBody());
    }
}
