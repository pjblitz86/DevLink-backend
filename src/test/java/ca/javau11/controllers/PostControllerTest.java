package ca.javau11.controllers;

import ca.javau11.entities.Post;
import ca.javau11.repositories.PostRepository;
import ca.javau11.services.PostService;

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

    @InjectMocks
    private PostController postController;

    private Post post;
    private Long userId = 1L;
    private Long postId = 100L;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(postId);
        post.setId(userId);
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
    void testDeletePost_NotFound() {
        when(postService.deletePost(postId, userId)).thenReturn(false);

        ResponseEntity<Void> response = postController.deletePost(postId, userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); 
    }
}
