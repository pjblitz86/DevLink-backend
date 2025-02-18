package ca.javau11.controllers;

import ca.javau11.entities.Job;
import ca.javau11.entities.User;
import ca.javau11.service.CustomUserDetails;
import ca.javau11.services.JobService;
import ca.javau11.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    private final JobService jobService;
    private final UserService userService;

    public JobController(JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
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

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createJob(@RequestBody Job job, @PathVariable Long userId) {
    	Optional<User> user = Optional.ofNullable(userService.getUserById(userId));
        if (user.isEmpty())
            return ResponseEntity.status(403).body("User not found or unauthorized.");
        
        Job createdJob = jobService.createJob(job, user.get());
        return ResponseEntity.ok(createdJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job, @AuthenticationPrincipal CustomUserDetails authenticatedUser) {
        logger.debug("Updating job with ID: {} for authenticated user: {}", id, authenticatedUser != null ? authenticatedUser.getId() : "null");

        if (authenticatedUser == null)
            return ResponseEntity.status(403).body(null);

        Optional<Job> updatedJob = jobService.updateJob(id, job, userService.getUserById(authenticatedUser.getId()));
        return updatedJob.map(ResponseEntity::ok).orElse(ResponseEntity.status(403).build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails authenticatedUser) {
        logger.debug("Deleting job with ID: {} for authenticated user: {}", id, authenticatedUser != null ? authenticatedUser.getId() : "null");

        if (authenticatedUser == null)
            return ResponseEntity.status(403).body("Unauthorized: No authenticated user.");
        

        boolean deleted = jobService.deleteJob(id, userService.getUserById(authenticatedUser.getId()));
        return deleted 
        	    ? ResponseEntity.ok("Job deleted successfully.") 
        	    : ResponseEntity.status(403).body("You are not authorized to delete this job.");
    }

}
