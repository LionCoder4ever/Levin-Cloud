package com.lc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;


/**
 * spring cloud gateway session
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisWebSession
public class GatewaySessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewaySessionApplication.class, args);
	}

}
