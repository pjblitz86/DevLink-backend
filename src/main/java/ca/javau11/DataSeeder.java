package ca.javau11;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ca.javau11.repositories.ProfileRepository;

@Component
public class DataSeeder implements CommandLineRunner {

	private ProfileRepository profileRepo;
	
	public DataSeeder(ProfileRepository profileRepo) {
		this.profileRepo = profileRepo;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if (profileRepo.count() == 0) {
			
			
			
			profileRepo.saveAll(List.of());
			
		}
		
	}
	
}