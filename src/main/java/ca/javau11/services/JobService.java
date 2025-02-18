package ca.javau11.services;

import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.repositories.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs(Integer limit) {
        List<Job> jobs = jobRepository.findAll();
        return (limit != null && limit > 0) ? jobs.subList(0, Math.min(limit, jobs.size())) : jobs;
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job createJob(Job job, User user) {
        job.setUser(user);
        return jobRepository.save(job);
    }

    public Optional<Job> updateJob(Long id, Job updatedJob, User user) {
        return jobRepository.findById(id).map(existingJob -> {
            if (!existingJob.getUser().getId().equals(user.getId()))
                return null;

            existingJob.setTitle(updatedJob.getTitle());
            existingJob.setType(updatedJob.getType());
            existingJob.setDescription(updatedJob.getDescription());
            existingJob.setLocation(updatedJob.getLocation());
            existingJob.setSalary(updatedJob.getSalary());
            existingJob.setCompany(updatedJob.getCompany());

            return jobRepository.save(existingJob);
        });
    }

    public boolean deleteJob(Long id, User user) {
        Optional<Job> jobOptional = jobRepository.findById(id);

        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();

            if (job.getUser().getId().equals(user.getId())) {
                user.getJobs().remove(job);
                jobRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

}
