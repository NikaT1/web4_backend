package com.project.web4.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class DataDTO {
    @NotEmpty(message = "x должен быть задан ")
    private Double x;
    @NotEmpty(message = "y должен быть задан")
    private Double y;
    @Positive(message = "r не может быть отрицательным")
    private Double r;
}
