package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum ErrorAttributesKey {
    STATUS("status"),
    MESSAGE("error");

    private final String key;
    ErrorAttributesKey(String key) {
        this.key = key;
    }
}
