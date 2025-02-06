package ca.javau11.services;

import ca.javau11.entities.Education;
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
class EducationServiceTest {

    @Mock
    private ProfileRepository profileRepo;

    @InjectMocks
    private EducationService educationService;

    private Profile profile;
    private Education education;

    private final Long profileId = 1L;
    private final Long educationId = 100L;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setId(profileId);
        profile.setEducations(new ArrayList<>());

        education = new Education();
        education.setId(educationId);
        education.setSchool("Harvard University");
        education.setDegree("Bachelor's in Computer Science");
        education.setProfile(profile);

        profile.getEducations().add(education);
    }

    @Test
    void testGetEducationsByProfileId_Success() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.of(profile));

        List<Education> educations = educationService.getEducationsByProfileId(profileId);

        assertNotNull(educations);
        assertEquals(1, educations.size());
        assertEquals("Harvard University", educations.get(0).getSchool());

        verify(profileRepo, times(1)).findById(profileId);
    }

    @Test
    void testGetEducationsByProfileId_ProfileNotFound() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> educationService.getEducationsByProfileId(profileId));

        assertEquals("Education not found for ID: " + profileId, exception.getMessage());
        verify(profileRepo, times(1)).findById(profileId);
    }

    @Test
    void testGetEducationById_Success() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        Optional<Education> foundEducation = educationService.getEducationById(educationId);

        assertTrue(foundEducation.isPresent());
        assertEquals(educationId, foundEducation.get().getId());
        assertEquals("Harvard University", foundEducation.get().getSchool());

        verify(profileRepo, times(1)).findAll();
    }

    @Test
    void testGetEducationById_NotFound() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        Optional<Education> foundEducation = educationService.getEducationById(999L);

        assertFalse(foundEducation.isPresent());
        verify(profileRepo, times(1)).findAll();
    }

    @Test
    void testAddEducationToProfile_Success() {
        Education newEducation = new Education();
        newEducation.setId(101L);
        newEducation.setSchool("MIT");
        newEducation.setDegree("Master's in AI");

        when(profileRepo.findById(profileId)).thenReturn(Optional.of(profile));
        when(profileRepo.save(any(Profile.class))).thenReturn(profile);

        Optional<Education> addedEducation = educationService.addEducationToProfile(profileId, newEducation);

        assertTrue(addedEducation.isPresent());
        assertEquals("MIT", addedEducation.get().getSchool());
        assertEquals(profile, addedEducation.get().getProfile());
        assertEquals(2, profile.getEducations().size());

        verify(profileRepo, times(1)).findById(profileId);
        verify(profileRepo, times(1)).save(profile);
    }

    @Test
    void testAddEducationToProfile_ProfileNotFound() {
        when(profileRepo.findById(profileId)).thenReturn(Optional.empty());

        Optional<Education> addedEducation = educationService.addEducationToProfile(profileId, education);

        assertFalse(addedEducation.isPresent());
        verify(profileRepo, times(1)).findById(profileId);
        verify(profileRepo, never()).save(any(Profile.class));
    }

    @Test
    void testDeleteEducation_Success() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        boolean deleted = educationService.deleteEducation(educationId);

        assertTrue(deleted);
        assertTrue(profile.getEducations().isEmpty());

        verify(profileRepo, times(1)).findAll();
        verify(profileRepo, times(1)).save(profile);
    }

    @Test
    void testDeleteEducation_NotFound() {
        when(profileRepo.findAll()).thenReturn(List.of(profile));

        boolean deleted = educationService.deleteEducation(999L);

        assertFalse(deleted);
        verify(profileRepo, times(1)).findAll();
        verify(profileRepo, never()).save(any(Profile.class));
    }
}
