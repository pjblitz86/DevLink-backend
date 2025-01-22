package ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.entities.Profile;
import ca.javau11.entities.User;
import ca.javau11.repositories.ProfileRepository;
import ca.javau11.repositories.UserRepository;

@Service
public class ProfileService {

	private UserRepository userRepo;
	private ProfileRepository profileRepo;
	
	public ProfileService(UserRepository userRepo, ProfileRepository profileRepo) {
		this.userRepo = userRepo;
		this.profileRepo = profileRepo;
	}
	
	public List<Profile> getProfiles() {
		return profileRepo.findAll();
	}
	
	public Optional<Profile> getProfileByUserId(Long userId) {
        return userRepo.findById(userId)
            .flatMap(user -> profileRepo.findByUser(user));
    }
	
	public Profile addProfile(Long userId, Profile profile) {
		
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // TODO: add exception handling in separate files
        
        profile.setUser(user);
        return profileRepo.save(profile);
	}
	
	public Optional<Profile> updateProfile(Long id, Profile profile) {
		Optional<Profile> box = profileRepo.findById(id);
		if(box.isPresent()) {
			Profile existingProfile = box.get(); // TODO: backend validation? and not sure about this method
			existingProfile.setCompany(profile.getCompany());
			existingProfile.setWebsite(profile.getWebsite());
			existingProfile.setLocation(profile.getLocation());
			existingProfile.setStatus(profile.getStatus());
			existingProfile.setSkills(profile.getSkills());
			existingProfile.setBio(profile.getBio());
			existingProfile.setGithubUserName(profile.getGithubUserName());
			return Optional.of(profileRepo.save(existingProfile));
		}
		
		return Optional.empty();
	}
	
	public boolean deleteProfile(Long id) {
		Optional<Profile> box = profileRepo.findById(id);
	    if (box.isEmpty()) return false;
	    
	    Profile profile = box.get();
	    profile.setUser(null);
	    
	    profileRepo.delete(profile);
	    return true;
	}
	
}