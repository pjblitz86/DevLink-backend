package ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ca.javau11.entities.Education;
import ca.javau11.entities.Profile;
import ca.javau11.repositories.ProfileRepository;

@Service
public class EducationService {

	private ProfileRepository profileRepo;

	public EducationService(ProfileRepository profileRepo) {
		this.profileRepo = profileRepo;
	}

	public List<Education> getEducationsByProfileId(Long profileId) {
		return profileRepo.findById(profileId)
                .map(Profile::getEducations)
                .orElseThrow(() -> new IllegalArgumentException("Education not found for ID: " + profileId));
	}

	public Optional<Education> getEducationById(Long educationId) {
		return profileRepo.findAll().stream()
                .flatMap(education -> education.getEducations().stream())
                .filter(education -> education.getId().equals(educationId))
                .findFirst();
	}

	public Optional<Education> addEducationToProfile(Long profileId, Education education) {
		return profileRepo.findById(profileId).map(profile -> {
			education.setProfile(profile);
            profile.getEducations().add(education);
            profileRepo.save(profile);
            return education;
        });
	}

	public boolean deleteEducation(Long id) {
		for (Profile profile : profileRepo.findAll()) {
	        Optional<Education> educationOptional = profile.getEducations().stream()
	                .filter(edu -> edu.getId().equals(id))
	                .findFirst();

	        if (educationOptional.isPresent()) {
	            Education educationToRemove = educationOptional.get();
	            profile.getEducations().remove(educationToRemove);

	            profileRepo.save(profile);
	            return true;
	        }
	    }

	    return false;
	}
}