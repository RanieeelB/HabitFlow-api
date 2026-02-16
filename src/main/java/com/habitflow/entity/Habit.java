package com.habitflow.entity;

import jdk.jfr.Frequency;

import java.time.LocalDateTime;

public class Habit {

    private Long id;
    private String name;
    private String description;
    private Frequency frequency;
    private User user;
    private LocalDateTime createdAt;
}
