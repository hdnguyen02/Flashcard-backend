package hdnguyen;

import hdnguyen.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FlashcardApplication {
	public static void main(String[] args) {
		SpringApplication.run(FlashcardApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner (RoleService roleService) {
		return arg -> {
			roleService.initRole();
		};
	}


}
