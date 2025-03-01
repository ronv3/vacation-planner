package com.org.vacation.request;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class VacationRequestRepository {
    private final JdbcTemplate jdbcTemplate;

    @Inject
    public VacationRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get.
    public List<VacationRequest> getVacationRequests() {
        String sql = """
                    SELECT id, employee_id, vacation_start, vacation_end, submitted_at, comment
                    FROM vacation_request
                """;
        return jdbcTemplate.query(sql, new VacationRequestRepository.VacationRequestRowMapper());
    }

    // Create.
    public Long create(VacationRequest vacationRequest) {
        String sql = """
                    INSERT INTO vacation_request (employee_id, vacation_start, vacation_end, comment, submitted_at)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING id;
                """;
        return jdbcTemplate.queryForObject(sql,
                Long.class,
                vacationRequest.getEmployeeId(),
                vacationRequest.getVacationStart(),
                vacationRequest.getVacationEnd(),
                vacationRequest.getComment(),
                vacationRequest.getSubmittedAt()
        );
    }

    // Update.
    public void updateVacationRequest(long id, VacationRequest vacationRequest) {
        String sql = """
                UPDATE vacation_request
                SET employee_id = ?, vacation_start = ?, vacation_end = ?, comment = ?, submitted_at = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                vacationRequest.getEmployeeId(),
                vacationRequest.getVacationStart(),
                vacationRequest.getVacationEnd(),
                vacationRequest.getComment(),
                vacationRequest.getSubmittedAt(),
                id
        );
    }

    // Delete.
    public void deleteById(Long id) {
        String sql = "DELETE FROM vacation_request WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public VacationRequest getVacationRequestById(long id) {
        String sql = """
                    SELECT id, employee_id, vacation_start, vacation_end, submitted_at, comment
                    FROM vacation_request
                    WHERE id = ?
                """;
        return jdbcTemplate.queryForObject(sql, new VacationRequestRepository.VacationRequestRowMapper(), id);
    }

    public List<VacationRequest> getFiltered(String name, LocalDate startDate, LocalDate endDate) {
        // Start with a base SQL query
        String sql = """
                SELECT vr.id, vr.employee_id, vr.vacation_start, vr.vacation_end, vr.submitted_at, vr.comment, e.employee_name
                FROM vacation_request vr
                JOIN employees e ON e.id = vr.employee_id
                WHERE 1=1
            """;

        // List to store query parameters dynamically
        List<Object> params = new ArrayList<>();

        // Add conditions based on the input parameters
        if (!name.isEmpty()) {
            sql += " AND e.employee_name LIKE ?";
            params.add("%" + name + "%"); // Add the parameter for name filter
        }

        if (startDate != null) {
            sql += " AND vr.vacation_start >= ?";
            params.add(startDate); // Add the parameter for start date filter
        }

        if (endDate != null) {
            sql += " AND vr.vacation_end <= ?";
            params.add(endDate); // Add the parameter for end date filter
        }

        // Convert the list to an array to pass to the query method
        Object[] paramArray = params.toArray();

        // Execute the query with the parameters
        return jdbcTemplate.query(sql, paramArray, new VacationRequestRowMapper());
    }

    // Row mapper
    private static class VacationRequestRowMapper implements RowMapper<VacationRequest> {
        @Override
        public VacationRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VacationRequest()
                    .setId(rs.getLong("id"))
                    .setEmployeeId(rs.getLong("employee_id"))
                    .setVacationStart(rs.getObject("vacation_start", LocalDate.class))
                    .setVacationEnd(rs.getObject("vacation_end", LocalDate.class))
                    .setComment(rs.getString("comment"))
                    .setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
        }
    }
}
