package com.kodality.vacation.request;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.util.List;

@Controller("vacation-requests")
public class VacationRequestController {

  private final VacationRequestService vacationRequestService;

  @Inject
  public VacationRequestController(VacationRequestService vacationRequestService) {
    this.vacationRequestService = vacationRequestService;
  }

  @Get
  public List<VacationRequest> getVacationRequests() {
    return vacationRequestService.getVacationRequests();
  }

}
