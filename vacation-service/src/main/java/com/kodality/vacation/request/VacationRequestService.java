package com.kodality.vacation.request;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.util.List;

@Singleton
@Requires(beans = DataSource.class)
public class VacationRequestService {

  private final VacationRequestRepository vacationRequestRepository;

  public VacationRequestService(VacationRequestRepository vacationRequestRepository) {
    this.vacationRequestRepository = vacationRequestRepository;
  }

  public List<VacationRequest> getVacationRequests() {
    return vacationRequestRepository.getVacationRequests();
  }

}
