package com.accenture.assessment.bni.controller.response;

import com.accenture.assessment.bni.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserPagedResponse {
    @JsonProperty("user_data")
    private List<UserDto> userData;
    @JsonProperty("max_records")
    private Integer maxRecords;
    private Integer offset;
}
