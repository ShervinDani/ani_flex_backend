package com.aniflex.backed.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Genre {
	
	@JsonProperty("data")
	private List<GenreData> data;
	
}
