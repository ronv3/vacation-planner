package com.kodality.vacation.request;

import com.kodality.vacation.employee.Employee;
import com.kodality.vacation.employee.EmployeeRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
public class VacationRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public VacationRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // SQL query - find all vacation requests.
    public List<VacationRequest> getVacationRequests() {
        String sql = """
                    SELECT id, employee_id, vacation_start, vacation_end, submitted_at, comment
                    FROM vacation_request
                """;
        return jdbcTemplate.query(sql, new VacationRequestRepository.VacationRequestRowMapper());
    }

    // SQL query - create a new vacation request.
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

    public VacationRequest getVacationRequestById(long id) {
        String sql = """
                SELECT id, employee_id, vacation_start, vacation_end, submitted_at, comment
                FROM vacation_request
                WHERE id = ?
            """;
        return jdbcTemplate.queryForObject(sql, new VacationRequestRepository.VacationRequestRowMapper(), id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM vacation_request WHERE id = ?";
        // TODO: Fix deprecated method
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM vacation_request WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // TODO: To finish.
    public List<VacationRequest> getFiltered(String name, LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder sql = new StringBuilder("""
                select  vr.id, vr.employee_id, vr.vacation_start, vr.vacation_end, vr.submitted_at, vr.comment 
                from
                vacation_request vr Join employees e ON e.id = vr.employee_id
                where 1=1;
                """);

        if (name != null && !name.isEmpty()) {
            sql.append(" and e.name = ");
        }
        return jdbcTemplate.query(sql.toString(), new VacationRequestRepository.VacationRequestRowMapper());

    }

    // Row mapper - convert data from a database to java object.
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
