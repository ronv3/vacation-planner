package com.kodality.vacation.request;
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

  // SQL query
  public List<VacationRequest> getVacationRequests() {
    String sql = "SELECT id, employee_name, submitted_at, comment FROM vacation_request";
    return jdbcTemplate.query(sql, new VacationRequestRowMapper());
  }

  // Row mapper - convert data from a database to java object.
  private static class VacationRequestRowMapper implements RowMapper<VacationRequest> {
    @Override
    public VacationRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new VacationRequest()
              .setId(rs.getLong("id"))
              .setEmployeeName(rs.getString("employee_name"))
              .setVacationStart(rs.getObject("vacation_start", LocalDate.class))
              .setVacationEnd(rs.getObject("vacation_end", LocalDate.class))
              .setComment(rs.getString("comment"))
              .setSubmittedAt(rs.getObject("submitted_at", LocalDateTime.class));
    }
  }
}
