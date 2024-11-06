package com.kodality.vacation.employee;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.util.List;

@Singleton
@Requires(beans = DataSource.class)
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Get.
    public List<Employee> getEmployees() {
        return employeeRepository.getEmployees();
    }

    // Get employee by id
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    // Update remaining vacation days
    public void updateRemainingVacationDays(Long id, int newRemainingDays) {
        employeeRepository.updateRemainingVacationDays(id, newRemainingDays);
    }
}
