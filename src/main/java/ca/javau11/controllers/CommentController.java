package ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.Comment;
import ca.javau11.service.CustomUserDetails;
import ca.javau11.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@GetMapping("/{postId}/comments")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
    
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Long commentId) {
        Optional<Comment> box = commentService.getCommentById(commentId);
        return ResponseEntity.of(box);
    }
    
    @PostMapping("/{postId}/comment")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @RequestBody Comment comment) {
        Optional<Comment> newComment = commentService.addCommentToPost(postId, comment);
        return newComment
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails authenticatedUser) {

        logger.debug("Deleting comment with ID: {} for authenticated user: {}", commentId,
                authenticatedUser != null ? authenticatedUser.getUsername() : "null");

        if (authenticatedUser == null)
            return ResponseEntity.status(403).body("Unauthorized: No authenticated user.");

        boolean deleted = commentService.deleteCommentById(commentId);
        return deleted 
        	    ? ResponseEntity.ok("Comment deleted successfully.") 
        	    : ResponseEntity.status(403).body("You are not authorized to delete this comment.");

    }
}