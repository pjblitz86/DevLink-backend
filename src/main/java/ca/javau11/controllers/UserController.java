package ca.javau11.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.User;
import ca.javau11.services.UserService;

@CrossOrigin
@RestController
public class UserController {

	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public User createUser(@RequestBody User user) {
		return userService.addUser(user);
	}
	
	@PostMapping("/login")
	public User loginUser(@RequestBody User user) {
		return userService.loginUser(user);
	}
	
}