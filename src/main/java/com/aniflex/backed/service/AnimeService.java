package com.aniflex.backed.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aniflex.backed.dto.Genre;
import com.aniflex.backed.dto.GenreData;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;



@Service
public class AnimeService {

	private String BASE_URL = "https://api.jikan.moe/v4/";
	private static final Logger logger = LoggerFactory.getLogger(AnimeService.class);
	
	@Autowired
	private ObjectMapper mapper;

	private JsonNode jsonNode;

	@Autowired
	private RestTemplate restTemplate;

	@Cacheable(value = "animeCache")
    public ResponseEntity<String> getAllAnime() {
    	return restTemplate.getForEntity(BASE_URL + "anime", String.class);
    }

	@Cacheable(value = "animeCache", key = "#filters.toString()")
	public ResponseEntity<String> getAllAnimeByFilter(Map<String, String> filters) {
		logger.info(BASE_URL + " Called..!");
		System.out.println("Inside Filter Call " + BASE_URL);
		return restTemplate.getForEntity(BASE_URL + "anime" +uriBuilder(filters), String.class);
	}
	
	private String uriBuilder(Map<String, String> queryParams) {

	    if (queryParams == null || queryParams.isEmpty()) {
	        return "";
	    }

	    return queryParams.entrySet().stream()
	            .map(entry -> entry.getKey() + "=" + entry.getValue())
	            .collect(Collectors.joining("&", "?", ""));
	}

	@Cacheable(value = "animeCache", key = "'genres'")
	public ResponseEntity<Genre> getGenres() {
		return loadGenre();
	}
	
	@CachePut(value = "animeCache", key = "'genres'")
	public ResponseEntity<Genre> refreshGenre() {
		return loadGenre();
	}
	
	private ResponseEntity<Genre> loadGenre()
	{
		ResponseEntity<Genre> responseBody =  restTemplate.getForEntity(BASE_URL + "genres/anime", Genre.class);
		Genre genre = responseBody.getBody();
		for(GenreData data : genre.getData())
		{
			this.getGenreAnime(data);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Genre>(genre, HttpStatus.OK);
	}
	
	private void getGenreAnime(GenreData data)
	{
		ResponseEntity<String> responseBody = restTemplate.getForEntity(BASE_URL + "/anime?genres=" + data.getMalId() + "&limit=1" , String.class);
		jsonNode = mapper.readTree(responseBody.getBody());
		jsonNode = jsonNode.path("data").get(0);
		data.setAnimeName(jsonNode.path("title_english").asString());
		data.setAnimeUrl(jsonNode.path("images").path("jpg").path("image_url").asString());
	}

	public ResponseEntity<String> getForHomePage(String category) {
		return null;
	}

	public void refreshAnimeHomeDetails(String category) {
		
	}
	
	
}
