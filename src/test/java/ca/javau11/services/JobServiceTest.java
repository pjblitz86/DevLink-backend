package ca.javau11.services;

import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.repositories.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job job;
    private User user;
    private final Long jobId = 1L;
    private final Long userId = 100L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");

        job = new Job();
        job.setId(jobId);
        job.setTitle("Software Engineer");
        job.setUser(user);
        job.setLocation("New York");
        job.setType("Full-Time");
        job.setSalary("$80,000");
        job.setDescription("This is a test job listing.");
        
        // Add job to user's job list (assuming user has a List<Job> jobs field)
        user.getJobs().add(job);
    }

    @Test
    void testDeleteJob_Success() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        doNothing().when(jobRepository).deleteById(jobId);

        boolean isDeleted = jobService.deleteJob(jobId, user);
        assertTrue(isDeleted, "Job should be deleted successfully");

        // Ensure job was removed from user's jobs list
        assertFalse(user.getJobs().contains(job), "Job should be removed from user’s job list");

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, times(1)).deleteById(jobId);
    }

    @Test
    void testDeleteJob_JobNotFound() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        boolean isDeleted = jobService.deleteJob(jobId, user);
        assertFalse(isDeleted, "Job deletion should fail as job doesn't exist");

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteJob_UnauthorizedUser() {
        User anotherUser = new User();
        anotherUser.setId(200L);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        boolean isDeleted = jobService.deleteJob(jobId, anotherUser);
        assertFalse(isDeleted, "Job deletion should fail as user is unauthorized");

        // Ensure job was not removed from user's job list
        assertTrue(user.getJobs().contains(job), "Job should still exist in the owner's job list");

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, never()).deleteById(anyLong());
    }
}
