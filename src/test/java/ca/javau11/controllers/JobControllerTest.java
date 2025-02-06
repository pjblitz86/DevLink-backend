package ca.javau11.controllers;

import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.services.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private Job job;
    private User authenticatedUser;
    private Long jobId = 100L;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        authenticatedUser = new User();
        authenticatedUser.setId(userId);
        authenticatedUser.setName("Test User");

        job = new Job();
        job.setId(jobId);
        job.setTitle("Software Engineer");
        job.setUser(authenticatedUser);
    }

    @Test
    void testDeleteJob_Success() {
        when(jobService.deleteJob(jobId, authenticatedUser)).thenReturn(true);

        ResponseEntity<String> response = jobController.deleteJob(jobId, authenticatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job deleted successfully.", response.getBody());

        verify(jobService, times(1)).deleteJob(jobId, authenticatedUser);
    }

    @Test
    void testDeleteJob_UnauthenticatedUser() {
        ResponseEntity<String> response = jobController.deleteJob(jobId, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized: No authenticated user.", response.getBody());

        verify(jobService, never()).deleteJob(anyLong(), any());
    }

    @Test
    void testDeleteJob_NotAuthorized() {
        when(jobService.deleteJob(jobId, authenticatedUser)).thenReturn(false);

        ResponseEntity<String> response = jobController.deleteJob(jobId, authenticatedUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized to delete this job.", response.getBody());

        verify(jobService, times(1)).deleteJob(jobId, authenticatedUser);
    }
}
