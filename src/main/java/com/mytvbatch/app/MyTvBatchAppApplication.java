package com.mytvbatch.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyTvBatchAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTvBatchAppApplication.class, args);
	}

}
