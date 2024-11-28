package com.reliaquest.api.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeCreateRequest {
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
}
