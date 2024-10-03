package io.javabrains.tinder_ai_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinderAiBackendApplication implements CommandLineRunner {

	@Autowired
	private io.javabrains.tinder_ai_backend.profiles.ProfileCreationService profileCreationService;

	public static void main(String[] args) {
		SpringApplication.run(TinderAiBackendApplication.class, args);
	}

	public void run(String... args){
		profileCreationService.createProfiles(0);
		profileCreationService.saveProfilesToDB();
	}
}


