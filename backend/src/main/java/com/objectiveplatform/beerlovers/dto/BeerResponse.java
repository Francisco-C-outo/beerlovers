package com.objectiveplatform.beerlovers.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BeerResponse {
    private final List<Beer> beers;
    private final long totalResults;
}
