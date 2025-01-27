package ca.javau11.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.controllers.ExperienceController;
import ca.javau11.entities.Education;
import ca.javau11.entities.Experience;
import ca.javau11.entities.Profile;
import ca.javau11.repositories.ProfileRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExperienceService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperienceService.class);
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

	// TODO: refactor method
	public Optional<Experience> updateExperience(Long id, Experience experienceToUpdate) {
		for (Profile profile : profileRepo.findAll()) {
            Optional<Experience> experienceOptional = profile.getExperiences().stream()
                    .filter(exp -> exp.getId().equals(id))
                    .findFirst();

            if (experienceOptional.isPresent()) {
                Experience experience = experienceOptional.get();
                experience.setTitle(experienceToUpdate.getTitle());
                experience.setCompany(experienceToUpdate.getCompany());
                experience.setLocation(experienceToUpdate.getLocation());
                experience.setStartDate(experienceToUpdate.getStartDate());
                experience.setEndDate(experienceToUpdate.getEndDate());
                experience.setCurrent(experienceToUpdate.getCurrent());
                experience.setDescription(experienceToUpdate.getDescription());

                profileRepo.save(profile);
                return Optional.of(experience);
            }
        }
        return Optional.empty();
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