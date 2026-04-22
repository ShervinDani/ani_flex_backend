package com.aniflex.backed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Image {
	
	@JsonProperty("jpg")
	private Jpg jpg;
	
}
