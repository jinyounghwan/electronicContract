package com.samsung.framework.controller;

import lombok.Getter;

@Getter
public class SampleVO {
    private String seq;
    private String name;
    private String email;
    private String[] interests;
    private String receiveNotification;

    public SampleVO(String seq, String name, String email, String[] interests, String receiveNotification) {
        this.seq = seq;
        this.name = name;
        this.email = email;
        this.interests = interests;
        this.receiveNotification = receiveNotification;
    }
}

