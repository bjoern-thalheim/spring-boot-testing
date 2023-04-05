package net.javaguides.springboottesting.repository;

import jakarta.inject.Inject;
import net.javaguides.springboottesting.AbstractPostgreSQLTestContainers;
import net.javaguides.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryIT extends AbstractPostgreSQLTestContainers {
    @Inject
    private EmployeeRepository repository;
    private Employee employee;

    @BeforeEach
    public void createStandardEmployeeInDb() {
        employee = Employee.builder().firstName("Björn").lastName("Thalheim").email("bjoern.thalheim@adesso.de").build();
        // this call actually manipulates the input argument, so we put in a clone (defensive copy)
        repository.save(employee);
    }

    @Test
    @DisplayName("When employee is saved, it's attributes shall all be correctly saved to and loaded from the DB.")
    public void testSaveEmployee() {
        // then
        List<Employee> employees = repository.findAll();
        assertThat(employees).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").containsExactly(employee);
    }

    @Test
    @DisplayName("When several employees are saved from the repo, they should be returned by the findAll() repository method.")
    public void testFindAllEmployees() {
        // given
        Employee employee1 = Employee.builder().firstName("John").lastName("Doe").email("john.doe@adesso.de").build();
        Employee employee2 = Employee.builder().firstName("Jane").lastName("Doe").email("jane.doe@adesso.de").build();
        Employee employee3 = Employee.builder().firstName("Brat").lastName("Pitt").email("brat.pitt@adesso.de").build();
        // when
        // this call actually manipulates the input argument, so we put in a clone (defensive copy)
        repository.save(employee1.toBuilder().build());
        repository.save(employee2.toBuilder().build());
        repository.save(employee3.toBuilder().build());
        // then
        List<Employee> employees = repository.findAll();
        assertThat(employees).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").containsExactly(employee, employee1, employee2, employee3);
    }

    @Test
    @DisplayName("When an employee is contained in the DB, it can be retrieved by ID")
    public void testGetEmployeeById() {
        // when
        Optional<Employee> fetchedOptionalEmployee = repository.findById(employee.getId());
        // then
        assertThat(fetchedOptionalEmployee).isPresent();
        Employee fetchedEmployee = fetchedOptionalEmployee.get();
        assertThat(fetchedEmployee).usingRecursiveComparison().ignoringFields("id").isEqualTo(employee);
    }

    @Test
    @DisplayName("When an Employee is contained in the DB, it can be retrieved by Email.")
    public void testGetEmployeeByEmail() {
        // when
        Optional<Employee> fetchedOptionalEmployee = repository.findByEmail(employee.getEmail());
        // then
        assertThat(fetchedOptionalEmployee).isPresent();
        Employee fetchedEmployee = fetchedOptionalEmployee.get();
        assertThat(fetchedEmployee).usingRecursiveComparison().ignoringFields("id").isEqualTo(employee);
    }

    @Test
    @DisplayName("When an employee is deleted from the DB, it should not be findable.")
    public void testDeleteEmployee() {
        // check precondition
        assertThat(repository.findAll()).hasSize(1);
        // when
        repository.delete(employee);
        // then
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("When an employee is in the DB, it shall be found by a part of its last name.")
    public void testFindEmployeeByNamePart() {
        // when
        Optional<Employee> fetchedEmployeeOptional = repository.findByLastNameContains(employee.getLastName().substring(3, 5));
        // then
        assertThat(fetchedEmployeeOptional).isPresent();
        Employee fetchedEmployee = fetchedEmployeeOptional.get();
        assertThat(fetchedEmployee).usingRecursiveComparison().ignoringFields("id").isEqualTo(employee);
    }
}