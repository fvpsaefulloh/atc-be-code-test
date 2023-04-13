package com.accenture.assessment.bni.repository;

import com.accenture.assessment.bni.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long>,
        QuerydslPredicateExecutor<UserSetting> {
}
