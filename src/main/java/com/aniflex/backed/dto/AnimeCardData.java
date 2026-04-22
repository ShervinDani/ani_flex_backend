package com.aniflex.backed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AnimeCardData {
	
	@JsonProperty("mal_id")
	private int mal_id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("images")
	private Image images;
	
}
