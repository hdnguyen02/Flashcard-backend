package hdnguyen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class QuizletApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizletApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner () {
		return arg -> {

		};
	}


}
