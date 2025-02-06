package ca.javau11.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.javau11.entities.Post;
import ca.javau11.entities.User;
import ca.javau11.repositories.PostRepository;
import ca.javau11.repositories.UserRepository;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private PostService postService;

    private User user;
    private Post post;

    private final Long userId = 1L;
    private final Long postId = 10L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setLikedPosts(new ArrayList<>());

        post = new Post();
        post.setId(postId);
        post.setText("Test Post");
        post.setUser(user);
        post.setLikes(new ArrayList<>()); 
        
        lenient().when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        lenient().when(postRepo.findById(postId)).thenReturn(Optional.of(post));
    }

    @Test
    void testGetPosts_Success() {
        List<Post> posts = List.of(post);
        when(postRepo.findAll()).thenReturn(posts);

        List<Post> result = postService.getPosts();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepo, times(1)).findAll();
    }

    @Test
    void testGetPostById_Success() {
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));

        Optional<Post> result = postService.getPost(postId);
        assertTrue(result.isPresent());
        assertEquals(postId, result.get().getId());
        verify(postRepo, times(1)).findById(postId);
    }

    @Test
    void testGetPostById_NotFound() {
        when(postRepo.findById(postId)).thenReturn(Optional.empty());

        Optional<Post> result = postService.getPost(postId);
        assertTrue(result.isEmpty());
        verify(postRepo, times(1)).findById(postId);
    }

    @Test
    void testAddPost_Success() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(postRepo.save(any(Post.class))).thenReturn(post);

        Post createdPost = postService.addPost(userId, post);
        assertNotNull(createdPost);
        assertEquals(postId, createdPost.getId());
        assertEquals(user, createdPost.getUser());
        verify(postRepo, times(1)).save(any(Post.class));
    }

    @Test
    void testUpdatePost_Success() {
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));
        when(postRepo.save(any(Post.class))).thenReturn(post);

        post.setText("Updated Text");
        Optional<Post> updatedPost = postService.updatePost(postId, post);

        assertTrue(updatedPost.isPresent());
        assertEquals("Updated Text", updatedPost.get().getText());
        verify(postRepo, times(1)).save(post);
    }

    @Test
    void testUpdatePost_NotFound() {
        when(postRepo.findById(postId)).thenReturn(Optional.empty());

        Optional<Post> updatedPost = postService.updatePost(postId, post);
        assertTrue(updatedPost.isEmpty());
        verify(postRepo, never()).save(any(Post.class));
    }

    @Test
    void testDeletePost_Success() {
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        boolean deleted = postService.deletePost(postId, userId);
        assertTrue(deleted);
        verify(postRepo, times(1)).delete(post);
    }

    @Test
    void testDeletePost_Unauthorized() {
        User otherUser = new User();
        otherUser.setId(2L);
        post.setUser(otherUser);

        when(postRepo.findById(postId)).thenReturn(Optional.of(post));
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(RuntimeException.class, () -> postService.deletePost(postId, userId));
        assertEquals("You do not have permission to delete this post.", exception.getMessage());

        verify(postRepo, never()).delete(any(Post.class));
    }

    @Test
    void testLikePost_Success() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));

        boolean liked = postService.likePost(userId, postId);
        assertTrue(liked);
        assertTrue(user.getLikedPosts().contains(post));
        assertTrue(post.getLikes().contains(user));

        verify(userRepo, times(1)).save(user);
        verify(postRepo, times(1)).save(post);
    }

    @Test
    void testUnlikePost_Success() {
        List<Post> likedPosts = new ArrayList<>();
        likedPosts.add(post);
        user.setLikedPosts(likedPosts);
        post.getLikes().add(user);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(postRepo.findById(postId)).thenReturn(Optional.of(post));

        boolean unliked = postService.unlikePost(userId, postId);
        assertTrue(unliked);
        assertFalse(user.getLikedPosts().contains(post));
        assertFalse(post.getLikes().contains(user));

        verify(userRepo, times(1)).save(user);
        verify(postRepo, times(1)).save(post);
    }
}
