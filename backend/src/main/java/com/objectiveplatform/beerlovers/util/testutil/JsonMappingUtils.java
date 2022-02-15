package com.objectiveplatform.beerlovers.util.testutil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JsonMappingUtils {

    public static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder
            .json()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    public static String asJsonString(final Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
