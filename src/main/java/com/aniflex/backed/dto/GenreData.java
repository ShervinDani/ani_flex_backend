package com.aniflex.backed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GenreData {
	
	@JsonProperty("mal_id")
	private int malId;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("url")
	private String url;
	
	private String animeName;
	
	private String animeUrl;
	
}
