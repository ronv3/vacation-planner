package com.kodality.vacation.request;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import lombok.Setter;

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

    @Post
    public HttpResponse<?> createVacationRequest(VacationRequest vacationRequest) {
        try {
            vacationRequestService.createVacationRequest(vacationRequest);
            return HttpResponse.status(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(e.getMessage());
        }
    }

}
