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

import java.util.ArrayList;
import java.util.List;
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
    }

    @Test
    void testGetAllJobs() {
        List<Job> jobs = new ArrayList<>();
        jobs.add(job);
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.getAllJobs(null);
        assertEquals(1, result.size());
        assertEquals("Software Engineer", result.get(0).getTitle());

        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void testGetJobById_Found() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        Optional<Job> result = jobService.getJobById(jobId);
        assertTrue(result.isPresent());
        assertEquals(jobId, result.get().getId());

        verify(jobRepository, times(1)).findById(jobId);
    }

    @Test
    void testGetJobById_NotFound() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        Optional<Job> result = jobService.getJobById(jobId);
        assertFalse(result.isPresent());

        verify(jobRepository, times(1)).findById(jobId);
    }

    @Test
    void testCreateJob() {
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Job createdJob = jobService.createJob(job, user);
        assertNotNull(createdJob);
        assertEquals("Software Engineer", createdJob.getTitle());
        assertEquals(userId, createdJob.getUser().getId());

        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testUpdateJob_Success() {
        Job updatedJob = new Job();
        updatedJob.setTitle("Updated Job Title");
        updatedJob.setLocation("San Francisco");
        updatedJob.setUser(user);
        
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(updatedJob);

        Optional<Job> result = jobService.updateJob(jobId, updatedJob, user);

        assertTrue(result.isPresent());
        assertEquals("Updated Job Title", result.get().getTitle());
        assertEquals("San Francisco", result.get().getLocation());

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testUpdateJob_UnauthorizedUser() {
        User anotherUser = new User();
        anotherUser.setId(200L);

        Job updatedJob = new Job();
        updatedJob.setTitle("Updated Job Title");
        updatedJob.setUser(anotherUser);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        Optional<Job> result = jobService.updateJob(jobId, updatedJob, anotherUser);

        assertFalse(result.isPresent());
        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void testDeleteJob_Success() {
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        doNothing().when(jobRepository).deleteById(jobId);

        boolean isDeleted = jobService.deleteJob(jobId, user);
        assertTrue(isDeleted);

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, times(1)).deleteById(jobId);
    }

    @Test
    void testDeleteJob_UnauthorizedUser() {
        User anotherUser = new User();
        anotherUser.setId(200L);

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        boolean isDeleted = jobService.deleteJob(jobId, anotherUser);
        assertFalse(isDeleted);

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, never()).deleteById(jobId);
    }
}
