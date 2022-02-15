package com.objectiveplatform.beerlovers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class User {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
