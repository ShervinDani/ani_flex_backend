package com.aniflex.backed.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aniflex.backed.service.AnimeService;

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
		System.out.println("Inside Filter Call");
		return animeService.getAllAnimeByFilter(filters);
	}

}
