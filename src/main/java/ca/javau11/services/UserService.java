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
    
    public UserService(UserRepository userRepo, 
    				   PasswordEncoder passwordEncoder,
    				   JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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
        logger.info("Attempting login for email: " + user.getEmail());

        Optional<User> optionalUser = userRepo.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
        	logger.info("User found: " + optionalUser.get());
            if (passwordEncoder.matches(user.getPassword(), optionalUser.get().getPassword())) {
                String jwtToken = JwtUtils.generateToken(optionalUser.get());
                logger.info("Login successful, token generated");
                return new Response<>("Login successful", optionalUser.get(), jwtToken);
            } else {
            	logger.info("Password mismatch");
            }
        } else {
        	logger.info("User not found with email: " + user.getEmail());
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
