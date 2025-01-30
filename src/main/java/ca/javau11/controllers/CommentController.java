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

import ca.javau11.entities.Comment;
import ca.javau11.services.CommentService;

@RestController
public class CommentController {

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@GetMapping("post/{postId}/comments")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
    
    @GetMapping("post/comment/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Long commentId) {
        Optional<Comment> box = commentService.getCommentById(commentId);
        return ResponseEntity.of(box);
    }
    
    @PostMapping("post/{postId}/comment")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long postId,
            @RequestBody Comment comment) {
        Optional<Comment> newComment = commentService.addCommentToPost(postId, comment);
        return newComment
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PutMapping("post/comment/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long id,
            @RequestBody Comment comment) {
        Optional<Comment> updatedComment = commentService.updateComment(id, comment);
        return ResponseEntity.of(updatedComment);
    }
    
    @DeleteMapping("post/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        boolean isDeleted = commentService.deleteComment(commentId);
        return isDeleted
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
	
}