package com.aniflex.backed.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AnimeHomeData {
	
	private AnimeCard topRanking;
	private AnimeCard popularNow;
	private AnimeCard newReleases;
	private AnimeCard genreHighlights;
	private AnimeCard upComing;
	private AnimeCard completed;
	private AnimeCard movieAnime;
	
}
