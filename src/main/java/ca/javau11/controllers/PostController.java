package ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.Post;
import ca.javau11.services.PostService;

@RestController
public class PostController {

	private PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
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
    
    @DeleteMapping("post/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) {
		boolean isDeleted = postService.deletePost(id);
		return isDeleted? 
				ResponseEntity.ok().build() 
				: ResponseEntity.notFound().build();
	}
    
    @PostMapping("post/{postId}/like/{userId}")
    public ResponseEntity<?> likePost(@PathVariable Long userId, @PathVariable Long postId) {
        boolean liked = postService.likePost(userId, postId);
        if (liked) {
            return ResponseEntity.ok("Post liked successfully.");
        }
        return ResponseEntity.badRequest().body("Post already liked.");
    }

    @DeleteMapping("post/{postId}/unlike/{userId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long userId, @PathVariable Long postId) {
        boolean unliked = postService.unlikePost(userId, postId);
        if (unliked) {
            return ResponseEntity.ok("Post unliked successfully.");
        }
        return ResponseEntity.badRequest().body("Post was not liked.");
    }
	
}