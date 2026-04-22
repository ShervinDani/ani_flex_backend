package com.aniflex.backed.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aniflex.backed.service.AnimeService;

import jakarta.annotation.PostConstruct;

@Component
public class Scheduler {
	
	@Autowired
	private AnimeService animeService;
	
	@PostConstruct
	public void init()
	{
		logger.info("Initializing Data...!");
		animeService.refreshGenre();
		logger.info("Genre Reloading is Completed...!");
		animeService.refreshAnimeHomeDetails("popularity");
		logger.info("Populer Anime Reload is Completed...!");
		animeService.refreshAnimeHomeDetails("rank");
		
	}
	
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	@Scheduled(cron = "${refresh.time}")
	public void genreScheduler()
	{
		logger.info("Scheduler Running ...!");
		animeService.refreshGenre();
	}
	
}
