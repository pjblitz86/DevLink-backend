package ca.javau11.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.Post;
import ca.javau11.exceptions.PostAlreadyLikedException;
import ca.javau11.exceptions.PostNotLikedException;
import ca.javau11.repositories.PostRepository;
import ca.javau11.services.PostService;

@RestController
@CrossOrigin(origins = "*")
public class PostController {
	
	private static final Logger logger = LoggerFactory.getLogger(PostController.class);
	private PostService postService;
	private PostRepository postRepo;
	
	public PostController(PostService postService, PostRepository postRepo) {
		this.postService = postService;
		this.postRepo = postRepo;
	}
	
	@GetMapping("/posts")
	public List<Post> getPosts() {
		return postService.getPosts();
	}
    
    @GetMapping("/post/{id}")
	public ResponseEntity<Post> getPost(@PathVariable Long id) {
		Optional<Post> box = postService.getPost(id);
		return ResponseEntity.of(box);
	}
    
    @PostMapping("post/user/{userId}")
	public Post createPost(@PathVariable Long userId, @RequestBody Post post) {
		return postService.addPost(userId, post);
	}
    
    @PutMapping("post/{id}")
	public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
		Optional<Post> box = postService.updatePost(id, post);
		return ResponseEntity.of(box);
	}
    
    @GetMapping("post/test")
    	public void testEndPoint() {
    	logger.debug("test endpoint");
    }
    
    @DeleteMapping("post/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long postId, @RequestParam Long userId) {
		logger.info("call to controller");
		boolean isDeleted = postService.deletePost(postId, userId);
		return isDeleted? 
				ResponseEntity.ok().build() 
				: ResponseEntity.notFound().build();
	}
    
    @PostMapping("post/{postId}/like/{userId}")
    public ResponseEntity<?> likePost(@PathVariable Long userId, @PathVariable Long postId) {
        boolean liked = postService.likePost(userId, postId);
        if (liked) {
            Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
            return ResponseEntity.ok(Map.of("postId", postId, "likes", post.getLikes()));
        }
        throw new PostAlreadyLikedException("You have already liked this post.");
    }

    @DeleteMapping("post/{postId}/unlike/{userId}")
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