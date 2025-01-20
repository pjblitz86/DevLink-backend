package ca.javau11.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ca.javau11.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>  {
	User findByEmail(String email);
}
