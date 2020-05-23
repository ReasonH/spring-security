package com.reason.springsec.domain;

import lombok.Getter;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
    private String roleName;

    UserRole(String roleName){
        this.roleName = roleName;
    };

    public boolean isCorrectName(String name){
        return name.equalsIgnoreCase(this.roleName); // 대소문자 무시한 비교
    }

    public static UserRole getRoleByName(String roleName){
        return Stream.of(UserRole.values())
                .filter(r -> r.isCorrectName(roleName))
                .findFirst().orElseThrow(() -> new NoSuchElementException("검색된 권한 없음"));
    }
}
