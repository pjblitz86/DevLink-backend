package ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.javau11.entities.Post;
import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

	@Transactional
    @Modifying
    @Query(value = "DELETE FROM user_likes_post WHERE post_id = :postId", nativeQuery = true)
    void deleteLikesByPostId(Long postId);
}