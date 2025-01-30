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
        // TODO: add exception handling in separate files
        
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

	public boolean deletePost(Long id) {
		Optional<Post> box = postRepo.findById(id);
	    if (box.isEmpty()) return false;
	    
	    Post post = box.get();
	    post.setUser(null);
	    
	    postRepo.delete(post);
	    return true;
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
        return false; // Already liked
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
        return false; // Not liked
    }
	
}