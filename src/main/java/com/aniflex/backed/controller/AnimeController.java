package com.aniflex.backed.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aniflex.backed.dto.Genre;
import com.aniflex.backed.service.AnimeService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/anime")
public class AnimeController {
	
	@Autowired
	private AnimeService animeService;
	
	@GetMapping("/getAll")
	public ResponseEntity<String> getAllAnime()
	{
		return animeService.getAllAnime();
	}
	
	@GetMapping("/getAllByFilter")
	public ResponseEntity<String> getAllAnimeByFilter(@RequestParam Map<String, String> filters)
	{
		return animeService.getAllAnimeByFilter(filters);
	}
	
	@GetMapping("/getForHomePage")
	public ResponseEntity<String> getForHomePage(@RequestParam String category)
	{
		return animeService.getForHomePage(category);
	}
	
	@GetMapping("/getGenres")
	public ResponseEntity<Genre> getGenres()
	{
		return animeService.getGenres();
	}

}
