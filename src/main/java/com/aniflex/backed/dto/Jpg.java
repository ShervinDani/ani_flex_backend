package com.aniflex.backed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Jpg {
	
	@JsonProperty("image_url")
	private String image_url;
	@JsonProperty("small_image_url")
	private String small_image_url;
	@JsonProperty("large_image_url")
	private String large_image_url;
	
}
