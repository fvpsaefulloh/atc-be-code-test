package com.accenture.assessment.bni.service;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserPagedResponse;
import com.accenture.assessment.bni.controller.response.UserResponse;
import com.accenture.assessment.bni.dto.UserDto;
import com.accenture.assessment.bni.entity.QUser;
import com.accenture.assessment.bni.entity.User;
import com.accenture.assessment.bni.entity.UserSetting;
import com.accenture.assessment.bni.enums.UserSettingsKeyEnum;
import com.accenture.assessment.bni.mapper.UserMapper;
import com.accenture.assessment.bni.mapper.UserMapperImpl;
import com.accenture.assessment.bni.repository.UserRepository;
import com.accenture.assessment.bni.repository.custom.UserCustomRepository;
import com.accenture.assessment.bni.service.impl.UserServiceImpl;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private final QUser qUser = QUser.user;

    @BeforeEach
    public void init() {
        this.userService = new UserServiceImpl(repository, userMapper, userSettingService, userCustomRepository);
        userRequest = new UserRequest();
        userRequest.setSsn("1123");
        userRequest.setFirstName("TestFirstName");
        userRequest.setBirthDate(LocalDate.now().minusYears(19));
        userRequest.setLastName("LastName");

        user = userMapper.fromRequestToEntity(userRequest);
        user.setId(1L);
    }

    @Test
    public void whenSaveUserWithValidDataThenDataSavedSuccessfully() {
        when(repository.save(any())).thenReturn(user);
        when(userSettingService.save(any())).thenReturn(getUserSettingList());

        UserResponse userResponse = userService.save(userRequest);
        UserDto userDto = userResponse.getUserData();
        assertEquals(userRequest.getBirthDate(), userDto.getBirthDate());
        assertEquals(userRequest.getFirstName(), userDto.getFirstName());
        assertEquals(userRequest.getLastName(), userDto.getLastName());
        assertEquals(16, userDto.getSsn().length());
    }

    @Test
    public void whenGetUserByIdThenReturnUserBasedOnId() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isTrue()).and(qUser.id.eq(user.getId()));
        when(repository.findOne(builder)).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.getUser(user.getId());
        UserDto userDto = userResponse.getUserData();
        assertEquals(userRequest.getBirthDate(), userDto.getBirthDate());
        assertEquals(userRequest.getFirstName(), userDto.getFirstName());
        assertEquals(userRequest.getLastName(), userDto.getLastName());
        assertEquals(16, userDto.getSsn().length());
    }

    @Test
    public void whenUpdateUSerWithValidDataThenDataUpdatedSuccessfully() {
        when(repository.save(any())).thenReturn(user);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isTrue()).and(qUser.id.eq(user.getId()));
        when(repository.findOne(builder)).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.update(userRequest, user.getId());
        UserDto userDto = userResponse.getUserData();
        assertEquals(userRequest.getBirthDate(), userDto.getBirthDate());
        assertEquals(userRequest.getFirstName(), userDto.getFirstName());
        assertEquals(userRequest.getLastName(), userDto.getLastName());
        assertEquals(16, userDto.getSsn().length());
    }

    @Test
    public void whenActivateInActiveUserThenUserActivated() {
        User inActiveUser = new User();
        inActiveUser.setIsActive(false);
        inActiveUser.setDeletedTime(ZonedDateTime.now());
        inActiveUser.setFirstName("Firstname");
        inActiveUser.setFamilyName("FamilyName");
        inActiveUser.setSsn("0000001234554321");
        inActiveUser.setUpdatedTime(ZonedDateTime.now().minusHours(4));
        inActiveUser.setCreatedTime(ZonedDateTime.now().minusDays(1));
        inActiveUser.setCreatedBy("SYSTEM");
        inActiveUser.setId(3L);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isFalse()).and(qUser.id.eq(inActiveUser.getId()));
        when(repository.findOne(builder)).thenReturn(Optional.of(inActiveUser));

        UserResponse userResponse = userService.activateUser(inActiveUser.getId());
        UserDto userDto = userResponse.getUserData();
        assertEquals(true, userDto.getIsActive());

    }

    @Test
    public void whenGetPagedUserThenUserPagedDataReturnedBasedOnMaxRecordsAndOffset() {

        User dummyUser = new User();
        dummyUser.setIsActive(false);
        dummyUser.setDeletedTime(ZonedDateTime.now());
        dummyUser.setFirstName("Firstname");
        dummyUser.setFamilyName("FamilyName");
        dummyUser.setSsn("0000001234554321");
        dummyUser.setUpdatedTime(ZonedDateTime.now().minusHours(4));
        dummyUser.setCreatedTime(ZonedDateTime.now().minusDays(1));
        dummyUser.setCreatedBy("SYSTEM");
        dummyUser.setId(3L);

        List<User> users = new ArrayList<>();

        users.add(dummyUser);
        users.add(user);

        Integer maxRecords = 10;
        Integer offset = 0;

        when(userCustomRepository.getActiveUser(maxRecords,offset)).thenReturn(users);

        UserPagedResponse userResponse = userService.getPagedActiveUser(maxRecords,offset);
        assertEquals(maxRecords, userResponse.getMaxRecords());
        assertEquals(offset, userResponse.getOffset());

        assertEquals(2, userResponse.getUserData().size());
    }

    @Test
    public void whenDeleteActiveUserThenUserSuccessfullyDeleted() {

        User dummyUser = new User();
        dummyUser.setIsActive(true);
        dummyUser.setDeletedTime(ZonedDateTime.now());
        dummyUser.setFirstName("Firstname");
        dummyUser.setFamilyName("FamilyName");
        dummyUser.setSsn("0000001234554321");
        dummyUser.setUpdatedTime(ZonedDateTime.now().minusHours(4));
        dummyUser.setCreatedTime(ZonedDateTime.now().minusDays(1));
        dummyUser.setCreatedBy("SYSTEM");
        dummyUser.setId(1L);

        Long id = 1L;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isTrue()).and(qUser.id.eq(id));
        when(repository.findOne(builder)).thenReturn(Optional.of(dummyUser));

        userService.delete(id);

        assertEquals(false, dummyUser.getIsActive());
        assertEquals(true, dummyUser.getDeletedTime() != null);
    }

    @Test
    public void whenUpdateUserSettingsThenUserSettingsUpdatedSuccessfully() {
        Long id = 1L;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isTrue()).and(qUser.id.eq(id));
        when(repository.findOne(builder)).thenReturn(Optional.of(user));
        ZonedDateTime current = ZonedDateTime.now();


        UserResponse userResponse = userService.updateSettings(getUserSettingsForUpdate(), id);

        assertEquals(true, current.isBefore(userResponse.getUserData().getUpdatedTime()));
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

    private List<Map<String, Object>> getUserSettingsForUpdate() {
        List<Map<String, Object>> userSettingsList = new ArrayList<>();
        Arrays.asList(UserSettingsKeyEnum.values()).forEach(
            (val) -> {
                Map<String, Object> map = new HashMap<>();
                map.put(val.getName(), val.getDefaultValue());
                userSettingsList.add(map);
            }
        );
        return userSettingsList;
    }
}
