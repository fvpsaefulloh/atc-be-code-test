package com.accenture.assessment.bni.service;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserPagedResponse;
import com.accenture.assessment.bni.controller.response.UserResponse;
import com.accenture.assessment.bni.entity.UserSetting;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponse save(UserRequest request);
    UserResponse update(UserRequest request, Long id);
    UserResponse getUser(Long id);
    UserResponse activateUser(Long id);
    UserPagedResponse getPagedActiveUser(Integer maxRecords, Integer offset);
    void delete(Long id);
    List<Map<String, String>> convertUserSettingsToMap(List<UserSetting> userSettings);

    UserResponse updateSettings(List<Map<String, Object>> settings, Long userId);
}
