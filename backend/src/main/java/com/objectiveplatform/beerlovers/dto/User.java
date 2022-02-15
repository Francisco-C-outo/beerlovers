package com.objectiveplatform.beerlovers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
