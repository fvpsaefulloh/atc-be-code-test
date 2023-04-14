package com.accenture.assessment.bni.service;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserResponse;
import com.accenture.assessment.bni.dto.UserDto;
import com.accenture.assessment.bni.entity.User;
import com.accenture.assessment.bni.entity.UserSetting;
import com.accenture.assessment.bni.enums.UserSettingsKeyEnum;
import com.accenture.assessment.bni.mapper.UserMapper;
import com.accenture.assessment.bni.mapper.UserMapperImpl;
import com.accenture.assessment.bni.repository.UserRepository;
import com.accenture.assessment.bni.repository.custom.UserCustomRepository;
import com.accenture.assessment.bni.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        UserMapperImpl.class
})
public class UserServiceTests {
    @Mock
    private UserRepository repository;

    @Mock
    private UserSettingService userSettingService;

    @Mock
    private UserCustomRepository userCustomRepository;

    @Autowired
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private UserService userService;
    private User user;
    private UserRequest userRequest;


    @BeforeEach
    public void init() {
        this.userService = new UserServiceImpl(repository, userMapper, userSettingService, userCustomRepository);
        userRequest = new UserRequest();
        userRequest.setSsn("1123");
        userRequest.setFirstName("TestFirstName");
        userRequest.setBirthDate(LocalDate.now().minusYears(19));
        userRequest.setLastName("LastName");

        user = userMapper.fromRequestToEntity(userRequest);
    }

    @Test
    public void whenSaveUSerWithValidDataThenDataSavedSuccessfully() {
        when(repository.save(any())).thenReturn(user);
        when(userSettingService.save(any())).thenReturn(getUserSettingList());

        UserResponse userResponse = userService.save(userRequest);
        UserDto userDto = userResponse.getUserData();
        Assertions.assertEquals(userRequest.getBirthDate(), userDto.getBirthDate());
        Assertions.assertEquals(userRequest.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(userRequest.getLastName(), userDto.getLastName());
        Assertions.assertEquals(16, userDto.getSsn().length());

    }

    private List<UserSetting> getUserSettingList() {
        List<UserSetting> userSettingList = new ArrayList<>();
        Arrays.asList(UserSettingsKeyEnum.values()).forEach(
                (val) -> {
                    UserSetting userSetting = new UserSetting();
                    userSetting.setKey(val.getName());
                    userSetting.setValue(val.getDefaultValue());
                }
        );
        return userSettingList;
    }
}
