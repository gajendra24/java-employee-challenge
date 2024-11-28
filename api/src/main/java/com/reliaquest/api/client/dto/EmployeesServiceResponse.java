package com.reliaquest.api.client.dto;

import com.reliaquest.api.controller.dto.response.EmployeeResponse;
import java.util.List;
import lombok.Data;

@Data
public class EmployeesServiceResponse {
    List<EmployeeResponse> data;
}
