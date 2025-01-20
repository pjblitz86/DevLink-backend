package ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.Education;
import ca.javau11.services.EducationService;

@CrossOrigin
@RestController
public class EducationController {

	private EducationService educationService;
	
	public EducationController(EducationService educationService) {
		this.educationService = educationService;
	}
	
	@GetMapping("/profile/{profileId}/educations")
    public List<Education> getEducationsByProfile(@PathVariable Long profileId) {
        return educationService.getEducationsByProfileId(profileId);
    }
    
    @GetMapping("/education/{id}")
    public ResponseEntity<Education> getEducation(@PathVariable Long id) {
        Optional<Education> box = educationService.getEducationById(id);
        return ResponseEntity.of(box);
    }
    
    @PostMapping("/profile/{profileId}/education/add")
    public ResponseEntity<Education> addEducation(
            @PathVariable Long profileId,
            @RequestBody Education education) {
        Optional<Education> newEducation = educationService.addEducationToProfile(profileId, education);
        return newEducation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PutMapping("/education/{id}")
    public ResponseEntity<Education> updateEducation(
            @PathVariable Long id,
            @RequestBody Education education) {
        Optional<Education> updatedEducation = educationService.updateEducation(id, education);
        return ResponseEntity.of(updatedEducation);
    }
    
    @DeleteMapping("/education/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        boolean isDeleted = educationService.deleteEducation(id);
        return isDeleted
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
	
}