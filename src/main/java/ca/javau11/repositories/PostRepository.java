package ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.javau11.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

}