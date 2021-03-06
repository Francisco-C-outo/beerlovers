package com.objectiveplatform.beerlovers.service;

import com.objectiveplatform.beerlovers.dto.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * This service represent our waypoint to communicate with the external API we are consuming to retrieve any relevant
 * information about beers that we present in our client layer. The operations used and its format are based on the
 * specification present in the punkAPI website
 */
@Service
public class PunkApiService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final String PUNK_API_BASE_URL = "https://api.punkapi.com/v2/";
    private static final String BASE_ENDPOINT = "/beers/";

    private static final String NR_ELEMENTS_PER_CALL_PARAM = "per_page";
    private static final String PAGE_NR_PARAM = "page";
    private static final String BEER_NAME_PARAM = "beer_name";

    private final WebClient webClient;

    @Autowired
    public PunkApiService() {
        this.webClient = WebClient.create(PUNK_API_BASE_URL);
    }

    public List<Beer> getBeersFromPunkApi(final int page, final int size) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_ENDPOINT)
                        .queryParam(PAGE_NR_PARAM, page)
                        .queryParam(NR_ELEMENTS_PER_CALL_PARAM, size)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Beer>>() {
                })
                .block(REQUEST_TIMEOUT);
    }

    public List<Beer> getAllBeersFromPunkApi() {
        boolean complete = false;
        final AtomicInteger page = new AtomicInteger(1);
        final int MAX_BEERS_PER_PAGE = 80;

        final List<Beer> fullResults = new ArrayList<>();

        while (!complete) {
            final List<Beer> result = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_ENDPOINT)
                            .queryParam(PAGE_NR_PARAM, page)
                            .queryParam(NR_ELEMENTS_PER_CALL_PARAM, MAX_BEERS_PER_PAGE)
                            .build())
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Beer>>() {
                    })
                    .block(REQUEST_TIMEOUT);

            if (isNull(result)) {
                throw new IllegalStateException("Unable to fetch data from punk API");
            }

            fullResults.addAll(result);
            page.getAndIncrement();
            if (result.isEmpty() || result.size() < 80) {
                complete = true;
            }
        }
        return fullResults;
    }

    /**
     * Method used to receive search results filtered from the API
     * Currently it filters by beer_name (amongst others) but not by text in the description as required
     */
    @Deprecated
    public List<Beer> getBeersFromPunkApiFiltered(final int page, final int size, final String keyword) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_ENDPOINT)
                        .queryParam(BEER_NAME_PARAM, keyword)
                        .queryParam(PAGE_NR_PARAM, page)
                        .queryParam(NR_ELEMENTS_PER_CALL_PARAM, size)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Beer>>() {
                })
                .block(REQUEST_TIMEOUT);
    }

    public List<Beer> getBeersFromPunkApiById(final Set<Integer> externalIds) {

        //As per the api documentation the ids must be supplied separated by a '|' symbol
        final String inputParameter = externalIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_ENDPOINT)
                        .queryParam("ids", inputParameter)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Beer>>() {
                })
                .block(REQUEST_TIMEOUT);
    }
}
