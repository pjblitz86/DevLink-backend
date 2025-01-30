package ca.javau11.controllers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ca.javau11.config.GithubProperties;
import ca.javau11.entities.User;
import ca.javau11.services.UserService;
import ca.javau11.utils.Response;
import jakarta.validation.Valid;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserService userService;
	private final GithubProperties githubProperties;
	
	public UserController(UserService userService, GithubProperties githubProperties) {
		this.userService = userService;
		this.githubProperties = githubProperties;
	}
	
	@PostMapping("/register")
    public ResponseEntity<Response<User>> createUser(@Valid @RequestBody User user) {
        Response<User> response = userService.addUser(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<User>> loginUser(@RequestBody User user) {
        logger.info("call");
    	Response<User> response = userService.loginUser(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
	
	@DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }
	
	@GetMapping("/api/github/{username}")
    public ResponseEntity<Response<List<Object>>> getGithubRepos(@PathVariable String username) {
        logger.info("Fetching GitHub repositories for user: {}", username);
        String githubToken = githubProperties.getToken();
        if (githubToken == null || githubToken.isEmpty()) {
            logger.error("GitHub API Token is missing. Check your configuration.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>("GitHub Token is missing. Check server configuration.", null));
        }
        String uri = String.format(
                "https://api.github.com/users/%s/repos?per_page=5&sort=created:asc",
                username
        );
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Spring Boot App");
            headers.set("Authorization", "Bearer " + githubToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Object[]> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Object[].class
            );
            if (response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response<>("No GitHub profile found for the username", null));
            }
            List<Object> repos = Arrays.asList(response.getBody());
            return ResponseEntity.ok(new Response<>("GitHub repositories fetched successfully", repos));

        } catch (HttpClientErrorException.Unauthorized e) { 
            logger.error("GitHub API returned 401 Unauthorized. Invalid token or missing permissions.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>("GitHub authentication failed. Check your API token.", null));

        } catch (HttpClientErrorException.Forbidden e) { 
            logger.error("GitHub API returned 403 Forbidden. Rate limit exceeded.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Response<>("GitHub API rate limit exceeded. Try again later.", null));

        } catch (Exception e) {
            logger.error("Error fetching GitHub repositories for user: {}", username, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response<>("No GitHub profile found for the username", null));
        }
    }
	
}