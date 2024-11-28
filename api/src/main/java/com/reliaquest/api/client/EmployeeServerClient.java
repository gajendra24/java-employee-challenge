package com.reliaquest.api.client;

import com.reliaquest.api.client.dto.DeleteEmployeeResponse;
import com.reliaquest.api.client.dto.EmployeeServiceResponse;
import com.reliaquest.api.client.dto.EmployeesServiceResponse;
import com.reliaquest.api.controller.dto.request.EmployeeCreateRequest;
import com.reliaquest.api.controller.dto.request.EmployeeDeleteRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "employee-mock", url = "http://localhost:8112")
public interface EmployeeServerClient {
    @GetMapping("/api/v1/employee")
    EmployeesServiceResponse getAllEmployees();

    @GetMapping("/api/v1/employee/{id}")
    EmployeeServiceResponse getEmployeeById(@PathVariable String id);

    @PostMapping("/api/v1/employee")
    EmployeeServiceResponse createEmployee(@RequestBody EmployeeCreateRequest request);

    @DeleteMapping("/api/v1/employee")
    DeleteEmployeeResponse deleteEmployee(@RequestBody EmployeeDeleteRequest request);
}
