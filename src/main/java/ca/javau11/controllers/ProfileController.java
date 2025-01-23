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

import ca.javau11.entities.Profile;
import ca.javau11.services.ProfileService;

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
	public ResponseEntity<Profile> getProfileByUserId(@PathVariable Long userId) {
		Optional<Profile> box = profileService.getProfileByUserId(userId);
		return ResponseEntity.of(box);
	}
	
	@PostMapping("/profile/user/{id}")
	public Profile createProfile(@PathVariable Long id, @RequestBody Profile profile) {
		return profileService.addProfile(id, profile);
	}
	
	@PutMapping("profile/{id}")
	public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @RequestBody Profile profile) {
		Optional<Profile> box = profileService.updateProfile(id, profile);
		return ResponseEntity.of(box);
	}
	
	@DeleteMapping("profile/{id}")
	public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
		boolean isDeleted = profileService.deleteProfile(id);
		return isDeleted? 
				ResponseEntity.ok().build() 
				: ResponseEntity.notFound().build();
	}
	
}