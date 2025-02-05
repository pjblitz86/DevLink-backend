package ca.javau11.controllers;

import ca.javau11.entities.Comment;
import ca.javau11.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment comment;
    private Long postId = 1L;
    private Long commentId = 100L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        comment = new Comment();
        comment.setId(commentId);
        comment.setText("This is a test comment.");
    }

    @Test
    void testGetCommentsByPost() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentService.getCommentsByPostId(postId)).thenReturn(comments);

        List<Comment> response = commentController.getCommentsByPost(postId);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("This is a test comment.", response.get(0).getText());
        verify(commentService, times(1)).getCommentsByPostId(postId);
    }

    @Test
    void testGetCommentById_Found() {
        when(commentService.getCommentById(commentId)).thenReturn(Optional.of(comment));

        ResponseEntity<Comment> response = commentController.getComment(commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a test comment.", response.getBody().getText());
        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    void testGetCommentById_NotFound() {
        when(commentService.getCommentById(commentId)).thenReturn(Optional.empty());

        ResponseEntity<Comment> response = commentController.getComment(commentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    void testAddComment_Success() {
        when(commentService.addCommentToPost(postId, comment)).thenReturn(Optional.of(comment));

        ResponseEntity<Comment> response = commentController.addComment(postId, comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a test comment.", response.getBody().getText());
        verify(commentService, times(1)).addCommentToPost(postId, comment);
    }

    @Test
    void testAddComment_Failure() {
        when(commentService.addCommentToPost(postId, comment)).thenReturn(Optional.empty());

        ResponseEntity<Comment> response = commentController.addComment(postId, comment);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(commentService, times(1)).addCommentToPost(postId, comment);
    }
}
