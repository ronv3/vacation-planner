package com.org.vacation.employee;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Singleton
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Inject
    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get.
    public List<Employee> getEmployees() {
        String sql = "SELECT id, employee_name, remaining_vacation_days FROM employees";
        return jdbcTemplate.query(sql, new EmployeeRepository.EmployeeRowMapper());
    }

    // Get by id
    public Employee findById(long id) {
        String sql = "Select * FROM employees WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[] {id}, new EmployeeRowMapper()).getFirst();
    }

    // Update remaining vacation days
    public void updateRemainingVacationDays(Long id, int newRemainingDays) {
        String sql = "UPDATE employees SET remaining_vacation_days = ? WHERE id = ?";
        jdbcTemplate.update(sql, newRemainingDays, id);
    }

    // Row mapper
    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Employee()
                    .setId(rs.getLong("id"))
                    .setEmployeeName(rs.getString("employee_name"))
                    .setRemainingVacationDays(rs.getInt("remaining_vacation_days"));
        }
    }
}
