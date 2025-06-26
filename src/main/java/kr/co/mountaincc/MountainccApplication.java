package kr.co.mountaincc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MountainccApplication {

	public static void main(String[] args) {
		SpringApplication.run(MountainccApplication.class, args);
	}

}
