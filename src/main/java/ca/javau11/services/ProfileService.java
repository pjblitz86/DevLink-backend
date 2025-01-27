package ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.dtos.ProfileDTO;
import ca.javau11.entities.Profile;
import ca.javau11.entities.User;
import ca.javau11.exceptions.ProfileAlreadyExistsException;
import ca.javau11.exceptions.UserNotFoundException;
import ca.javau11.repositories.ProfileRepository;
import ca.javau11.repositories.UserRepository;
import jakarta.validation.ValidationException;

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
	    return profileRepo.findByUserId(userId);
	}
	
	public Profile addProfile(Long userId, ProfileDTO profileDTO) {
        // Check required fields
        validateProfileDTO(profileDTO);

        Optional<Profile> existingProfile = profileRepo.findByUserId(userId);
        if (existingProfile.isPresent()) {
            throw new ProfileAlreadyExistsException("Profile already exists for this user");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Profile profile = mapDtoToProfile(profileDTO);
        profile.setUser(user);

        return profileRepo.save(profile);
    }
	
	public Profile updateProfile(Long userId, ProfileDTO profileDTO) {
        validateProfileDTO(profileDTO);
        Profile existingProfile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));

        updateProfileFromDto(existingProfile, profileDTO);

        return profileRepo.save(existingProfile);
    }
	
	public boolean deleteProfile(Long profileId) {
		Optional<Profile> box = profileRepo.findById(profileId);
	    if (box.isEmpty()) return false;
	    
	    Profile profile = box.get();
	    profile.setUser(null);
	    
	    profileRepo.delete(profile);
	    return true;
	}
	
	private void validateProfileDTO(ProfileDTO profileDTO) {
        if (profileDTO.getStatus() == null || profileDTO.getStatus().isEmpty()) {
            throw new ValidationException("Status is required");
        }
        if (profileDTO.getSkills() == null || profileDTO.getSkills().isEmpty()) {
            throw new ValidationException("Skills are required and must be comma-separated values");
        }
    }
	
	private Profile mapDtoToProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setCompany(profileDTO.getCompany());
        profile.setWebsite(profileDTO.getWebsite());
        profile.setLocation(profileDTO.getLocation());
        profile.setStatus(profileDTO.getStatus());
        profile.setSkills(profileDTO.getSkillsAsList()); // Convert skills to List<String>
        profile.setBio(profileDTO.getBio());
        profile.setGithubUserName(profileDTO.getGithubUserName());
        profile.setYoutube(profileDTO.getYoutube());
        profile.setTwitter(profileDTO.getTwitter());
        profile.setFacebook(profileDTO.getFacebook());
        profile.setLinkedin(profileDTO.getLinkedin());
        profile.setInstagram(profileDTO.getInstagram());
        return profile;
    }

    private void updateProfileFromDto(Profile existingProfile, ProfileDTO profileDTO) {
        existingProfile.setCompany(profileDTO.getCompany());
        existingProfile.setWebsite(profileDTO.getWebsite());
        existingProfile.setLocation(profileDTO.getLocation());
        existingProfile.setStatus(profileDTO.getStatus());
        existingProfile.setSkills(profileDTO.getSkillsAsList());
        existingProfile.setBio(profileDTO.getBio());
        existingProfile.setGithubUserName(profileDTO.getGithubUserName());
        existingProfile.setYoutube(profileDTO.getYoutube());
        existingProfile.setTwitter(profileDTO.getTwitter());
        existingProfile.setFacebook(profileDTO.getFacebook());
        existingProfile.setLinkedin(profileDTO.getLinkedin());
        existingProfile.setInstagram(profileDTO.getInstagram());
    }
	
	
}