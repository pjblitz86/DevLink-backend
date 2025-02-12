package ca.javau11.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ca.javau11.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
