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

    public VacationRequestService(VacationRequestRepository vacationRequestRepository, EmployeeService employeeService) {
        this.vacationRequestRepository = vacationRequestRepository;
        this.employeeService = employeeService;
    }

    public List<VacationRequest> getVacationRequests() {
        return vacationRequestRepository.getVacationRequests();
    }

    public VacationRequest createVacationRequest(VacationRequest vacationRequest) {
        Employee employee = employeeService.getEmployeeById(vacationRequest.getEmployeeId());

        // Validating that employee exists
        validateEmployeeExists(employee);

        // Validate Vacation Request
        long requestedDays = calculateVacationDuration(vacationRequest);
        validateVacationRequest(requestedDays, vacationRequest, employee.getRemainingVacationDays());

        // Calculating newRemainingVacationDays
        long newRemainingVacationDays = employee.getRemainingVacationDays() - requestedDays;

        // Applying changes
        employeeService.updateRemainingVacationDays(employee.getId(), (int) newRemainingVacationDays);
        long id = vacationRequestRepository.create(vacationRequest);
        vacationRequest.setSubmittedAt(LocalDateTime.now());
        vacationRequest.setId(id);
        return vacationRequest;

    }

    public void updateVacationRequest(long id, VacationRequest newVacationRequest) {
        Employee employee = employeeService.getEmployeeById(newVacationRequest.getEmployeeId());

        // Validating Employee input
        validateEmployeeExists(employee);

        // Calculate actual remaining days
        long currentRemainingVacationDays = calculateRemainingVacationDaysAfterDeletion(id, employee);

        // Validate Vacation Request
        long requestedDays = calculateVacationDuration(newVacationRequest);
        validateVacationRequest(requestedDays, newVacationRequest, currentRemainingVacationDays);

        // Calculating newRemainingVacationDays
        long newRemainingVacationDays = currentRemainingVacationDays - requestedDays;

        //Applying changes
        employeeService.updateRemainingVacationDays(newVacationRequest.getEmployeeId(), (int) (newRemainingVacationDays));
        vacationRequestRepository.updateVacationRequest(id, newVacationRequest);
        newVacationRequest.setSubmittedAt(LocalDateTime.now());
    }

    public void deleteVacationRequest(Long id) {
        // Find relevant vacation Request, validate
        VacationRequest vacationRequest = getVacationRequestById(id);
        validateVacationRequestExists(vacationRequest);

        // Find relevant employee, validate
        Employee employee = employeeService.getEmployeeById(vacationRequest.getEmployeeId());
        validateEmployeeExists(employee);

        // Calculate deleted vacation duration and add it to current remaining vacation days
        long vacationDuration = calculateVacationDuration(vacationRequest);
        int updatedRemainingVacationDays = (int) (employee.getRemainingVacationDays() + vacationDuration);

        // Apply changes
        employeeService.updateRemainingVacationDays(employee.getId(), updatedRemainingVacationDays);
        vacationRequestRepository.deleteById(id);
    }

    // Calculate duration between days
    private long calculateVacationDuration(VacationRequest vacationRequest) {
        return ChronoUnit.DAYS.between(vacationRequest.getVacationStart(), vacationRequest.getVacationEnd()) + 1;
    }

    // Calculating remaining vacation days after deletion
    private long calculateRemainingVacationDaysAfterDeletion(long oldId, Employee employee) {
        long oldDuration = calculateVacationDuration(getVacationRequestById(oldId));
        return oldDuration + employee.getRemainingVacationDays();
    }

    // Validating Vacation Request
    private void validateVacationRequest(long requestedDays, VacationRequest vacationRequest, long remainingVacationDays) {
        // Enough remaining vacation days
        if (requestedDays > remainingVacationDays) {
            throw new IllegalArgumentException("Insufficient vacation days remaining.");
        }
        // Atleast 14 days before requesting
        if (ChronoUnit.DAYS.between(LocalDate.now(), vacationRequest.getVacationStart()) < 14) {
            throw new IllegalArgumentException("Vacation request must be submitted at least 14 days in advance.");
        }
    }

    // Check if vacation request exists
    private void validateVacationRequestExists(VacationRequest vacationRequest) {
        if (vacationRequest == null) {
            throw new IllegalArgumentException("Vacation request not found.");
        }
    }

    // Check if employee exists
    private void validateEmployeeExists(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found.");
        }
    }

    // Find vacation request by id
    private VacationRequest getVacationRequestById(Long id) {
        return vacationRequestRepository.getVacationRequestById(id);
    }

    // Get filtered requests
    public List<VacationRequest> getFilteredVacationRequests(long id, LocalDate startDate, LocalDate endDate) {
        String name = employeeService.getEmployeeById(id).getEmployeeName();
        return vacationRequestRepository.getFiltered(name, startDate, endDate);

    }
}
