package net.javaguides.springboottesting.repository;

import jakarta.inject.Inject;
import net.javaguides.springboottesting.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Inject
    private EmployeeRepository repository;

    @Test
    @DisplayName("When employee is saved, it's attributes shall all be correctly saved to and loaded from the DB.")
    public void testSaveEmployee() {
        // given
        Employee employee = Employee.builder().firstName("Björn").lastName("Thalheim").email("bjoern.thalheim@adesso.de").build();
        // when
        // this call actually manipulates the input argument, so we put in a clone (defensive copy)
        Employee savedEmployee = repository.save(employee.toBuilder().build());
        // then
        assertThat(savedEmployee.getId()).isGreaterThan(0L);
        assertThat(repository.findAll()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").containsExactly(employee);
    }

}