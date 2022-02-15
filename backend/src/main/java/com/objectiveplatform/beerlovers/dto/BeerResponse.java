package com.objectiveplatform.beerlovers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BeerResponse {
    private List<Beer> beers;
    private long totalResults;
}
