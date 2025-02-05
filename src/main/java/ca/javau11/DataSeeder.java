package ca.javau11;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.javau11.entities.Job;
import ca.javau11.entities.Company;
import ca.javau11.entities.User;
import ca.javau11.repositories.JobRepository;
import ca.javau11.repositories.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final JobRepository jobRepo;
    private final UserRepository userRepo;

    public DataSeeder(JobRepository jobRepo, UserRepository userRepo) {
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Optional<User> existingUser = userRepo.findByEmail("jobowner@example.com");

        User user;
        if (existingUser.isEmpty()) {
            logger.info("⚠️ User not found, creating new user...");
            user = new User();
            user.setName("New Job Owner");
            user.setEmail("jobowner@example.com");
            user.setPassword("securepassword"); // ⚠️ Encrypt in real-world applications

            user = userRepo.save(user);
            logger.info("✅ User created with ID: {}", user.getId());
        } else {
            user = existingUser.get();
            logger.info("✅ User already exists with ID: {}", user.getId());
        }

        if (jobRepo.count() == 0) {
            List<Job> jobs = List.of(
                new Job("Senior React Developer", "Full-Time", 
                        "Join our team in Boston, MA. We need a strong React developer.", 
                        "Boston, MA", "$70K - $80K",
                        new Company("NewTek Solutions", "Tech company specializing in web solutions.", "contact@teksolutions.com", "555-555-5555"),
                        user),

                new Job("React.js Developer", "Full-Time", 
                        "We need a front-end React expert in Brooklyn, NY.", 
                        "Brooklyn, NY", "$75K - $85K",
                        new Company("Dolor Cloud", "Cloud-based tech company.", "contact@dolorsitamet.com", "555-555-5555"),
                        user),

                new Job("Full Stack Developer", "Part-Time", 
                        "Work remotely on full-stack applications.", 
                        "Remote", "$80K - $90K",
                        new Company("TechCorp", "Innovative software company.", "contact@techcorp.com", "555-555-5555"),
                        user)
            );

            jobRepo.saveAll(jobs);
            logger.info("✅ Jobs have been successfully assigned to user: {}", user.getEmail());
        } else {
            logger.info("✅ Jobs already exist, skipping job insertion.");
        }
    }
}
