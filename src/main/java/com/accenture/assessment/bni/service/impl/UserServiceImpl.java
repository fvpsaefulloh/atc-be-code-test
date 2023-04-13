package com.accenture.assessment.bni.service.impl;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserPagedResponse;
import com.accenture.assessment.bni.controller.response.UserResponse;
import com.accenture.assessment.bni.dto.UserDto;
import com.accenture.assessment.bni.entity.QUser;
import com.accenture.assessment.bni.entity.User;
import com.accenture.assessment.bni.entity.UserSetting;
import com.accenture.assessment.bni.mapper.UserMapper;
import com.accenture.assessment.bni.repository.UserRepository;
import com.accenture.assessment.bni.repository.custom.UserCustomRepository;
import com.accenture.assessment.bni.service.UserService;
import com.accenture.assessment.bni.service.UserSettingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserSettingService userSettingService;
    private final UserCustomRepository userCustomRepository;
    private final static QUser qUser = QUser.user;

    @Override
    public UserResponse save(UserRequest request) {
        User user = mapper.fromRequestToEntity(request);
        user.setCreatedTime(ZonedDateTime.now());
        user.setUpdatedTime(ZonedDateTime.now());
        repository.save(user);

        List<UserSetting> userSettings = userSettingService.save(user.getId());
        UserResponse userResponse = new UserResponse(mapper.toDto(user), convertUserSettingsToMap(userSettings));
        return userResponse;
    }

    @Override
    public UserResponse getUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id tidak ditemukan"));
        List<UserSetting> userSettings = userSettingService.getByUserId(user.getId());
        return new UserResponse(mapper.toDto(user),
                convertUserSettingsToMap(userSettings));
    }

    @Override
    public UserPagedResponse getActiveUser(Integer maxRecords, Integer offset) {
        List<UserDto> userDtos = mapper.toDtoList(userCustomRepository.getActiveUser(maxRecords, offset));
        return new UserPagedResponse(userDtos, maxRecords, offset);
    }


    private SortedMap<String, String> convertUserSettingsToMap(List<UserSetting> userSettings) {
        SortedMap<String, String> sortedMap = new TreeMap<>();
        userSettings.forEach(a -> sortedMap.put(a.getKey(), a.getValue()));
        return sortedMap;
    }
}
