package ca.javau11.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.dtos.ProfileDTO;
import ca.javau11.entities.Profile;
import ca.javau11.services.ProfileService;
import ca.javau11.utils.Response;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

	private ProfileService profileService;
	
	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	@GetMapping
	public ResponseEntity<?> getProfiles() {
	    List<Profile> profiles = profileService.getProfiles();
	    List<Profile> validProfiles = profiles.stream()
	        .filter(profile -> profile.getUser() != null)
	        .collect(Collectors.toList());
	    return ResponseEntity.ok(validProfiles);
	}
	
	@GetMapping("/{profileId}")
	public ResponseEntity<?> getProfileById(@PathVariable Long profileId) {
	    Optional<Profile> profile = profileService.getProfileById(profileId);

	    if (profile.isEmpty())
	        return ResponseEntity.ok(new Response<>("No profile found with the given ID", null));

	    return ResponseEntity.ok(new Response<>("Profile found", profile.get()));
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getProfileByUserId(@PathVariable Long userId) {
	    Optional<Profile> box = profileService.getProfileByUserId(userId);
	    if (box.isEmpty())
	    	return ResponseEntity.ok(new Response<>("No profile exists for this user", null));
	    
	    return ResponseEntity.ok(new Response<>("Profile found", box.get()));
	}
	
	@PostMapping("/user/{userId}")
	public ResponseEntity<?> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileDTO profileDTO) {
        Profile profile = profileService.addProfile(userId, profileDTO);
        return ResponseEntity.status(201).body(new Response<>("Profile successfully created", profile));
    }
	
	@PutMapping("/user/{userId}")
	public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileDTO profileDTO) {
        Profile profile = profileService.updateProfile(userId, profileDTO);
        return ResponseEntity.ok(new Response<>("Profile successfully updated", profile));
    }
	
	@DeleteMapping("/{profileId}")
	public ResponseEntity<Void> deleteProfile(@PathVariable Long profileId) {
		boolean isDeleted = profileService.deleteProfile(profileId);
		return isDeleted
			   ? ResponseEntity.ok().build() 
			   : ResponseEntity.notFound().build();
	}
	
}