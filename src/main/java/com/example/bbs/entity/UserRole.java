package com.example.bbs.entity;

import lombok.Getter;

//상수 자료형이므로 @Setter없이 @Getter만 사용가능
@Getter // 열거 자료형(enum)으로 작성
public enum UserRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;

}
