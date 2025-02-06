package ca.javau11.services;

import ca.javau11.entities.Experience;
import ca.javau11.entities.Profile;
import ca.javau11.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceTest {

    @Mock
    private ProfileRepository profileRepo;

    @InjectMocks
    private ExperienceService experienceService;

    private Profile profile;
    private Experience experience;
    
    private final Long profileId = 1L;
    private final Long experienceId = 100L;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setId(profileId);
        profile.setExperiences(new ArrayList<>());

        experience = new Experience();
        experience.setId(experienceId);
        experience.setTitle("Software Engineer");
        experience.setCompany("Tech Corp");
        experience.setProfile(profile);

        profile.getExperiences().add(experience);
    }

    @Test
    void testGetExperiencesByProfileId_Success() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.of(profile));

        List<Experience> experiences = experienceService.getExperiencesByProfileId(profileId);

        assertNotNull(experiences);
        assertEquals(1, experiences.size());
        assertEquals("Software Engineer", experiences.get(0).getTitle());

        verify(profileRepo, times(1)).findById(profileId);
    }

    @Test
    void testGetExperiencesByProfileId_ProfileNotFound() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> experienceService.getExperiencesByProfileId(profileId));

        assertEquals("Profile not found for ID: " + profileId, exception.getMessage());
        verify(profileRepo, times(1)).findById(profileId);
    }

    @Test
    void testGetExperienceById_Success() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        Optional<Experience> foundExperience = experienceService.getExperienceById(experienceId);

        assertTrue(foundExperience.isPresent());
        assertEquals(experienceId, foundExperience.get().getId());
        assertEquals("Software Engineer", foundExperience.get().getTitle());

        verify(profileRepo, times(1)).findAll();
    }

    @Test
    void testGetExperienceById_NotFound() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        Optional<Experience> foundExperience = experienceService.getExperienceById(999L);

        assertFalse(foundExperience.isPresent());
        verify(profileRepo, times(1)).findAll();
    }

    @Test
    void testAddExperienceToProfile_Success() {
        Experience newExperience = new Experience();
        newExperience.setId(101L);
        newExperience.setTitle("Data Analyst");

        when(profileRepo.findById(profileId)).thenReturn(Optional.of(profile));
        when(profileRepo.save(any(Profile.class))).thenReturn(profile);

        Optional<Experience> addedExperience = experienceService.addExperienceToProfile(profileId, newExperience);

        assertTrue(addedExperience.isPresent());
        assertEquals("Data Analyst", addedExperience.get().getTitle());
        assertEquals(profile, addedExperience.get().getProfile());
        assertEquals(2, profile.getExperiences().size());

        verify(profileRepo, times(1)).findById(profileId);
        verify(profileRepo, times(1)).save(profile);
    }

    @Test
    void testAddExperienceToProfile_ProfileNotFound() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.empty());

        Optional<Experience> addedExperience = experienceService.addExperienceToProfile(profileId, experience);

        assertFalse(addedExperience.isPresent());
        verify(profileRepo, times(1)).findById(profileId);
        verify(profileRepo, never()).save(any(Profile.class));
    }

    @Test
    void testDeleteExperience_Success() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        boolean deleted = experienceService.deleteExperience(experienceId);

        assertTrue(deleted);
        assertTrue(profile.getExperiences().isEmpty());

        verify(profileRepo, times(1)).findAll();
        verify(profileRepo, times(1)).save(profile);
    }

    @Test
    void testDeleteExperience_NotFound() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        boolean deleted = experienceService.deleteExperience(999L);

        assertFalse(deleted);
        verify(profileRepo, times(1)).findAll();
        verify(profileRepo, never()).save(any(Profile.class));
    }
}
