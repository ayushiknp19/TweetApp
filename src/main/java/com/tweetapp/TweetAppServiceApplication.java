package com.tweetapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.Generated;

@SpringBootApplication
@EnableJpaRepositories
@Generated
@EnableAspectJAutoProxy
@EntityScan(basePackages ={"com.tweetapp.*"})
public class TweetAppServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetAppServiceApplication.class, args);
	}

}
