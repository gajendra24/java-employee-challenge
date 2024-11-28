package com.reliaquest.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.client.EmployeeServerClient;
import com.reliaquest.api.controller.dto.request.EmployeeCreateRequest;
import com.reliaquest.api.controller.dto.request.EmployeeDeleteRequest;
import com.reliaquest.api.controller.dto.response.EmployeeResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeServerClient employeeServerClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @return List of all employees
     */
    public List<EmployeeResponse> getAllEmployees() {
        try {
            return employeeServerClient.getAllEmployees().getData();
        } catch (Exception ex) {
            log.error("Exception happened for fetching employees. {}", ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @param id of employee
     * @return Employee info for id
     */
    public EmployeeResponse getEmployeeById(String id) {
        try {
            return employeeServerClient.getEmployeeById(id).getData();
        } catch (Exception ex) {
            log.error("Exception happened for fetching employee by id. {}", ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @param searchString is a employee name to search for
     * @return List of employees for matching searching string
     */
    public List<EmployeeResponse> getEmployeesByNameSearch(String searchString) {
        try {
            List<EmployeeResponse> employeeList =
                    employeeServerClient.getAllEmployees().getData();
            return employeeList.stream()
                    .filter(employee -> employee.getEmployeeName().equals(searchString))
                    .toList();
        } catch (Exception ex) {
            log.error("Exception happened for fetching employees for a given search string. {}", ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @return Highest salary of employee in Integer
     */
    public Integer getHighestSalaryOfEmployees() {
        try {
            List<EmployeeResponse> employeeList =
                    employeeServerClient.getAllEmployees().getData();
            Optional<EmployeeResponse> highestSalaryOfEmployee =
                    employeeList.stream().max(Comparator.comparing(EmployeeResponse::getEmployeeSalary));

            return highestSalaryOfEmployee
                    .map(EmployeeResponse::getEmployeeSalary)
                    .orElse(0);
        } catch (Exception ex) {
            log.error("Exception happened for getting highest salary of employees. {}", ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @return Top 10 employees with highest salaries
     */
    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            List<EmployeeResponse> employeeList =
                    employeeServerClient.getAllEmployees().getData();

            return employeeList.stream()
                    .sorted((e1, e2) -> e2.getEmployeeSalary() - e1.getEmployeeSalary())
                    .limit(10)
                    .map(EmployeeResponse::getEmployeeName)
                    .toList();
        } catch (Exception ex) {
            log.error("Exception happened for fetching top 10 highest paying employees. {}", ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @param request employee entity to create new employee
     * @return employee info of created employee
     */
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        try {
            return employeeServerClient.createEmployee(request).getData();
        } catch (Exception ex) {
            log.error("Exception happened for creating new employee. {}", ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @param id of employee to be deleted
     * @return name of employee deleted
     * The downstream employee mock accept name to delete employee <br>
     * First fetch employee info from id and then call mock employee api for name
     *
     */
    public String deleteEmployee(String id) {
        try {
            EmployeeResponse employee = employeeServerClient.getEmployeeById(id).getData();

            if (!employee.getEmployeeName().isEmpty()) {
                log.info("Employee Name: {} to be deleted", employee.getEmployeeName());
                if (employeeServerClient
                        .deleteEmployee(new EmployeeDeleteRequest(employee.getEmployeeName()))
                        .isData()) {
                    return employee.getEmployeeName();
                }
            }

            log.error("Could not delete an employee");
        } catch (Exception ex) {
            log.error("Exception happened for deleting employee. {}", ex.getMessage());
        }

        return null;
    }
}
