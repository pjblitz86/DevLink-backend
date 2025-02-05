package ca.javau11.controllers;

import ca.javau11.entities.Education;
import ca.javau11.services.EducationService;
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
class EducationControllerTest {

    @Mock
    private EducationService educationService;

    @InjectMocks
    private EducationController educationController;

    private Education education;
    private Long profileId = 1L;
    private Long educationId = 100L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        education = new Education();
        education.setId(educationId);
        education.setSchool("Harvard University");
        education.setDegree("Bachelor of Science");
    }

    @Test
    void testGetEducationsByProfile() {
        List<Education> educations = Arrays.asList(education);
        when(educationService.getEducationsByProfileId(profileId)).thenReturn(educations);

        List<Education> response = educationController.getEducationsByProfile(profileId);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Harvard University", response.get(0).getSchool());
        verify(educationService, times(1)).getEducationsByProfileId(profileId);
    }

    @Test
    void testGetEducationById_Found() {
        when(educationService.getEducationById(educationId)).thenReturn(Optional.of(education));

        ResponseEntity<Education> response = educationController.getEducation(educationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Harvard University", response.getBody().getSchool());
        verify(educationService, times(1)).getEducationById(educationId);
    }

    @Test
    void testGetEducationById_NotFound() {
        when(educationService.getEducationById(educationId)).thenReturn(Optional.empty());

        ResponseEntity<Education> response = educationController.getEducation(educationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(educationService, times(1)).getEducationById(educationId);
    }

    @Test
    void testAddEducation_Success() {
        when(educationService.addEducationToProfile(profileId, education)).thenReturn(Optional.of(education));

        ResponseEntity<Education> response = educationController.addEducation(profileId, education);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // ✅ Updated
        assertEquals("Harvard University", response.getBody().getSchool());
        verify(educationService, times(1)).addEducationToProfile(profileId, education);
    }

    @Test
    void testAddEducation_Failure() {
        when(educationService.addEducationToProfile(profileId, education)).thenReturn(Optional.empty());

        ResponseEntity<Education> response = educationController.addEducation(profileId, education);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(educationService, times(1)).addEducationToProfile(profileId, education);
    }

    @Test
    void testDeleteEducation_Success() {
        when(educationService.deleteEducation(educationId)).thenReturn(true);

        ResponseEntity<Void> response = educationController.deleteEducation(educationId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(educationService, times(1)).deleteEducation(educationId);
    }

    @Test
    void testDeleteEducation_NotFound() {
        when(educationService.deleteEducation(educationId)).thenReturn(false);

        ResponseEntity<Void> response = educationController.deleteEducation(educationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(educationService, times(1)).deleteEducation(educationId);
    }
}
