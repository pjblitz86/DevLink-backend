package ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.dtos.ProfileDTO;
import ca.javau11.entities.Profile;
import ca.javau11.services.ProfileService;
import ca.javau11.utils.Response;
import jakarta.validation.Valid;

@RestController
public class ProfileController {

	private ProfileService profileService;
	
	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	@GetMapping("/profiles")
	public List<Profile> getProfiles() {
		return profileService.getProfiles();
	}
	
	@GetMapping("/profile/{userId}")
	public ResponseEntity<?> getProfileByUserId(@PathVariable Long userId) {
	    Optional<Profile> box = profileService.getProfileByUserId(userId);
	    if (box.isEmpty()) {
	    	return ResponseEntity.ok(new Response<>("No profile exists for this user", null));
	    }
	    return ResponseEntity.ok(new Response<>("Profile found", box.get()));
	}
	
	@PostMapping("/profile/{userId}")
	public ResponseEntity<?> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileDTO profileDTO) {
        Profile profile = profileService.addProfile(userId, profileDTO);
        return ResponseEntity.status(201).body(new Response<>("Profile successfully created", profile));
    }
	
	@PutMapping("profile/{userId}")
	public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileDTO profileDTO) {
        Profile profile = profileService.updateProfile(userId, profileDTO);
        return ResponseEntity.ok(new Response<>("Profile successfully updated", profile));
    }
	
	@DeleteMapping("profile/{id}")
	public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
		boolean isDeleted = profileService.deleteProfile(id);
		return isDeleted? 
				ResponseEntity.ok().build() 
				: ResponseEntity.notFound().build();
	}
	
}