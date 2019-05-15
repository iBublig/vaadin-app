package ru.bublig.testtask.model;

import java.time.LocalDateTime;

public class Recipe {
    private String description;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime createData;
    private LocalDateTime validity;
    private RecipeStatus status;
}
