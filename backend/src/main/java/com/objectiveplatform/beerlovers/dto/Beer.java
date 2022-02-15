package com.objectiveplatform.beerlovers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Beer {
    @Min(0)
    @JsonProperty(value = "id")
    private int id;
    @NotBlank
    @JsonProperty(value = "name")
    private String name;
    @NotBlank
    @JsonProperty(value = "description")
    private String description;
    @NotBlank
    @JsonProperty(value = "tagline")
    private String tagline;
    @JsonProperty(value = "image_url")
    private String imageUrl;
}