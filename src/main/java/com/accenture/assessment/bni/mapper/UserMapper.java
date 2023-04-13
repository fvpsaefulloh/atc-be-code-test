package com.accenture.assessment.bni.mapper;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.dto.UserDto;
import com.accenture.assessment.bni.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mappings({@Mapping(source = "ssn", target = "ssn",
            qualifiedByName = "toSsn16CharacterWithPaddingLeftZero"),
            @Mapping(source = "lastName", target = "familyName")
    })
    User fromRequestToEntity(UserRequest userRequest);

    @Mapping(target = "lastName", source = "familyName")
    UserDto toDto(User user);
    List<UserDto> toDtoList(List<User> users);

    @Named("toSsn16CharacterWithPaddingLeftZero")
    static String toSsn16CharacterWithPaddingLeftZero(String ssn) {
        if (ssn.length() < 16) {
            return StringUtils.leftPad(ssn,16,"0");
        } else {
            return ssn;
        }
    }

}
