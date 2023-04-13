package com.accenture.assessment.bni.controller.response;

import com.accenture.assessment.bni.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.SortedMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    @JsonProperty("user_data")
    private UserDto userData;

    @JsonProperty("user_settings")
    private SortedMap<String, String> userSettings;
}
