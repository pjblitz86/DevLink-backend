package ca.javau11.services;

import ca.javau11.entities.Company;
import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.repositories.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

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
        user.setJobs(new ArrayList<>());  // Ensure user's job list is initialized

        job = new Job();
        job.setId(jobId);
        job.setTitle("Software Engineer");
        job.setUser(user);
        job.setLocation("New York");
        job.setType("Full-Time");
        job.setSalary("$80,000");
        job.setDescription("This is a test job listing.");

        // Add job to user's job list
        user.getJobs().add(job);
    }

    // ✅ Test retrieving all jobs with and without a limit
    @Test
    void testGetAllJobs() {
        List<Job> jobs = List.of(job);
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> retrievedJobs = jobService.getAllJobs(null);
        assertEquals(1, retrievedJobs.size(), "Should return all jobs");

        List<Job> limitedJobs = jobService.getAllJobs(1);
        assertEquals(1, limitedJobs.size(), "Should return the requested number of jobs");

        verify(jobRepository, times(2)).findAll();
    }

    // ✅ Test retrieving a job by ID
    @Test
    void testGetJobById_Found() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        Optional<Job> retrievedJob = jobService.getJobById(jobId);
        assertTrue(retrievedJob.isPresent(), "Job should be found");
        assertEquals(jobId, retrievedJob.get().getId(), "Job ID should match");

        verify(jobRepository, times(1)).findById(jobId);
    }

    @Test
    void testGetJobById_NotFound() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        Optional<Job> retrievedJob = jobService.getJobById(jobId);
        assertFalse(retrievedJob.isPresent(), "Job should not be found");

        verify(jobRepository, times(1)).findById(jobId);
    }

    // ✅ Test creating a new job
    @Test
    void testCreateJob_Success() {
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Job createdJob = jobService.createJob(job, user);
        assertNotNull(createdJob, "Created job should not be null");
        assertEquals(user, createdJob.getUser(), "Job should be linked to the correct user");

        verify(jobRepository, times(1)).save(any(Job.class));
    }

    // ✅ Test updating an existing job
    @Test
    void testUpdateJob_Success() {
    	Company updatedCompany = new Company();
    	updatedCompany.setName("Updated Company"); 
        Job updatedJob = new Job();
        updatedJob.setTitle("Updated Title");
        updatedJob.setType("Part-Time");
        updatedJob.setDescription("Updated Description");
        updatedJob.setLocation("Remote");
        updatedJob.setSalary("$90,000");
        updatedJob.setCompany(updatedCompany);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(updatedJob);

        Optional<Job> result = jobService.updateJob(jobId, updatedJob, user);

        assertTrue(result.isPresent(), "Updated job should be returned");
        assertEquals("Updated Title", result.get().getTitle(), "Job title should be updated");

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testUpdateJob_UnauthorizedUser() {
        User anotherUser = new User();
        anotherUser.setId(200L);

        Job updatedJob = new Job();
        updatedJob.setTitle("Unauthorized Update");

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        Optional<Job> result = jobService.updateJob(jobId, updatedJob, anotherUser);

        assertTrue(result.isEmpty(), "Unauthorized user should not be able to update job");

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, never()).save(any(Job.class));
    }

    // ✅ Test deleting a job
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
