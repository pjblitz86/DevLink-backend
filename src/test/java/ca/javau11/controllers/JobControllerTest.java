package ca.javau11.controllers;

import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.service.CustomUserDetails;
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
    private CustomUserDetails authenticatedUser;
    private Long jobId = 100L;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");

        authenticatedUser = new CustomUserDetails(userId, "test@example.com", "password123");

        job = new Job();
        job.setId(jobId);
        job.setTitle("Software Engineer");
        job.setUser(user);
    }

    @Test
    void testDeleteJob_Success() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(jobService.deleteJob(jobId, user)).thenReturn(true);

        ResponseEntity<String> response = jobController.deleteJob(jobId, authenticatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job deleted successfully.", response.getBody());

        verify(jobService, times(1)).deleteJob(jobId, user);
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
        when(userService.getUserById(userId)).thenReturn(user);
        when(jobService.deleteJob(jobId, user)).thenReturn(false);

        ResponseEntity<String> response = jobController.deleteJob(jobId, authenticatedUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized to delete this job.", response.getBody());

        verify(jobService, times(1)).deleteJob(jobId, user);
    }

    @Test
    void testUpdateJob_Success() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(jobService.updateJob(eq(jobId), any(Job.class), eq(user))).thenReturn(Optional.of(job));

        ResponseEntity<Job> response = jobController.updateJob(jobId, job, authenticatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(job, response.getBody());

        verify(jobService, times(1)).updateJob(eq(jobId), any(Job.class), eq(user));
    }

    @Test
    void testUpdateJob_UnauthenticatedUser() {
        ResponseEntity<Job> response = jobController.updateJob(jobId, job, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());

        verify(jobService, never()).updateJob(anyLong(), any(), any());
    }

    @Test
    void testUpdateJob_NotAuthorized() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(jobService.updateJob(eq(jobId), any(Job.class), eq(user))).thenReturn(Optional.empty());

        ResponseEntity<Job> response = jobController.updateJob(jobId, job, authenticatedUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());

        verify(jobService, times(1)).updateJob(eq(jobId), any(Job.class), eq(user));
    }
}
