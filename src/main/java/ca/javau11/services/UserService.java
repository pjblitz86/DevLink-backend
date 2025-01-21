package ca.javau11.services;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.javau11.entities.User;
import ca.javau11.exceptions.AuthenticationException;
import ca.javau11.exceptions.DuplicateEmailException;
import ca.javau11.exceptions.UserNotFoundException;
import ca.javau11.repositories.UserRepository;
import ca.javau11.utils.JwtUtils;
import ca.javau11.utils.Response;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepo, 
    				   PasswordEncoder passwordEncoder,
    				   JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Response<User> addUser(User user) {
    	if (user.getName() == null || user.getName().isEmpty()) {
            return new Response<>("Name is required");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return new Response<>("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return new Response<>("Password is required");
        }
    	
    	if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new DuplicateEmailException("Email is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAvatar(user.getAvatar());
        User savedUser = userRepo.save(user);
        String jwtToken = JwtUtils.generateToken(savedUser);
        return new Response<>("User registered successfully", savedUser, jwtToken);
    }

    public Response<User> loginUser(User user) {
        User existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            String jwtToken = JwtUtils.generateToken(existingUser);
            return new Response<>("Login successful", existingUser, jwtToken);
        } 
        throw new AuthenticationException("Invalid email or password");
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
        	throw new UserNotFoundException("User not found with id: " + id);
        }
    }
}
