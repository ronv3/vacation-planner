package com.kodality.vacation.request;

import jakarta.inject.Singleton;
import java.util.List;

@Singleton
public class VacationRequestService {

  private final VacationRequestRepository vacationRequestRepository;

  public VacationRequestService(VacationRequestRepository vacationRequestRepository) {
    this.vacationRequestRepository = vacationRequestRepository;
  }

  public List<VacationRequest> getVacationRequests() {
    return vacationRequestRepository.getVacationRequests();
  }

}
