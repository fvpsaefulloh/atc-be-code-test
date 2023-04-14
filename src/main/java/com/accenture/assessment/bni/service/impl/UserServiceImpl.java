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
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserSettingService userSettingService;
    private final UserCustomRepository userCustomRepository;
    private final static QUser qUser = QUser.user;

    @Override
    @Transactional
    public UserResponse save(UserRequest request) {
        User user = mapper.fromRequestToEntity(request);
        checkIfSsnAlreadyExist(user.getSsn());
        user.setCreatedTime(ZonedDateTime.now());
        user.setUpdatedTime(ZonedDateTime.now());
        repository.save(user);
        List<UserSetting> userSettings =  userSettingService.save(user.getId());
        UserResponse response = new UserResponse(mapper.toDto(user), convertUserSettingsToMap(userSettings));
        return response;
    }

    private void checkIfSsnAlreadyExist(String ssn) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.ssn.eq(ssn));

        repository.findOne(builder)
                .ifPresent(
                    s -> {
                        throw new DataIntegrityViolationException(String
                                .format("Record with unique value %s already exists in the system", ssn));
                    });
    }

    @Override
    public UserResponse update(UserRequest request, Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isTrue()).and(qUser.id.eq(id));
        User user = getOne(builder, id);
        user.setBirthDate(request.getBirthDate());
        user.setFirstName(request.getFirstName());
        user.setFamilyName(request.getLastName());
        user.setUpdatedTime(ZonedDateTime.now());
        repository.save(user);
        return getUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        User user = getActiveUserById(id);
        return getUserResponse(user) ;
    }

    @Override
    @Transactional(readOnly = true)
    public UserPagedResponse getPagedActiveUser(Integer maxRecords, Integer offset) {
        List<UserDto> userDtos = mapper.toDtoList(userCustomRepository.getActiveUser(maxRecords, offset));
        return new UserPagedResponse(userDtos, maxRecords, offset);
    }

    @Override
    @Transactional
    public UserResponse activateUser(Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isFalse());
        builder.and(qUser.id.eq(id));

        User user = getOne(builder, id);
        user.setIsActive(true);
        user.setUpdatedTime(ZonedDateTime.now());
        user.setDeletedTime(null);
        return getUserResponse(user) ;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = getActiveUserById(id);
        user.setDeletedTime(ZonedDateTime.now());
        user.setIsActive(false);
        repository.save(user);
    }


    @Override
    public List<Map<String, String>> convertUserSettingsToMap(List<UserSetting> userSettings) {
        List<UserSetting> orderedUserSettings = userSettings.stream()
                .sorted(Comparator.comparing(a -> a.getKey()))
                .collect(Collectors.toList());

        List<Map<String, String>> mapList = new ArrayList<>();

        orderedUserSettings.forEach(
                (data) -> {
                    Map<String, String> map = new HashMap<>();
                    map.put(data.getKey(), data.getValue());
                    mapList.add(map);
                }
        );

        return mapList;
    }

    @Override
    public UserResponse updateSettings(List<Map<String, Object>> settings, Long userId) {
        User user = getActiveUserById(userId);
        List<UserSetting> userSettings = userSettingService.update(settings, userId);
        user.setUpdatedTime(ZonedDateTime.now());
        repository.save(user);
        return getUserResponse(user, userSettings);
    }

    private User getOne(BooleanBuilder builder, Long id) {
        User user = repository.findOne(builder)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find resource with id " + id));
        return user;
    }

    private UserResponse getUserResponse(User user) {
        List<UserSetting> userSettings = userSettingService.getByUserId(user.getId());
        UserResponse userResponse = getUserResponse(user, userSettings);
        return userResponse;
    }

    private UserResponse getUserResponse(User user, List<UserSetting> userSettings) {
        UserResponse userResponse = new UserResponse(mapper.toDto(user), convertUserSettingsToMap(userSettings));
        return userResponse;
    }

    private User getActiveUserById(Long id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isTrue()).and(qUser.id.eq(id));
        User user = getOne(builder, id);
        return user;
    }

}
