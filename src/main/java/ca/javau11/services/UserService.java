package ca.javau11.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.javau11.entities.User;
import ca.javau11.exceptions.AuthenticationException;
import ca.javau11.exceptions.DuplicateEmailException;
import ca.javau11.exceptions.UserNotFoundException;
import ca.javau11.repositories.UserRepository;
import ca.javau11.utils.JwtUtils;
import ca.javau11.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    public UserService(UserRepository userRepo, 
    				   PasswordEncoder passwordEncoder,
    				   JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
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

        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateEmailException("Email is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        String jwtToken = jwtUtils.generateToken(savedUser);
        return new Response<>("User registered successfully", savedUser, jwtToken);
    }

    public Response<User> loginUser(User user) {
        Optional<User> optionalUser = userRepo.findByEmail(user.getEmail());
        if (optionalUser.isEmpty()) {
            throw new AuthenticationException("User empty" + user.getEmail());
        }
        User existingUser = optionalUser.get();
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new AuthenticationException("Invalid email or password");
        }
        String jwtToken = jwtUtils.generateToken(existingUser);
        return new Response<>("Login successful", existingUser, jwtToken);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
        	throw new UserNotFoundException("User not found with id: " + id);
        }
    }

	public boolean deleteUserById(Long id) {
		Optional<User> box = userRepo.findById(id);
	    if (box.isEmpty()) return false;	    
	    User user = box.get();
	    userRepo.delete(user);
	    return true;
	}
}
