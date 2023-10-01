package hdnguyen;


import hdnguyen.dao.DeskDao;
import hdnguyen.dao.TopicDao;
import hdnguyen.entity.Desk;
import hdnguyen.entity.Label;
import hdnguyen.entity.Topic;
import hdnguyen.service.TopicService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.Set;


@SpringBootApplication
public class QuizletApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(QuizletApiApplication.class, args);
	}

	@Bean // kiến thức về arrow function của interface => interface chỉ có phương thức cần override
	CommandLineRunner commandLineRunner (DeskDao deskDao) {
		return arg -> {


		};
	}


}
