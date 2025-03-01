package com.org.vacation.request;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public HttpResponse<?> deleteVacationRequest(@PathVariable Long id) {
        try {
            vacationRequestService.deleteVacationRequest(id);
            return HttpResponse.ok();
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(e.getMessage());
        }
    }


    @Get("/filter")
    public HttpResponse<List<VacationRequest>> filterVacationRequests(
            @QueryValue("employeeId") Optional<Long> employeeId,
            @QueryValue("startDate") Optional<LocalDate> startDate,
            @QueryValue("endDate") Optional<LocalDate> endDate) {
        List<VacationRequest> filteredVacationRequests = vacationRequestService.getFilteredVacationRequests(
                employeeId.orElse(null), startDate.orElse(null), endDate.orElse(null)
        );
        Logger logger = Logger.getLogger(getClass().getName());
        logger.log(Level.INFO, employeeId.toString());
        logger.log(Level.INFO, startDate.toString());
        logger.log(Level.INFO, endDate.toString());
        if (filteredVacationRequests.isEmpty()) {
            return HttpResponse.notFound(filteredVacationRequests);
        }
        return HttpResponse.ok(filteredVacationRequests);
    }


}
