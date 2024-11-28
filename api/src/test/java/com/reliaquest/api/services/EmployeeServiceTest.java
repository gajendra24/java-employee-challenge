package com.reliaquest.api.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.reliaquest.api.client.EmployeeServerClient;
import com.reliaquest.api.client.dto.DeleteEmployeeResponse;
import com.reliaquest.api.client.dto.EmployeeServiceResponse;
import com.reliaquest.api.client.dto.EmployeesServiceResponse;
import com.reliaquest.api.controller.dto.request.EmployeeCreateRequest;
import com.reliaquest.api.controller.dto.request.EmployeeDeleteRequest;
import com.reliaquest.api.controller.dto.response.EmployeeResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeServiceTest {
    @Mock
    EmployeeServerClient employeeServerClient;

    @InjectMocks
    EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        // Initialize the mocks before each test method
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllEmployeesTest() {
        // Given
        EmployeesServiceResponse employees = mockEmployeeData();
        when(employeeServerClient.getAllEmployees()).thenReturn(employees);

        // When
        List<EmployeeResponse> response = employeeService.getAllEmployees();

        // Then
        for (int id = 0; id < 50; id++) {
            EmployeeResponse emp = response.get(id);
            Assertions.assertEquals("Gajendra" + id, emp.getEmployeeName());
            Assertions.assertEquals(1000 + id, emp.getEmployeeSalary());
            Assertions.assertEquals(30 + id, emp.getEmployeeAge());
        }
    }

    @Test
    public void getAllEmployeesErrorTest() {
        // Given
        doThrow(new RuntimeException("Server error")).when(employeeServerClient).getAllEmployees();

        // When
        List<EmployeeResponse> response = employeeService.getAllEmployees();

        // Then
        Assertions.assertNull(response);
    }

    @Test
    public void getAllEmployeeByIdTest() {
        // Given
        EmployeeResponse employeeResponse = new EmployeeResponse("1000", "Gajendra", 1000, 30, "SSE", "test@gmal.com");
        EmployeeServiceResponse employee = new EmployeeServiceResponse();
        employee.setData(employeeResponse);
        when(employeeServerClient.getEmployeeById("1000")).thenReturn(employee);

        // When
        EmployeeResponse response = employeeService.getEmployeeById("1000");

        // Then
        Assertions.assertEquals("Gajendra", response.getEmployeeName());
        Assertions.assertEquals(1000, response.getEmployeeSalary());
        Assertions.assertEquals(30, response.getEmployeeAge());
    }

    @Test
    public void getAllEmployeeByIdErrorTest() {
        // Given
        doThrow(new RuntimeException("Server error")).when(employeeServerClient).getEmployeeById(any());

        // When
        EmployeeResponse response = employeeService.getEmployeeById("1000");

        // Then
        Assertions.assertNull(response);
    }

    @Test
    public void getAllEmployeesBySearchStringTest() {
        // Given
        EmployeesServiceResponse employees = mockEmployeeData();
        when(employeeServerClient.getAllEmployees()).thenReturn(employees);

        // When
        List<EmployeeResponse> response = employeeService.getEmployeesByNameSearch("Gajendra1");

        // Then
        EmployeeResponse emp = response.get(0);
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("Gajendra1", emp.getEmployeeName());
        Assertions.assertEquals(1001, emp.getEmployeeSalary());
        Assertions.assertEquals(31, emp.getEmployeeAge());
    }

    @Test
    public void getAllEmployeesBySearchStringErrorTest() {
        // Given
        doThrow(new RuntimeException("Server error")).when(employeeServerClient).getAllEmployees();

        // When
        List<EmployeeResponse> response = employeeService.getEmployeesByNameSearch("Gajendra1");

        // Then
        Assertions.assertNull(response);
    }

    @Test
    public void getHighestSalaryOfEmployeesTest() {
        // Given
        EmployeesServiceResponse employees = mockEmployeeData();
        when(employeeServerClient.getAllEmployees()).thenReturn(employees);

        // when
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

        // then
        Assertions.assertEquals(1049, highestSalary);
    }

    @Test
    public void getHighestSalaryOfEmployeesErrorTest() {
        // Given
        doThrow(new RuntimeException("Server error")).when(employeeServerClient).getAllEmployees();

        // When
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

        // Then
        Assertions.assertNull(highestSalary);
    }

    @Test
    public void getTopTenHighestEarningEmployeeNamesTest() {
        // Given
        EmployeesServiceResponse employees = mockEmployeeData();
        when(employeeServerClient.getAllEmployees()).thenReturn(employees);

        // When
        List<String> top10Employee = employeeService.getTopTenHighestEarningEmployeeNames();

        // Then
        Assertions.assertEquals(10, top10Employee.size());
        int topId = 49;
        for (String empName : top10Employee) {
            Assertions.assertEquals("Gajendra" + topId--, empName);
        }
    }

    @Test
    public void getTopTenHighestEarningEmployeeNamesErrorTest() {
        // Given
        doThrow(new RuntimeException("Server error")).when(employeeServerClient).getAllEmployees();

        // When
        List<String> top10Employee = employeeService.getTopTenHighestEarningEmployeeNames();

        // Then
        Assertions.assertNull(top10Employee);
    }

    @Test
    public void createEmployeeTest() {
        // Given
        EmployeeCreateRequest newEmp = new EmployeeCreateRequest("Gajendra", 1000, 30, "SSE");
        EmployeeResponse employeeResponse = new EmployeeResponse("1000", "Gajendra", 1000, 30, "SSE", "test@gmal.com");
        EmployeeServiceResponse created = new EmployeeServiceResponse();
        created.setData(employeeResponse);

        // When
        when(employeeServerClient.createEmployee(newEmp)).thenReturn(created);

        // Then
        Assertions.assertEquals("Gajendra", created.getData().getEmployeeName());
    }

    @Test
    public void deleteEmployeeTest() {
        // Given
        EmployeeResponse employeeResponse = new EmployeeResponse("1000", "Gajendra", 1000, 30, "SSE", "test@gmal.com");
        EmployeeServiceResponse employee = new EmployeeServiceResponse();
        employee.setData(employeeResponse);

        // When
        when(employeeServerClient.getEmployeeById("1000")).thenReturn(employee);

        DeleteEmployeeResponse deleteEmployeeResponse = new DeleteEmployeeResponse();
        deleteEmployeeResponse.setData(true);
        when(employeeServerClient.deleteEmployee(
                        new EmployeeDeleteRequest(employee.getData().getEmployeeName())))
                .thenReturn(deleteEmployeeResponse);

        String res = employeeService.deleteEmployee("1000");

        // Then
        Assertions.assertEquals("Gajendra", res);
    }

    private EmployeesServiceResponse mockEmployeeData() {
        List<EmployeeResponse> emps = new ArrayList<>();

        for (int id = 0; id < 50; id++) {
            emps.add(new EmployeeResponse(
                    String.valueOf(id), "Gajendra" + id, 1000 + id, 30 + id, "SSE", "test@gmal.com"));
        }

        EmployeesServiceResponse employeesServiceResponse = new EmployeesServiceResponse();
        employeesServiceResponse.setData(emps);
        return employeesServiceResponse;
    }
}
