package com.reason.springsec.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormLoginDto {
    @JsonProperty(value = "userid")
    private String id = null;

    @JsonProperty(value = "password")
    private String password = null;
}
