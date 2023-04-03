package net.javaguides.springboottesting.repository;

import net.javaguides.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
