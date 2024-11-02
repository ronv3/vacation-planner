package com.kodality.vacation.request;

import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
public class VacationRequestRepository {

  public List<VacationRequest> getVacationRequests() {
    // TODO: query real data from database here
    return List.of(
        new VacationRequest().setEmployeeName("Taavi Tikk").setSubmittedAt(LocalDateTime.of(2024, 1, 14, 12, 54)).setComment("test vacation request #1"),
        new VacationRequest().setEmployeeName("Andres Mihkelson").setSubmittedAt(LocalDateTime.of(2024, 1, 23, 23, 17)).setComment("test vacation request #2"),
        new VacationRequest().setEmployeeName("Anu Kalda").setSubmittedAt(LocalDateTime.of(2024, 2, 2, 9, 27)).setComment("test vacation request #3")
    );
  }

}
