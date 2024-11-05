package com.kodality.vacation.request;

import com.kodality.vacation.employee.Employee;
import com.kodality.vacation.employee.EmployeeRepository;
import com.kodality.vacation.employee.EmployeeService;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
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
    private final EmployeeRepository employeeRepository;

    public VacationRequestService(VacationRequestRepository vacationRequestRepository, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    public List<VacationRequest> getVacationRequests() {
        return vacationRequestRepository.getVacationRequests();
    }

    public VacationRequest createVacationRequest(VacationRequest vacationRequest) {
        Employee employee = employeeService.getEmployeeById(vacationRequest.getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }

        // Validating input
        long requestedDays = ChronoUnit.DAYS.between(vacationRequest.getVacationStart(), vacationRequest.getVacationEnd()) + 1;
        if (requestedDays > employee.getRemainingVacationDays()) {
            throw new IllegalArgumentException("Insufficient vacation days remaining.");
        }
        if (ChronoUnit.DAYS.between(LocalDate.now(), vacationRequest.getVacationStart()) < 14) {
            throw new IllegalArgumentException("Vacation request must be submitted at least 14 days in advance.");
        }

        vacationRequest.setSubmittedAt(LocalDateTime.now());

        // Creating a new vacation request in the db, returning ID
        long id = vacationRequestRepository.create(vacationRequest);
        employeeService.updateRemainingVacationDays(employee.getId(), employee.getRemainingVacationDays() - (int) requestedDays);
        vacationRequest.setId(id);

        return vacationRequest;

    }

    public void updateVacationRequest(long id, VacationRequest vacationRequest) {
        Employee employee = employeeService.getEmployeeById(vacationRequest.getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }

        //Ca
        VacationRequest oldVacationRequest = vacationRequestRepository.getVacationRequestById(id);
        int oldDuration = (int) ChronoUnit.DAYS.between(oldVacationRequest.getVacationStart(), oldVacationRequest.getVacationEnd()) + 1;

        // Validating input
        long requestedDays = ChronoUnit.DAYS.between(vacationRequest.getVacationStart(), vacationRequest.getVacationEnd()) + 1;
        if (requestedDays > employee.getRemainingVacationDays()) {
            throw new IllegalArgumentException("Insufficient vacation days remaining.");
        }
        if (ChronoUnit.DAYS.between(LocalDate.now(), vacationRequest.getVacationStart()) < 14) {
            throw new IllegalArgumentException("Vacation request must be submitted at least 14 days in advance.");
        }

        vacationRequest.setSubmittedAt(LocalDateTime.now());
        vacationRequestRepository.updateVacationRequest(id, vacationRequest);
        employeeRepository.updateRemainingVacationDays(id, oldDuration);
    }

    public void deleteVacationRequest(Long id) {
        if (!vacationRequestRepository.existsById(id)) {
            throw new IllegalArgumentException("Vacation request not found.");
        }

        vacationRequestRepository.deleteById(id);
    }

    public List<VacationRequest> getFilteredVacationRequests(String name, LocalDateTime startDate, LocalDateTime endDate) {
        return vacationRequestRepository.getFiltered(name, startDate, endDate);

    }
}
