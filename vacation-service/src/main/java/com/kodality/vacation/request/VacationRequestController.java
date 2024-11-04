package com.kodality.vacation.request;

import com.google.gson.Gson;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
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
            vacationRequestService.createVacationRequest(vacationRequest);
            return HttpResponse.status(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return HttpResponse.badRequest("Invalid JSON: " + e.getMessage());
        }
    }

    @Put("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpResponse<?> updateVacationRequest(@PathVariable long id, @Body String json) {
        try {
            // convert JSON to object
            Gson gson = new Gson();
            VacationRequest vacationRequest = gson.fromJson(json, VacationRequest.class);
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

}
