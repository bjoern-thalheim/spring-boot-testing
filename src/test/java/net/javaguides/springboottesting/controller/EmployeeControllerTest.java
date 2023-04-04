package net.javaguides.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @MockBean
    EmployeeService service;
    // given
    Employee employee = Employee.builder().firstName("Björn").lastName("Thalheim").email("bjoern.thalheim@adesso.de").id(1L).build();
    @Captor
    ArgumentCaptor<Employee> employeeArgumentCaptor;
    @Inject
    private MockMvc mockMvc;
    @Inject
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEmployeeShouldResultWithCorrectStatus() throws Exception {
        // given
        given(service.save(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        // when
        ResultActions result = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
        verify(service).save(employeeArgumentCaptor.capture());
        assertThat(employeeArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(employee);
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        // given
        Employee other = Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").id(2L).build();
        List<Employee> employees = List.of(employee, other);
        given(service.getAll()).willReturn(employees);
        // when
        ResultActions result = mockMvc.perform(get("/api/employees"));
        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())))
                .andExpect(jsonPath("$[0].lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$[0].firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$[0].email", is(employee.getEmail())))
                .andExpect(jsonPath("$[1].lastName", is(other.getLastName())))
                .andExpect(jsonPath("$[1].firstName", is(other.getFirstName())))
                .andExpect(jsonPath("$[1].email", is(other.getEmail())));
        verify(service).getAll();
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        // given
        given(service.getById(any(Long.class)))
                .willReturn(Optional.of(employee));
        // when
        ResultActions result = mockMvc.perform(get("/api/employees/%s".formatted(Long.toString(employee.getId()))));
        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
        verify(service).getById(eq(employee.getId()));
    }
    @Test
    public void test404ShouldBeReturnedWhenEmployeeDoesNotExist() throws Exception {
        // given
        given(service.getById(any(Long.class)))
                .willReturn(Optional.empty());
        // when
        ResultActions result = mockMvc.perform(get("/api/employees/%s".formatted("42")));
        // then
        result.andExpect(status().isNotFound());
        verify(service).getById(eq(42L));
    }

}