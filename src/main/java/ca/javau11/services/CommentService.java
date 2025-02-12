package ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.entities.Comment;
import ca.javau11.entities.Post;
import ca.javau11.repositories.CommentRepository;
import ca.javau11.repositories.PostRepository;

@Service
public class CommentService {

	private PostRepository postRepo;
	private CommentRepository commentRepo;

	public CommentService(PostRepository postRepo, CommentRepository commentRepo) {
		this.postRepo = postRepo;
		this.commentRepo = commentRepo;
	}

	public List<Comment> getCommentsByPostId(Long postId) {
		return postRepo.findById(postId)
                .map(Post::getComments)
                .orElseThrow(() -> new IllegalArgumentException("Post not found for ID: " + postId));
	}

	public Optional<Comment> getCommentById(Long commentId) {
		return postRepo.findAll().stream()
                .flatMap(post -> post.getComments().stream())
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst();
	}

	public Optional<Comment> addCommentToPost(Long postId, Comment comment) {
		return postRepo.findById(postId).map(post -> {
			comment.setPost(post);
			post.getComments().add(comment);
			postRepo.save(post);
            return comment;
        });
	}
	
	public boolean deleteCommentById(Long commentId) {
        Optional<Comment> commentBox = commentRepo.findById(commentId);

        if (commentBox.isPresent()) {
            commentRepo.deleteById(commentId);
            return true;
        }
        return false;
    }
}