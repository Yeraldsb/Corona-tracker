package com.example.coronatracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Tells spring to wrap the method in coronaData.java and call it
public class CoronaTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoronaTrackerApplication.class, args);
    }

}
