package ca.javau11.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.Post;
import ca.javau11.exceptions.PostAlreadyLikedException;
import ca.javau11.exceptions.PostNotLikedException;
import ca.javau11.repositories.PostRepository;
import ca.javau11.service.CustomUserDetails;
import ca.javau11.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	
	private static final Logger logger = LoggerFactory.getLogger(PostController.class);
	private PostService postService;
	private PostRepository postRepo;
	
	public PostController(PostService postService, PostRepository postRepo) {
		this.postService = postService;
		this.postRepo = postRepo;
	}
	
	@GetMapping
	public List<Post> getPosts() {
		return postService.getPosts();
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<Post> getPost(@PathVariable Long id) {
		Optional<Post> box = postService.getPost(id);
		return ResponseEntity.of(box);
	}
    
    @PostMapping("/user/{userId}")
	public Post createPost(@PathVariable Long userId, @RequestBody Post post) {
		return postService.addPost(userId, post);
	}
    
    @PutMapping("/{id}")
	public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
		Optional<Post> box = postService.updatePost(id, post);
		return ResponseEntity.of(box);
	}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails authenticatedUser) {
        logger.debug("Deleting post with ID: {} for authenticated user: {}", id, authenticatedUser != null ? authenticatedUser.getId() : "null");

        if (authenticatedUser == null) {
            return ResponseEntity.status(403).body("Unauthorized: No authenticated user.");
        }

        boolean deleted = postService.deletePost(id, authenticatedUser.getId());
        if (deleted) {
            return ResponseEntity.ok("Post deleted successfully.");
        } else {
            return ResponseEntity.status(403).body("You are not authorized to delete this post.");
        }
    }

    
    @PostMapping("/{postId}/like/{userId}")
    public ResponseEntity<?> likePost(@PathVariable Long userId, @PathVariable Long postId) {
        boolean liked = postService.likePost(userId, postId);
        if (liked) {
            Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
            return ResponseEntity.ok(Map.of("postId", postId, "likes", post.getLikes()));
        }
        throw new PostAlreadyLikedException("You have already liked this post.");
    }

    @DeleteMapping("/{postId}/unlike/{userId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long userId, @PathVariable Long postId) {
        boolean unliked = postService.unlikePost(userId, postId);
        if (unliked) {
            Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
            return ResponseEntity.ok(Map.of("postId", postId, "likes", post.getLikes()));
        }

        throw new PostNotLikedException("You have not liked this post, nothing to unlike.");
    }
	
}