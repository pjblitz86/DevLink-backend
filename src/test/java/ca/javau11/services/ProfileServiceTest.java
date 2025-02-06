package ca.javau11.services;

import ca.javau11.dtos.ProfileDTO;
import ca.javau11.entities.Profile;
import ca.javau11.entities.User;
import ca.javau11.exceptions.ProfileAlreadyExistsException;
import ca.javau11.exceptions.UserNotFoundException;
import ca.javau11.repositories.ProfileRepository;
import ca.javau11.repositories.UserRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private ProfileRepository profileRepo;

    @InjectMocks
    private ProfileService profileService;

    private Profile profile;
    private User user;
    private ProfileDTO profileDTO;
    private final Long userId = 1L;
    private final Long profileId = 100L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        profile = new Profile();
        profile.setId(profileId);
        profile.setUser(user);
        profile.setStatus("Developer");
        profile.setSkills(List.of("Java", "Spring Boot"));

        profileDTO = new ProfileDTO();
        profileDTO.setStatus("Developer");
        profileDTO.setSkills("Java,Spring Boot");
    }

    @Test
    void testGetProfiles() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        List<Profile> profiles = profileService.getProfiles();

        assertFalse(profiles.isEmpty());
        assertEquals(1, profiles.size());
        verify(profileRepo, times(1)).findAll();
    }

    @Test
    void testGetProfileById_Success() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileService.getProfileById(profileId);

        assertTrue(foundProfile.isPresent());
        assertEquals(profileId, foundProfile.get().getId());
        verify(profileRepo, times(1)).findById(profileId);
    }

    @Test
    void testGetProfileByUserId_Success() {
        when(profileRepo.findByUserId(userId)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileService.getProfileByUserId(userId);

        assertTrue(foundProfile.isPresent());
        assertEquals(userId, foundProfile.get().getUser().getId());
        verify(profileRepo, times(1)).findByUserId(userId);
    }

    @Test
    void testAddProfile_Success() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(profileRepo.findByUserId(userId)).thenReturn(Optional.empty());
        when(profileRepo.save(any(Profile.class))).thenReturn(profile);

        Profile createdProfile = profileService.addProfile(userId, profileDTO);

        assertNotNull(createdProfile);
        assertEquals("Developer", createdProfile.getStatus());
        verify(profileRepo, times(1)).save(any(Profile.class));
    }

    @Test
    void testAddProfile_UserNotFound() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> profileService.addProfile(userId, profileDTO));
        verify(profileRepo, never()).save(any(Profile.class));
    }

    @Test
    void testAddProfile_AlreadyExists() {
        lenient().when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(profileRepo.findByUserId(userId)).thenReturn(Optional.of(profile));

        assertThrows(ProfileAlreadyExistsException.class, () -> profileService.addProfile(userId, profileDTO));
        verify(profileRepo, never()).save(any(Profile.class));
    }

    @Test
    void testUpdateProfile_Success() {
        when(profileRepo.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(profileRepo.save(any(Profile.class))).thenReturn(profile);

        Profile updatedProfile = profileService.updateProfile(userId, profileDTO);

        assertNotNull(updatedProfile);
        assertEquals("Developer", updatedProfile.getStatus());
        verify(profileRepo, times(1)).save(profile);
    }

    @Test
    void testUpdateProfile_ProfileNotFound() {
        when(profileRepo.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> profileService.updateProfile(userId, profileDTO));
        verify(profileRepo, never()).save(any(Profile.class));
    }

    @Test
    void testDeleteProfile_Success() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.of(profile));

        boolean result = profileService.deleteProfile(profileId);

        assertTrue(result);
        verify(profileRepo, times(1)).delete(profile);
    }

    @Test
    void testDeleteProfile_NotFound() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.empty());

        boolean result = profileService.deleteProfile(profileId);

        assertFalse(result);
        verify(profileRepo, never()).delete(any(Profile.class));
    }

    @Test
    void testValidateProfileDTO_ThrowsValidationException() {
        ProfileDTO invalidDto = new ProfileDTO();

        assertThrows(ValidationException.class, () -> profileService.addProfile(userId, invalidDto));
    }
}
