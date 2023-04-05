package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee save(Employee employee);

    List<Employee> getAll();

    Optional<Employee> getById(Long id);

    void delete(Employee employee);
}
