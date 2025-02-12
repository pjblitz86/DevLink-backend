package ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.Experience;
import ca.javau11.services.ExperienceService;

@RestController
@RequestMapping("/api")
public class ExperienceController {
	
	private ExperienceService experienceService;
	
	public ExperienceController(ExperienceService experienceService) {
		this.experienceService = experienceService;
	}
	
	@GetMapping("/profiles/{profileId}/experiences")
    public List<Experience> getExperiencesByProfile(@PathVariable Long profileId) {
        return experienceService.getExperiencesByProfileId(profileId);
    }

    @GetMapping("/experience/{id}")
    public ResponseEntity<Experience> getExperience(@PathVariable Long id) {
        Optional<Experience> box = experienceService.getExperienceById(id);
        return ResponseEntity.of(box);
    }
    
    @PostMapping("/profiles/{profileId}/experience/add")
    public ResponseEntity<Experience> addExperience(
            @PathVariable Long profileId,
            @RequestBody Experience experience) {
        Optional<Experience> newExperience = experienceService.addExperienceToProfile(profileId, experience);
        return newExperience
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/experience/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        boolean isDeleted = experienceService.deleteExperience(id);
        return isDeleted
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
	
}