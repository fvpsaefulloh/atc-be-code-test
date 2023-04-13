package com.accenture.assessment.bni.repository;

import com.accenture.assessment.bni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
