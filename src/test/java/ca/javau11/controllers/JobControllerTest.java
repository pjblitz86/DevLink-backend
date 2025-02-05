package ca.javau11.controllers;

import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.services.JobService;
import ca.javau11.services.UserService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private UserService userService;

    @InjectMocks
    private JobController jobController;

    private Job job;
    private User user;
    private Long jobId = 100L;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");

        job = new Job();
        job.setId(jobId);
        job.setTitle("Software Engineer");
        job.setUser(user);
    }

    @Test
    void testGetAllJobs() {
        List<Job> jobs = Arrays.asList(job);
        when(jobService.getAllJobs(null)).thenReturn(jobs);

        ResponseEntity<List<Job>> response = jobController.getAllJobs(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(jobService, times(1)).getAllJobs(null);
    }

    @Test
    void testGetJobById_Success() {
        when(jobService.getJobById(jobId)).thenReturn(Optional.of(job));

        ResponseEntity<Job> response = jobController.getJobById(jobId);

        assertNotNull(response.getBody());
        assertEquals(jobId, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetJobById_NotFound() {
        when(jobService.getJobById(jobId)).thenReturn(Optional.empty());

        ResponseEntity<Job> response = jobController.getJobById(jobId);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateJob_Success() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(jobService.createJob(job, user)).thenReturn(job);

        ResponseEntity<?> response = jobController.createJob(job, userId);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateJob_UserNotFound() {
        when(userService.getUserById(userId)).thenReturn(null);

        ResponseEntity<?> response = jobController.createJob(job, userId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User not found or unauthorized.", response.getBody());
    }

    @Test
    void testUpdateJob_Success() {
        when(jobService.updateJob(jobId, job, user)).thenReturn(Optional.of(job));

        ResponseEntity<Job> response = jobController.updateJob(jobId, job, user);

        assertNotNull(response.getBody());
        assertEquals(jobId, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateJob_NotFound() {
        when(jobService.updateJob(jobId, job, user)).thenReturn(Optional.empty());

        ResponseEntity<Job> response = jobController.updateJob(jobId, job, user);

        assertNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testUpdateJob_Unauthorized() {
        ResponseEntity<Job> response = jobController.updateJob(jobId, job, null);

        assertNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testDeleteJob_Success() {
        when(jobService.deleteJob(jobId, user)).thenReturn(true);

        ResponseEntity<String> response = jobController.deleteJob(jobId, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job deleted successfully.", response.getBody());
    }

    @Test
    void testDeleteJob_Unauthorized() {
        ResponseEntity<String> response = jobController.deleteJob(jobId, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    void testDeleteJob_NotAuthorized() {
        when(jobService.deleteJob(jobId, user)).thenReturn(false);

        ResponseEntity<String> response = jobController.deleteJob(jobId, user);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized to delete this job.", response.getBody());
    }
}
