package net.easyhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin("http://localhost:4200")
public class IntegrationEasyhouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationEasyhouseApplication.class, args);
	}

}
