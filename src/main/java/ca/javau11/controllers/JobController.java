package ca.javau11.controllers;

import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.services.JobService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs(@RequestParam(required = false) Integer limit) {
        List<Job> jobs = jobService.getAllJobs(limit);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(403).body(null);
        }
        
        Job createdJob = jobService.createJob(job, user);
        return ResponseEntity.ok(createdJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(403).body(null);
        }

        Optional<Job> updatedJob = jobService.updateJob(id, job, user);
        return updatedJob.map(ResponseEntity::ok).orElse(ResponseEntity.status(403).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        boolean deleted = jobService.deleteJob(id, user);
        if (deleted) {
            return ResponseEntity.ok("Job deleted successfully.");
        } else {
            return ResponseEntity.status(403).body("You are not authorized to delete this job.");
        }
    }
}
