package com.example.UserService;

import com.example.UserService.service.KeycloakService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
@OpenAPIDefinition(
		info = @Info(
				title = "VN GroupBy API",
				version = "1.0.0",
				description = "Building VN GroupBy e-commerce platform",
				contact = @Contact(
						name = "Trịnh Quang Tuyến",
						email = "tuyen.tq196733@sis.hust.edu.vn"
				)
		)
)
public class UserServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
		;
	}

}
