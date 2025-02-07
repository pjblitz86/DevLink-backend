package ca.javau11.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ca.javau11.dtos.ProfileDTO;
import ca.javau11.entities.Profile;
import ca.javau11.entities.User;
import ca.javau11.services.ProfileService;
import ca.javau11.utils.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private Profile mockProfile;
    private ProfileDTO mockProfileDTO;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("testuser");

        mockProfile = new Profile();
        mockProfile.setId(1L);
        mockProfile.setUser(mockUser);
        mockProfile.setBio("Test Bio");

        mockProfileDTO = new ProfileDTO();
        mockProfileDTO.setBio("Updated Bio");
    }

    @Test
    void getProfiles_Success() {
        Profile profileWithoutUser = new Profile();
        profileWithoutUser.setId(2L);
        profileWithoutUser.setUser(null);

        List<Profile> profiles = Arrays.asList(mockProfile, profileWithoutUser);

        when(profileService.getProfiles()).thenReturn(profiles);

        ResponseEntity<?> response = profileController.getProfiles();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Profile> result = (List<Profile>) response.getBody();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockProfile.getId(), result.get(0).getId());
    }

    @Test
    void getProfileById_Success() {
        when(profileService.getProfileById(1L)).thenReturn(Optional.of(mockProfile));

        ResponseEntity<?> response = profileController.getProfileById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Response<?> body = (Response<?>) response.getBody();
        assertNotNull(body);
        assertEquals("Profile found", body.getMessage());
        assertEquals(mockProfile, body.getData());
    }

    @Test
    void getProfileById_NotFound() {
        when(profileService.getProfileById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileController.getProfileById(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Response<?> body = (Response<?>) response.getBody();
        assertNotNull(body);
        assertEquals("No profile found with the given ID", body.getMessage());
        assertNull(body.getData());
    }

    @Test
    void getProfileByUserId_Success() {
        when(profileService.getProfileByUserId(1L)).thenReturn(Optional.of(mockProfile));

        ResponseEntity<?> response = profileController.getProfileByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Response<?> body = (Response<?>) response.getBody();
        assertNotNull(body);
        assertEquals("Profile found", body.getMessage());
        assertEquals(mockProfile, body.getData());
    }

    @Test
    void getProfileByUserId_NotFound() {
        when(profileService.getProfileByUserId(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = profileController.getProfileByUserId(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Response<?> body = (Response<?>) response.getBody();
        assertNotNull(body);
        assertEquals("No profile exists for this user", body.getMessage());
        assertNull(body.getData());
    }

    @Test
    void createProfile_Success() {
        when(profileService.addProfile(eq(1L), any(ProfileDTO.class))).thenReturn(mockProfile);

        ResponseEntity<?> response = profileController.createProfile(1L, mockProfileDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Response<?> body = (Response<?>) response.getBody();
        assertNotNull(body);
        assertEquals("Profile successfully created", body.getMessage());
        assertEquals(mockProfile, body.getData());
    }

    @Test
    void updateProfile_Success() {
        when(profileService.updateProfile(eq(1L), any(ProfileDTO.class))).thenReturn(mockProfile);

        ResponseEntity<?> response = profileController.updateProfile(1L, mockProfileDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Response<?> body = (Response<?>) response.getBody();
        assertNotNull(body);
        assertEquals("Profile successfully updated", body.getMessage());
        assertEquals(mockProfile, body.getData());
    }

    @Test
    void deleteProfile_Success() {
        when(profileService.deleteProfile(1L)).thenReturn(true);

        ResponseEntity<Void> response = profileController.deleteProfile(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteProfile_NotFound() {
        when(profileService.deleteProfile(99L)).thenReturn(false);

        ResponseEntity<Void> response = profileController.deleteProfile(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
