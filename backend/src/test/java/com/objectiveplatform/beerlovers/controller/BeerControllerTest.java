package com.objectiveplatform.beerlovers.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.objectiveplatform.beerlovers.dto.Beer;
import com.objectiveplatform.beerlovers.dto.BeerResponse;
import com.objectiveplatform.beerlovers.service.BeerService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.objectiveplatform.beerlovers.util.testutil.JsonMappingUtils.OBJECT_MAPPER;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    private static final String ROOT_ENDPOINT = "/v1/beers";
    private final EasyRandom generator = new EasyRandom();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BeerService beerService;

    @WithMockUser(username = "user")
    @Test
    void testGetBeersHappyCase() throws Exception {
        //GIVEN
        final int page = 1;
        final int size = 10;
        final int totalSize = 20;
        final List<Beer> serviceResult = generator.objects(Beer.class, 10).collect(Collectors.toList());

        //WHEN
        when(beerService.getBeersPaginated(page, size)).thenReturn(new BeerResponse(serviceResult, totalSize));

        AtomicReference<BeerResponse> returnedObject = new AtomicReference<>();

        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("size", String.valueOf(size))
                        .param("page", String.valueOf(page)))
                .andDo(print())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    BeerResponse response = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
                    });
                    returnedObject.set(response);
                })
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.beers", hasSize(10)))
                .andExpect(jsonPath("$.totalResults", is(20)));
    }

    @Test
    void testGetBeersNoAuth() throws Exception {
        //GIVEN
        final int page = 1;
        final int size = 10;

        //WHEN
        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("size", String.valueOf(size))
                        .param("page", String.valueOf(page)))
                .andDo(print())
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user")
    @Test
    void testGetBeersMissingParam() throws Exception {
        //GIVEN
        final int size = 10;

        //WHEN
        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("size", String.valueOf(size)))
                .andDo(print())
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @WithMockUser(username = "user")
    @Test
    void testGetBeersEmptyParameter() throws Exception {
        //GIVEN
        final int page = 1;
        AtomicReference<BeerResponse> returnedObject = new AtomicReference<>();

        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("size", "")
                        .param("page", String.valueOf(page)))
                .andDo(print())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    BeerResponse response = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
                    });
                    returnedObject.set(response);
                })
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @WithMockUser(username = "user")
    @Test
    void testGetBeersFilteredHappyCase() throws Exception {
        //GIVEN
        final int page = 1;
        final int size = 2;
        final int totalSize = 2;
        final String keyword = "beer";
        final List<Beer> serviceResult = generator.objects(Beer.class, 2).collect(Collectors.toList());

        //WHEN
        when(beerService.getBeersFiltered(page, size, keyword)).thenReturn(new BeerResponse(serviceResult, totalSize));

        AtomicReference<BeerResponse> returnedObject = new AtomicReference<>();

        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("size", String.valueOf(size))
                        .param("page", String.valueOf(page))
                        .param("keyword", keyword))
                .andDo(print())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    BeerResponse response = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
                    });
                    returnedObject.set(response);
                })
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.beers", hasSize(2)))
                .andExpect(jsonPath("$.totalResults", is(2)));
    }

    @Test
    void testGetBeersFilteredNoAuth() throws Exception {
        //GIVEN
        final int page = 1;
        final int size = 2;
        final String keyword = "beer";

        //WHEN
        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("size", String.valueOf(size))
                        .param("page", String.valueOf(page))
                        .param("keyword", keyword))
                .andDo(print())
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user")
    @Test
    void testGetBeersFilteredMissingParam() throws Exception {
        //GIVEN
        final int page = 1;
        final String keyword = "beer";

        //WHEN
        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("page", String.valueOf(page))
                        .param("keyword", keyword))
                .andDo(print())
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @WithMockUser(username = "user")
    @Test
    void testGetBeersFilteredEmptyParameter() throws Exception {
        //GIVEN
        final int page = 1;

        AtomicReference<BeerResponse> returnedObject = new AtomicReference<>();

        this.mockMvc.perform(get(ROOT_ENDPOINT + '/')
                        .param("size", "")
                        .param("page", String.valueOf(page)))
                .andDo(print())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    BeerResponse response = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
                    });
                    returnedObject.set(response);
                })
                .andDo(print())
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)));

    }

    @WithMockUser(username = "user")
    @Test
    void testGetFavoriteBeersHappyCase() throws Exception {
        //GIVEN
        final List<Beer> serviceResult = generator.objects(Beer.class, 20).collect(Collectors.toList());

        //WHEN
        when(beerService.getFavoriteBeersForUser()).thenReturn(serviceResult);

        AtomicReference<List<Beer>> returnedObject = new AtomicReference<>();

        this.mockMvc.perform(get(ROOT_ENDPOINT + "/favorites"))
                .andDo(print())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<Beer> response = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
                    });
                    returnedObject.set(response);
                })
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(20)));
    }

    @Test
    void testGetFavoriteBeersNoAuth() throws Exception {
        //WHEN
        this.mockMvc.perform(get(ROOT_ENDPOINT + "/favorites"))
                .andDo(print())
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addFavoriteBeerForUser() throws Exception {
        //GIVEN
        final Beer beer = new Beer();
        beer.setName("Super Bock");
        beer.setTagline("It throws you back to your college years...");
        beer.setDescription("The best beer in Portugal!");
        beer.setImageUrl("");
        //WHEN
        when(beerService.createFavoriteEntryForUser(beer)).thenReturn(beer);
        this.mockMvc.perform(post(ROOT_ENDPOINT + "/favorites")
                        .param("beer", String.valueOf(beer)))
                .andDo(print())
                //THEN
                .andExpect(status().isUnauthorized());
    }
}