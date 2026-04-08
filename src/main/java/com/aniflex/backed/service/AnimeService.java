package com.aniflex.backed.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class AnimeService {

	private String BASE_URL = "https://api.jikan.moe/v4/anime";

	@Autowired
	private RestTemplate restTemplate;

	@Cacheable(value = "animeCache")
    public ResponseEntity<String> getAllAnime() {
    	return restTemplate.getForEntity(BASE_URL, String.class);
    }

	@Cacheable(value = "animeCache", key = "#filters.toString()")
	public ResponseEntity<String> getAllAnimeByFilter(Map<String, String> filters) {
		System.out.println("Inside Filter Call " + BASE_URL);
		return restTemplate.getForEntity(BASE_URL+uriBuilder(filters), String.class);
	}
	
	private String uriBuilder(Map<String, String> queryParams) {

	    if (queryParams == null || queryParams.isEmpty()) {
	        return "";
	    }

	    return queryParams.entrySet().stream()
	            .map(entry -> entry.getKey() + "=" + entry.getValue())
	            .collect(Collectors.joining("&", "?", ""));
	}

}
