package ca.javau11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("ca.javau11.repositories")
public class Application {

	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
