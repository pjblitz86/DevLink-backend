package ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.entities.Experience;
import ca.javau11.entities.Profile;
import ca.javau11.repositories.ProfileRepository;

@Service
public class ExperienceService {
	
	private ProfileRepository profileRepo;

	public ExperienceService(ProfileRepository profileRepo) {
		this.profileRepo = profileRepo;
	}
	
	public List<Experience> getExperiencesByProfileId(Long profileId) {
		return profileRepo.findById(profileId)
                .map(Profile::getExperiences)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for ID: " + profileId));
	}

	public Optional<Experience> getExperienceById(Long experienceId) {
		return profileRepo.findAll().stream()
                .flatMap(profile -> profile.getExperiences().stream())
                .filter(experience -> experience.getId().equals(experienceId))
                .findFirst();
	}
	
	public Optional<Experience> addExperienceToProfile(Long profileId, Experience experience) {
		return profileRepo.findById(profileId).map(profile -> {
			experience.setProfile(profile);
            profile.getExperiences().add(experience);
            profileRepo.save(profile);
            return experience;
        });
	}

	public boolean deleteExperience(Long id) {
	    for (Profile profile : profileRepo.findAll()) {
	        Optional<Experience> experienceOptional = profile.getExperiences().stream()
	                .filter(exp -> exp.getId().equals(id))
	                .findFirst();

	        if (experienceOptional.isPresent()) {
	            Experience experienceToRemove = experienceOptional.get();
	            profile.getExperiences().remove(experienceToRemove);

	            profileRepo.save(profile);
	            return true;
	        }
	    }

	    return false;
	}
}