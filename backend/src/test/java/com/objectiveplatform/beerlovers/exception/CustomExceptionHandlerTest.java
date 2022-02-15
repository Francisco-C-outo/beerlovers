package com.objectiveplatform.beerlovers.exception;


import com.fasterxml.jackson.core.type.TypeReference;
import com.objectiveplatform.beerlovers.controller.BeerController;
import com.objectiveplatform.beerlovers.service.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.atomic.AtomicReference;

import static com.objectiveplatform.beerlovers.util.testutil.JsonMappingUtils.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class CustomExceptionHandlerTest {

    private static final String URL_FOR_TEST = "http://localhost:8080/v1/beers/";
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_NR = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeerService beerService;

    @WithMockUser(username = "user")
    @Test
    void whenTry_thenOK() throws Exception {
        this.mockMvc.perform(get(URL_FOR_TEST)
                        .param("size", String.valueOf(PAGE_SIZE))
                        .param("page", String.valueOf(PAGE_NR)))
                .andDo(print())
                //THEN
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user")
    @Test
    void whenMethodArgumentMismatch_thenBadRequest() throws Exception {
        final String PAGE_SIZE_AS_STRING = "abc";

        AtomicReference<ApiError> returnedObject = new AtomicReference<>();

        this.mockMvc.perform(get(URL_FOR_TEST)
                        .param("size", PAGE_SIZE_AS_STRING)
                        .param("page", String.valueOf(PAGE_NR)))
                .andDo(print())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    ApiError errorResponse = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
                    });
                    returnedObject.set(errorResponse);
                })
                //THEN
                .andExpect(status().isBadRequest());

        assertEquals(1, returnedObject.get().getErrors().size());
        assertTrue(returnedObject.get().getErrors().get(0).contains("should be of type"));
    }

    @WithMockUser(username = "user")
    @Test
    void whenNoTypeMismatch_ThenBadRequest() throws Exception {
        AtomicReference<ApiError> returnedObject = new AtomicReference<>();

        final String PAGE_SIZE_AS_STRING = "abc";
        this.mockMvc.perform(get(URL_FOR_TEST)
                        .param("size", PAGE_SIZE_AS_STRING)
                        .param("page", String.valueOf(PAGE_NR)))
                .andDo(print())
                .andDo(result -> {
                    String json = result.getResponse().getContentAsString();
                    ApiError errorResponse = OBJECT_MAPPER.readValue(json, new TypeReference<>() {
                    });
                    returnedObject.set(errorResponse);
                })
                //THEN
                .andExpect(status().isBadRequest());

        assertEquals(1, returnedObject.get().getErrors().size());
        assertTrue(returnedObject.get().getErrors().get(0).contains("size should be of"));
    }
}