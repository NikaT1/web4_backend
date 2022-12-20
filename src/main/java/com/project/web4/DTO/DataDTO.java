package com.project.web4.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class DataDTO {
    @NotNull(message = "x должен быть задан ")
    private Double x;
    @NotNull(message = "y должен быть задан")
    private Double y;
    @Positive(message = "r не может быть отрицательным")
    private Double r;
}
