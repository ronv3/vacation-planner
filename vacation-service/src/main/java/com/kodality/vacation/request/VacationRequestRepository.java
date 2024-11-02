package com.kodality.vacation.request;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
public class VacationRequestRepository {

  private final JdbcTemplate jdbcTemplate;

  @Inject
  public VacationRequestRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<VacationRequest> getVacationRequests() {
    String sql = "SELECT id, employee_name, submitted_at, comment FROM vacation_request";
    return jdbcTemplate.query(sql, new VacationRequestRowMapper());
  }

  private static class VacationRequestRowMapper implements RowMapper<VacationRequest> {
    @Override
    public VacationRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new VacationRequest()
              .setId(rs.getLong("id"))
              .setEmployeeName(rs.getString("employee_name"))
              .setSubmittedAt(rs.getObject("submitted_at", LocalDateTime.class))
              .setComment(rs.getString("comment"));
    }
  }
}
