package com.aniflex.backed.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class ApiConfig {
	
	@Bean
	RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
	
	@Bean
	CacheManager cacheManager()
	{
		return new ConcurrentMapCacheManager("animeCache");
	}
	
	@Bean
	ObjectMapper objectMapper()
	{
		return new ObjectMapper();
	}
	
}
