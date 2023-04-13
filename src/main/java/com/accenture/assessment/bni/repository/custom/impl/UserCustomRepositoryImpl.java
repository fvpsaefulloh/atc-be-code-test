package com.accenture.assessment.bni.repository.custom.impl;

import com.accenture.assessment.bni.entity.QUser;
import com.accenture.assessment.bni.entity.User;
import com.accenture.assessment.bni.repository.custom.UserCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {
    private final static QUser qUser = QUser.user;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<User> getActiveUser(Integer maxRecords, Integer offset) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qUser.isActive.isTrue());

        JPAQuery<User> jpaQuery = new JPAQuery<>(entityManager);

        return jpaQuery.from(qUser)
                .where(builder)
                .limit(maxRecords)
                .offset(offset)
                .fetch();
    }
}
