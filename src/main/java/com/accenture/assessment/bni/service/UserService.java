package com.accenture.assessment.bni.service;

import com.accenture.assessment.bni.controller.request.UserRequest;
import com.accenture.assessment.bni.controller.response.UserPagedResponse;
import com.accenture.assessment.bni.controller.response.UserResponse;

public interface UserService {
    UserResponse save(UserRequest request);
    UserResponse getUser(Long id);
    UserPagedResponse getActiveUser(Integer maxRecords, Integer offset);
}
