package com.aniflex.backed.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aniflex.backed.dto.AnimeCard;
import com.aniflex.backed.dto.AnimeCardData;
import com.aniflex.backed.dto.AnimeHomeData;
import com.aniflex.backed.dto.Image;
import com.aniflex.backed.dto.Jpg;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AnimeService {

	private String BASE_URL = "https://api.jikan.moe/v4/";

	@Autowired
	private ObjectMapper mapper;

	private JsonNode jsonNode;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AnimeHomeData animeHomeData;

	@Cacheable(value = "animeCache")
	public ResponseEntity<String> getAllAnime() {
		return restTemplate.getForEntity(BASE_URL + "anime", String.class);
	}

	public ResponseEntity<String> getAllAnimeByFilter(Map<String, String> filters) {
		return restTemplate.getForEntity(BASE_URL + "anime" + uriBuilder(filters), String.class);
	}

	private String uriBuilder(Map<String, String> queryParams) {

		if (queryParams == null || queryParams.isEmpty()) {
			return "";
		}

		return queryParams.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
				.collect(Collectors.joining("&", "?", ""));
	}

	@Cacheable(value = "animeCache", key = "'homePageList'")
	public ResponseEntity<AnimeHomeData> getForHomePage() {
		return this.loadAnimeForHomePage();
	}

	public void refreshAnimeHomeDetails(String category) {

	}

	public ResponseEntity<AnimeHomeData> loadAnimeForHomePage() {
		animeHomeData.setGenreHighlights(this.loadGenre());
		animeHomeData.setPopularNow(loadAnime("popularity"));
		animeHomeData.setNewReleases(this.loadAnime("rank"));
		animeHomeData.setTopRanking(this.loadAnime("rank"));
		return new ResponseEntity<AnimeHomeData>(animeHomeData, HttpStatus.OK);
	}

	private AnimeCard loadAnime(String filter) {
		ResponseEntity<AnimeCard> responseBody = restTemplate
				.getForEntity(BASE_URL + "anime" + "?order_by=" + filter + "&sort=desc", AnimeCard.class);
		AnimeCard genre = responseBody.getBody();
		return genre;
	}

	private AnimeCard loadGenre() {
		ResponseEntity<AnimeCard> responseBody = restTemplate.getForEntity(BASE_URL + "genres/anime", AnimeCard.class);
		AnimeCard genre = responseBody.getBody();
		for (AnimeCardData data : genre.getData()) {
			this.getGenreAnime(data);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return genre;
	}

	private void getGenreAnime(AnimeCardData data) {
		ResponseEntity<String> responseBody = restTemplate
				.getForEntity(BASE_URL + "/anime?genres=" + data.getMal_id() + "&limit=1", String.class);
		jsonNode = mapper.readTree(responseBody.getBody());
		jsonNode = jsonNode.path("data").get(0);
		data.setTitle(jsonNode.path("title").asString());
		Image image = new Image();
		Jpg jpg = new Jpg();
		jpg.setImage_url(jsonNode.path("images").path("jpg").path("image_url").asString());
		image.setJpg(jpg);
		data.setImages(image);
	}

}
