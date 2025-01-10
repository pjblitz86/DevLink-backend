package ca.javau11.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ca.javau11.entities.Profile;
import ca.javau11.entities.User;
import ca.javau11.repositories.ProfileRepository;
import ca.javau11.repositories.UserRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ProfileIntegrationTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setName("testuser");
        user.setPassword("password");
        user = userRepository.save(user);
    }

    @Test
    @Rollback
    public void testCreateProfile() {
        Profile profile = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        profile.setUser(user);
        Profile savedProfile = profileRepository.save(profile);

        assertThat(savedProfile.getId()).isNotNull();
        assertThat(savedProfile.getUser().getName()).isEqualTo("testuser");
        assertThat(savedProfile.getCompany()).isEqualTo("Company1");
    }

    @Test
    @Rollback
    public void testFindProfileById() {
        Profile profile = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        profile.setUser(user);
        Profile savedProfile = profileRepository.save(profile);

        Optional<Profile> foundProfile = profileRepository.findById(savedProfile.getId());

        assertThat(foundProfile).isPresent();
        assertThat(foundProfile.get().getCompany()).isEqualTo("Company1");
    }

    @Test
    @Rollback
    public void testUpdateProfile() {
        Profile profile = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        profile.setUser(user);
        Profile savedProfile = profileRepository.save(profile);

        savedProfile.setCompany("UpdatedCompany");
        Profile updatedProfile = profileRepository.save(savedProfile);

        assertThat(updatedProfile.getCompany()).isEqualTo("UpdatedCompany");
    }

    @Test
    @Rollback
    public void testDeleteProfile() {
        Profile profile = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        profile.setUser(user);
        Profile savedProfile = profileRepository.save(profile);

        profileRepository.deleteById(savedProfile.getId());

        Optional<Profile> deletedProfile = profileRepository.findById(savedProfile.getId());

        assertThat(deletedProfile).isNotPresent();
    }

    @Test
    @Rollback
    public void testFindAllProfiles() {
        Profile profile1 = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        profile1.setUser(user);
        Profile profile2 = new Profile("Company2", "Location2", "Status2", Arrays.asList("Python", "Django"));
        profile2.setUser(user);

        profileRepository.saveAll(Arrays.asList(profile1, profile2));

        List<Profile> profiles = profileRepository.findAll();

        assertThat(profiles.size()).isGreaterThanOrEqualTo(2);
    }
}