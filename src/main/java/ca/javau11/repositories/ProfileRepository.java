package ca.javau11.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.javau11.entities.Profile;
import ca.javau11.entities.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {

	Optional<Profile> findByUser(User user);
	@Query("SELECT p FROM Profile p WHERE p.user.id = :userId")
    Optional<Profile> findByUserId(@Param("userId") Long userId);

}
