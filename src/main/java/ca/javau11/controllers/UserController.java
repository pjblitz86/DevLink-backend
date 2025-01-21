package ca.javau11.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.javau11.entities.User;
import ca.javau11.services.UserService;
import ca.javau11.utils.Response;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class UserController {

	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register")
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
	
}