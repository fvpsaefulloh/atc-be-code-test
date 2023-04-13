package com.accenture.assessment.bni.service.impl;

import com.accenture.assessment.bni.entity.QUserSetting;
import com.accenture.assessment.bni.entity.UserSetting;
import com.accenture.assessment.bni.enums.UserSettingsKeyEnum;
import com.accenture.assessment.bni.repository.UserSettingRepository;
import com.accenture.assessment.bni.service.UserSettingService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSettingServiceImpl implements UserSettingService {

    private final UserSettingRepository repository;
    private final QUserSetting qUserSetting = QUserSetting.userSetting;

    @Override
    @Transactional
    public List<UserSetting> save(Long userId) {
        List<UserSettingsKeyEnum> userSettingsKeyEnums = Arrays.stream(UserSettingsKeyEnum.values())
                .collect(Collectors.toList());
        List<UserSetting> userSettings = new ArrayList<>();

        userSettingsKeyEnums.forEach(
                (e) -> {
                    UserSetting userSetting = new UserSetting();
                    userSetting.setUserId(userId);
                    userSetting.setKey(e.getName());
                    userSetting.setValue(e.getDefaultValue());
                    repository.save(userSetting);
                    userSettings.add(userSetting);
                }
        );
        return userSettings;
    }

    @Override
    public List<UserSetting> update(Map<String, Object> settings, Long userId) {
        List<UserSetting> userSettings = getByUserId(userId);
        settings.forEach((k, v) -> {
            for (UserSetting userSetting : userSettings) {
                if(k.equalsIgnoreCase(userSetting.getKey())) {
                    if (v != null) {
                        userSetting.setValue(String.valueOf(v));
                        repository.save(userSetting);
                    }
                }
            }
        });
        return userSettings;
    }

    @Override
    public List<UserSetting> getByUserId(Long userId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qUserSetting.userId.eq(userId));
        List<UserSetting> userSettings = (List<UserSetting>) repository.findAll(booleanBuilder);
        return userSettings;
    }


}
