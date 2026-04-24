package com.aniflex.backed.service;

import java.util.List;
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
		try
		{
			animeHomeData.setGenreHighlights(this.loadGenre());
			animeHomeData.setPopularNow(loadAnime("order_by=popularity"));
			Thread.sleep(500);
			animeHomeData.setNewReleases(this.loadAnime("status=airing&sort=desc"));
			Thread.sleep(500);
			animeHomeData.setTopRanking(this.loadAnime("min_score=5&sort=desc"));
			Thread.sleep(500);
			animeHomeData.setUpComing(this.loadAnime("status=upcoming&sort=desc"));
			Thread.sleep(500);
			animeHomeData.setCompleted(this.loadAnime("status=complete&sort=desc"));
			Thread.sleep(500);
			animeHomeData.setMovieAnime(this.loadAnime("type=movie&min_score=5&status=complete&sort=desc"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<AnimeHomeData>(animeHomeData, HttpStatus.OK);
	}

	private AnimeCard loadAnime(String filter) {
		ResponseEntity<AnimeCard> responseBody = restTemplate
				.getForEntity(BASE_URL + "anime?" + filter, AnimeCard.class);
		AnimeCard card = responseBody.getBody();
	    List<AnimeCardData> deduped = card.getData().stream()
	        .collect(Collectors.toMap(
	            AnimeCardData::getMal_id,
	            d -> d,
	            (existing, duplicate) -> existing  // keep first on conflict
	        ))
	        .values()
	        .stream()
	        .collect(Collectors.toList());
	    
	    card.setData(deduped);
	    return card;
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
		jpg.setLarge_image_url(jsonNode.path("images").path("jpg").path("large_image_url").asString());
		jpg.setSmall_image_url(jsonNode.path("images").path("jpg").path("small_image_url").asString());
		image.setJpg(jpg);
		data.setImages(image);
	}

}
