package ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.entities.Post;
import ca.javau11.entities.User;
import ca.javau11.repositories.PostRepository;
import ca.javau11.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class PostService {

	private PostRepository postRepo;
	private UserRepository userRepo;

	public PostService(PostRepository postRepo, UserRepository userRepo) {
		this.postRepo = postRepo;
		this.userRepo = userRepo;
	}

	public List<Post> getPosts() {
		return postRepo.findAll();
	}

	public Optional<Post> getPost(Long id) {
		return postRepo.findById(id);
	}

	public Post addPost(Long userId, Post post) {
		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);
        return postRepo.save(post);
	}

	public Optional<Post> updatePost(Long id, Post post) {
		Optional<Post> box = postRepo.findById(id);
		if(box.isPresent()) {
			Post existingPost = box.get(); 
			existingPost.setText(post.getText());
			existingPost.setName(post.getName());
			return Optional.of(postRepo.save(existingPost));
		}
		
		return Optional.empty();
	}

	public boolean deletePost(Long postId, Long userId) {
	    Optional<Post> box = postRepo.findById(postId);
	    if (box.isEmpty()) {
	        throw new RuntimeException("Post not found.");
	    }

	    Post post = box.get();
	    User authenticatedUser = userRepo.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    if (!post.getUser().getId().equals(authenticatedUser.getId())) {
	        throw new RuntimeException("You do not have permission to delete this post.");
	    }

	    try {
	        postRepo.delete(post);
	        return true;
	    } catch (Exception e) {
	        throw new RuntimeException("Error occurred while deleting the post.");
	    }
	}

	
	@Transactional
    public boolean likePost(Long userId, Long postId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepo.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!user.getLikedPosts().contains(post)) {
            user.getLikedPosts().add(post);
            post.getLikes().add(user);
            userRepo.save(user);
            postRepo.save(post);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean unlikePost(Long userId, Long postId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepo.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        if (user.getLikedPosts().contains(post)) {
            user.getLikedPosts().remove(post);
            post.getLikes().remove(user);
            userRepo.save(user);
            postRepo.save(post);
            return true;
        }
        return false;
    }
	
}