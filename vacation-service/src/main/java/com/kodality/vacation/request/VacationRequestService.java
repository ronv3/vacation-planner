package com.kodality.vacation.request;

import com.kodality.vacation.employee.Employee;
import com.kodality.vacation.employee.EmployeeService;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.springframework.util.xml.SimpleTransformErrorListener;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Singleton
@Requires(beans = DataSource.class)
public class VacationRequestService {

    private final VacationRequestRepository vacationRequestRepository;
    private final EmployeeService employeeService;

    public VacationRequestService(VacationRequestRepository vacationRequestRepository, EmployeeService employeeService) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.employeeService = employeeService;
    }

    public List<VacationRequest> getVacationRequests() {
        return vacationRequestRepository.getVacationRequests();
    }

    public void createVacationRequest(VacationRequest vacationRequest) {
        Employee employee = employeeService.getEmployeeById(vacationRequest.getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }

        long requestedDays = ChronoUnit.DAYS.between(vacationRequest.getVacationStart(), vacationRequest.getVacationEnd()) + 1;

        // Check if employee has enough vacation days.
        if (requestedDays > employee.getRemainingVacationDays()) {
            throw new IllegalArgumentException("Insufficient vacation days remaining.");
        }

        // Check if the request is submitted at least 14 days in advance
        if (ChronoUnit.DAYS.between(LocalDate.now(), vacationRequest.getVacationStart()) < 14) {
            throw new IllegalArgumentException("Vacation request must be submitted at least 14 days in advance.");
        }

        vacationRequest.setSubmittedAt(LocalDateTime.now());
        vacationRequestRepository.create(vacationRequest);
        employeeService.updateRemainingVacationDays(employee.getId(), employee.getRemainingVacationDays() - (int) requestedDays);


    }

}
