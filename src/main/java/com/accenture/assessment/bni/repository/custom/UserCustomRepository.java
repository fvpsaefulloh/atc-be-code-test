package com.accenture.assessment.bni.repository.custom;

import com.accenture.assessment.bni.entity.User;

import java.util.List;

public interface UserCustomRepository {
    List<User> getActiveUser(Integer maxRecords, Integer offset);
}
