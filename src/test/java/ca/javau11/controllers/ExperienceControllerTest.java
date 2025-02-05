package ca.javau11.controllers;

import ca.javau11.entities.Experience;
import ca.javau11.services.ExperienceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceControllerTest {

    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private ExperienceController experienceController;

    private Experience experience;
    private Long profileId = 1L;
    private Long experienceId = 100L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        experience = new Experience();
        experience.setId(experienceId);
        experience.setTitle("Software Engineer");
    }

    @Test
    void testGetExperiencesByProfile() {
        List<Experience> experiences = Arrays.asList(experience);
        when(experienceService.getExperiencesByProfileId(profileId)).thenReturn(experiences);

        List<Experience> response = experienceController.getExperiencesByProfile(profileId);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Software Engineer", response.get(0).getTitle());
        verify(experienceService, times(1)).getExperiencesByProfileId(profileId);
    }

    @Test
    void testGetExperienceById_Found() {
        when(experienceService.getExperienceById(experienceId)).thenReturn(Optional.of(experience));

        ResponseEntity<Experience> response = experienceController.getExperience(experienceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Software Engineer", response.getBody().getTitle());
        verify(experienceService, times(1)).getExperienceById(experienceId);
    }

    @Test
    void testGetExperienceById_NotFound() {
        when(experienceService.getExperienceById(experienceId)).thenReturn(Optional.empty());

        ResponseEntity<Experience> response = experienceController.getExperience(experienceId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(experienceService, times(1)).getExperienceById(experienceId);
    }

    @Test
    void testAddExperience_Success() {
        when(experienceService.addExperienceToProfile(profileId, experience)).thenReturn(Optional.of(experience));

        ResponseEntity<Experience> response = experienceController.addExperience(profileId, experience);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Software Engineer", response.getBody().getTitle());
        verify(experienceService, times(1)).addExperienceToProfile(profileId, experience);
    }

    @Test
    void testAddExperience_Failure() {
        when(experienceService.addExperienceToProfile(profileId, experience)).thenReturn(Optional.empty());

        ResponseEntity<Experience> response = experienceController.addExperience(profileId, experience);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(experienceService, times(1)).addExperienceToProfile(profileId, experience);
    }

    @Test
    void testDeleteExperience_Success() {
        when(experienceService.deleteExperience(experienceId)).thenReturn(true);

        ResponseEntity<Void> response = experienceController.deleteExperience(experienceId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(experienceService, times(1)).deleteExperience(experienceId);
    }

    @Test
    void testDeleteExperience_NotFound() {
        when(experienceService.deleteExperience(experienceId)).thenReturn(false);

        ResponseEntity<Void> response = experienceController.deleteExperience(experienceId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(experienceService, times(1)).deleteExperience(experienceId);
    }
}
