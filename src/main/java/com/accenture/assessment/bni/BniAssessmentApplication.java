package com.accenture.assessment.bni;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
public class BniAssessmentApplication {
	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
	}
	public static void main(String[] args) {
		SpringApplication.run(BniAssessmentApplication.class, args);
	}

}
