package com.aniflex.backed.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aniflex.backed.dto.AnimeHomeData;
import com.aniflex.backed.service.AnimeService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/anime")
public class AnimeController {
	
	@Autowired
	private AnimeService animeService;
	
	@GetMapping("/getAllByFilter")
	public ResponseEntity<String> getAllAnimeByFilter(@RequestParam Map<String, String> filters)
	{
		return animeService.getAllAnimeByFilter(filters);
	}
	
	@GetMapping("/getForHomePage")
	public ResponseEntity<AnimeHomeData> getForHomePage()
	{
		return animeService.getForHomePage();
	}

}
