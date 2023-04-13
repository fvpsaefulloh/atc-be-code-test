package com.accenture.assessment.bni.service;

import com.accenture.assessment.bni.entity.UserSetting;

import java.util.List;
import java.util.Map;

public interface UserSettingService {
    List<UserSetting> save(Long userId);
    List<UserSetting> update(Map<String, Object> settings, Long userId);

    List<UserSetting> getByUserId(Long userId);
}
