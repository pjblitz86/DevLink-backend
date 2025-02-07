package ca.javau11.services;

import ca.javau11.entities.Comment;
import ca.javau11.entities.Post;
import ca.javau11.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private PostRepository postRepo;

    @InjectMocks
    private CommentService commentService;

    private Post post;
    private Comment comment;

    private final Long postId = 1L;
    private final Long commentId = 10L;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(postId);
        post.setText("Test Post");
        post.setComments(new ArrayList<>());

        comment = new Comment();
        comment.setId(commentId);
        comment.setText("Test Comment");
        comment.setPost(post);

        post.getComments().add(comment);
        lenient().when(postRepo.findById(postId)).thenReturn(Optional.of(post));
    }

    @Test
    void testGetCommentsByPostId_Success() {
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));

        List<Comment> comments = commentService.getCommentsByPostId(postId);
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test Comment", comments.get(0).getText());

        verify(postRepo, times(1)).findById(postId);
    }

    @Test
    void testGetCommentsByPostId_PostNotFound() {
        when(postRepo.findById(postId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            commentService.getCommentsByPostId(postId)
        );

        assertEquals("Post not found for ID: " + postId, exception.getMessage());
        verify(postRepo, times(1)).findById(postId);
    }

    @Test
    void testGetCommentById_Success() {
        when(postRepo.findAll()).thenReturn(List.of(post));

        Optional<Comment> foundComment = commentService.getCommentById(commentId);
        assertTrue(foundComment.isPresent());
        assertEquals(commentId, foundComment.get().getId());
    }

    @Test
    void testGetCommentById_NotFound() {
        when(postRepo.findAll()).thenReturn(List.of(post));

        Optional<Comment> foundComment = commentService.getCommentById(999L);
        assertTrue(foundComment.isEmpty());
    }

    @Test
    void testAddCommentToPost_Success() {
        Comment newComment = new Comment();
        newComment.setId(20L);
        newComment.setText("New Test Comment");

        when(postRepo.findById(postId)).thenReturn(Optional.of(post));
        when(postRepo.save(any(Post.class))).thenReturn(post);

        Optional<Comment> addedComment = commentService.addCommentToPost(postId, newComment);
        assertTrue(addedComment.isPresent());
        assertEquals("New Test Comment", addedComment.get().getText());
        assertEquals(post, addedComment.get().getPost());

        verify(postRepo, times(1)).save(post);
    }

    @Test
    void testAddCommentToPost_PostNotFound() {
        when(postRepo.findById(postId)).thenReturn(Optional.empty());

        Comment newComment = new Comment();
        newComment.setText("Another Comment");

        Optional<Comment> addedComment = commentService.addCommentToPost(postId, newComment);
        assertTrue(addedComment.isEmpty());

        verify(postRepo, never()).save(any(Post.class));
    }
}
