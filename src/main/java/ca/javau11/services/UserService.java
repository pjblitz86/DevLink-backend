package ca.javau11.services;

import org.springframework.stereotype.Service;

import ca.javau11.entities.User;
import ca.javau11.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepo;
	
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public User addUser(User user) {
		return userRepo.save(user);
	}

	public User loginUser(User user) {
		// TODO 
		return null;
	}
	
}