package ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.entities.Comment;
import ca.javau11.entities.Post;
import ca.javau11.repositories.PostRepository;

@Service
public class CommentService {

	private PostRepository postRepo;

	public CommentService(PostRepository postRepo) {
		this.postRepo = postRepo;
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

	public Optional<Comment> updateComment(Long id, Comment commentToUpdate) {
		for (Post post : postRepo.findAll()) {
            Optional<Comment> commentOptional = post.getComments().stream()
                    .filter(comment -> comment.getId().equals(id))
                    .findFirst();

            if (commentOptional.isPresent()) {
                Comment comment = commentOptional.get();
                comment.setText(commentToUpdate.getText());

                postRepo.save(post);
                return Optional.of(comment);
            }
        }
        return Optional.empty();
	}

	public boolean deleteComment(Long id) {
		for (Post post : postRepo.findAll()) {
	        Optional<Comment> commentOptional = post.getComments().stream()
	                .filter(comment -> comment.getId().equals(id))
	                .findFirst();

	        if (commentOptional.isPresent()) {
	            Comment commentToRemove = commentOptional.get();
	            post.getComments().remove(commentToRemove);

	            postRepo.save(post);
	            return true;
	        }
	    }

	    return false;
	}
	
}