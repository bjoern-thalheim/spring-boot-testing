package net.javaguides.springboottesting.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        return service.save(employee);
    }

    @GetMapping
    public List<Employee> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") long id) {
        return service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable("id") long id, @RequestBody Employee employee) {
        Employee entity = getById(id);
        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setEmail(employee.getEmail());
        return service.save(entity);
    }

    @DeleteMapping("/{id}")
    public Employee delete(@PathVariable("id") long id) {
        Employee employee = getById(id);
        service.delete(employee);
        return employee;
    }
}
