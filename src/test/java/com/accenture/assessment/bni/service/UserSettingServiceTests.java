package com.accenture.assessment.bni.service;

import com.accenture.assessment.bni.entity.QUserSetting;
import com.accenture.assessment.bni.entity.UserSetting;
import com.accenture.assessment.bni.enums.UserSettingsKeyEnum;
import com.accenture.assessment.bni.repository.UserSettingRepository;
import com.accenture.assessment.bni.service.impl.UserSettingServiceImpl;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class UserSettingServiceTests {
    @Mock
    private UserSettingRepository repository;

    private UserSettingService userSettingService;

    private final static QUserSetting qUserSetting = QUserSetting.userSetting;
    @BeforeEach
    public void init() {
        userSettingService = new UserSettingServiceImpl(repository);
    }

    @Test
    public void whenSaveUserSettingsThenUserSettingSaved() {
        Long userId = 1L;
        Arrays.asList(UserSettingsKeyEnum.values()).forEach(
                (val) -> {
                    UserSetting userSetting = new UserSetting();
                    userSetting.setKey(val.getName());
                    userSetting.setValue(val.getDefaultValue());
                    when(repository.save(userSetting)).thenReturn(userSetting);

                }
        );

        List<UserSetting> userSettings =  userSettingService.save(userId);
        assertEquals(5, userSettings.size());
    }

    @Test
    public void whenUpdateUserSettingsWithValidDataThenUserSettingUpdated() {

        Arrays.asList(UserSettingsKeyEnum.values()).forEach(
            (val) -> {
                UserSetting userSetting = new UserSetting();
                userSetting.setKey(val.getName());
                userSetting.setValue(val.getDefaultValue());
                when(repository.save(userSetting)).thenReturn(userSetting);

            }
        );

        Long userId = 1L;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qUserSetting.userId.eq(userId));
        booleanBuilder.and(qUserSetting.user.isActive.isTrue());
        when(repository.findAll(booleanBuilder)).thenReturn((getUserSettingList()));

        List<UserSetting> userSettings =  userSettingService.update(getUserSettingsForUpdate(), userId);
        assertEquals(5, userSettings.size());
    }

    private List<UserSetting> getUserSettingList() {
        List<UserSetting> userSettingList = new ArrayList<>();
        Arrays.asList(UserSettingsKeyEnum.values()).forEach(
                (val) -> {
                    UserSetting userSetting = new UserSetting();
                    userSetting.setKey(val.getName());
                    userSetting.setValue(val.getDefaultValue());
                    userSettingList.add(userSetting);
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
