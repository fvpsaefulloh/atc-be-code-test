package com.accenture.assessment.bni.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_user_setting")
public class UserSetting {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_name", nullable = false, length = 100)
    private String key;
    @Column(name = "val",nullable = false, length = 100)
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}
