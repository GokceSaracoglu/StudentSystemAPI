package com.saracoglu.student.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.saracoglu.student.system")

public class StudentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentSystemApplication.class, args);
	}

}
