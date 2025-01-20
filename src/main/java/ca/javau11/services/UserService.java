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

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User addUser(User user) {
    	user.setAvatar(user.getAvatar());
    	if (userRepo.findByEmail(user.getEmail()) != null) {
    		throw new DuplicateEmailException("Email is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User loginUser(User user) {
        User existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return existingUser;
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
