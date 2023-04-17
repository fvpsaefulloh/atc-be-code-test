package com.accenture.assessment.bni.service.impl;

import com.accenture.assessment.bni.configuration.InvalidValueException;
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
import java.util.regex.Pattern;
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
    @Transactional
    public List<UserSetting> update(List<Map<String, Object>> settings, Long userId) {
        List<UserSetting> userSettings = getByUserId(userId);
        settings.forEach(
                (map) -> {
                    map.forEach((k, v) -> {
                        validateValue(k, v);
                        for (UserSetting userSetting : userSettings) {
                            if(k.equalsIgnoreCase(userSetting.getKey())) {
                                if (v != null) {
                                    userSetting.setValue(String.valueOf(v));
                                    repository.save(userSetting);
                                } else {
                                    throw new InvalidValueException(k, v.toString());
                                }
                            }
                        }
                    });
                }
        );

        return userSettings;
    }

    private void validateValue(String k, Object v) {
        UserSettingsKeyEnum userSettingsKeyEnum = UserSettingsKeyEnum.of(k);
        if (!userSettingsKeyEnum.equals(UserSettingsKeyEnum.WIDGET_ORDER) && userSettingsKeyEnum.getClassType().equals(Boolean.class)) {
            if (!v.toString().equalsIgnoreCase(Boolean.FALSE.toString()) && !v.toString().equalsIgnoreCase(Boolean.TRUE.toString()) ) {
                throw new InvalidValueException(k, v.toString());
            }
        } else {
            if (!Pattern.matches("^[1-5](,[1-5])*$", v.toString())) {
                throw new InvalidValueException(k, v.toString());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSetting> getByUserId(Long userId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qUserSetting.userId.eq(userId));
        booleanBuilder.and(qUserSetting.user.isActive.isTrue());
        List<UserSetting> userSettings = (List<UserSetting>) repository.findAll(booleanBuilder);
        return userSettings;
    }
    
}
