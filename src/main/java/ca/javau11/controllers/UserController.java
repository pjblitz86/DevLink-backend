package ca.javau11.controllers;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ca.javau11.entities.User;
import ca.javau11.service.CustomUserDetails;
import ca.javau11.services.ProfileService;
import ca.javau11.services.UserService;
import ca.javau11.utils.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserService userService;
	
	public UserController(UserService userService, ProfileService profileService) {
		this.userService = userService;
	}
	
	@PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response<User>> createUser(@Valid @RequestBody User user) {
        Response<User> response = userService.addUser(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<User>> loginUser(@RequestBody User user) {
    	Response<User> response = userService.loginUser(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
	
	@PostMapping("user/upload-avatar/{userId}")
	public ResponseEntity<?> uploadAvatar(
	        @PathVariable Long userId,
	        @RequestParam("avatar") MultipartFile file,
	        @AuthenticationPrincipal CustomUserDetails authenticatedUser,
	        HttpServletRequest request) {

	    if (authenticatedUser == null) 
	        return ResponseEntity.status(403).body(new Response<>("Unauthorized: User is not logged in", null));

	    if (!authenticatedUser.getId().equals(userId))
	        return ResponseEntity.status(403).body(new Response<>("Unauthorized: Cannot update another user's avatar", null));

	    if (file.isEmpty())
	        return ResponseEntity.badRequest().body(new Response<>("File is empty", null));

	    try {
	        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	        String uploadDir = new File("src/main/resources/static/uploads").getAbsolutePath();
	        File directory = new File(uploadDir);
	        if (!directory.exists()) {
	            boolean dirCreated = directory.mkdirs();
	            if (!dirCreated) {
	                return ResponseEntity.status(500).body(new Response<>("Failed to create upload directory", null));
	            }
	        }

	        String fileName = userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        File avatarFile = new File(uploadDir + File.separator + fileName);

	        file.transferTo(avatarFile);

	        User user = userService.getUserById(userId);
	        if (user == null)
	            return ResponseEntity.badRequest().body(new Response<>("User not found", null));

	        String avatarUrl = baseUrl + "/uploads/" + fileName;
	        user.setAvatar(avatarUrl);
	        userService.saveUser(user);

	        return ResponseEntity.ok(new Response<>("Avatar uploaded successfully", avatarUrl));

	    } catch (IOException e) {
	        return ResponseEntity.status(500).body(new Response<>("File upload failed: " + e.getMessage(), null));
	    }
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails authenticatedUser) {
	    if (authenticatedUser == null)
	        return ResponseEntity.status(403).body(new Response<>("Unauthorized: User not logged in", null));

	    if (!authenticatedUser.getId().equals(id))
	        return ResponseEntity.status(403).body(new Response<>("Unauthorized: Cannot delete another user's account", null));

	    try {
	        boolean userDeleted = userService.deleteUserById(id);
	        if (!userDeleted)
	            return ResponseEntity.status(403).body(new Response<>("Failed to delete user", null));

	        return ResponseEntity.ok(new Response<>("User deleted successfully", true));

	    } catch (Exception e) {
	        return ResponseEntity.status(500).body(new Response<>("Error deleting user: " + e.getMessage(), null));
	    }
	}
	
}