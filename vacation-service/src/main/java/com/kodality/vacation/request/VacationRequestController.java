package com.kodality.vacation.request;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@Controller("/vacation-requests")
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
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse<?> createVacationRequest(@Body VacationRequest vacationRequest) {
        try {
            VacationRequest createdRequest = vacationRequestService.createVacationRequest(vacationRequest);
            return HttpResponse.status(HttpStatus.CREATED).body(createdRequest);
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return HttpResponse.badRequest("Invalid JSON: " + e.getMessage());
        }
    }

    @Put("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse<?> updateVacationRequest(@PathVariable long id, @Body VacationRequest vacationRequest) {
        try {
            vacationRequestService.updateVacationRequest(id, vacationRequest);
            return HttpResponse.status(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return HttpResponse.badRequest("Invalid JSON: " + e.getMessage());
        }
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT) // Responds with 204 No Content
    public void deleteVacationRequest(@PathVariable Long id) {
        vacationRequestService.deleteVacationRequest(id);
    }

    /*@Get("/FilterVacations")
    public HttpResponse<List<VacationRequest>> updateVacationRequest(@RequestAttribute String name, @RequestAttribute LocalDateTime startDate, @RequestAttribute LocalDateTime endDate) {
        List<VacationRequest> filteredVacationRequests = vacationRequestService.getFilteredVacationRequests(name, startDate, endDate);
        if (filteredVacationRequests.isEmpty()) {
            return HttpResponse.notFound(filteredVacationRequests);
        }

        return HttpResponse.ok(filteredVacationRequests);
    }

     */

}
