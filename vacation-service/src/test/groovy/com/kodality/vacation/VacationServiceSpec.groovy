package com.kodality.vacation

import com.kodality.vacation.employee.Employee
import com.kodality.vacation.employee.EmployeeService
import com.kodality.vacation.request.VacationRequest
import com.kodality.vacation.request.VacationRequestRepository
import com.kodality.vacation.request.VacationRequestService
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import jakarta.inject.Inject
import spock.lang.Unroll
import java.time.LocalDate

@MicronautTest
class VacationServiceSpec extends Specification {

    @Inject
    VacationRequestService vacationRequestService
    @Inject
    VacationRequestRepository vacationRequestRepository
    @Inject
    EmployeeService employeeService

    @Unroll
    void "should submit vacation request successfully when employee has enough days and submits 14+ days in advance"() {
        given:
        def employee = new Employee()
                .setId(1L)
                .setEmployeeName("John Doe")
                .setRemainingVacationDays(28)

        def vacationRequest = new VacationRequest()
                .setEmployeeId(employee.getId())
                .setVacationStart(LocalDate.now().plusDays(15)) // 15 days in advance
                .setVacationEnd(LocalDate.now().plusDays(18))   // 3-day vacation
                .setComment("Annual leave")

        employeeService.updateRemainingVacationDays(employee.getId(), employee.getRemainingVacationDays())

        when:
        vacationRequestService.createVacationRequest(vacationRequest)

        then:
        noExceptionThrown()
    }

    @Unroll
    void "should throw exception if employee does not have enough vacation days"() {
        given:
        def employee = new Employee()
                .setId(2L)
                .setEmployeeName("Jane Doe")
                .setRemainingVacationDays(2) // Only 2 days left

        def vacationRequest = new VacationRequest()
                .setEmployeeId(employee.getId())
                .setVacationStart(LocalDate.now().plusDays(15))
                .setVacationEnd(LocalDate.now().plusDays(20)) // Requesting 5 days

        employeeService.updateRemainingVacationDays(employee.getId(), employee.getRemainingVacationDays())

        when:
        vacationRequestService.createVacationRequest(vacationRequest)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Insufficient vacation days remaining."
    }

    @Unroll
    void "should throw exception if vacation request is submitted less than 14 days in advance"() {
        given:
        def employee = new Employee()
                .setId(3L)
                .setEmployeeName("Alice Smith")
                .setRemainingVacationDays(10) // Has enough days

        def vacationRequest = new VacationRequest()
                .setEmployeeId(employee.getId())
                .setVacationStart(LocalDate.now().plusDays(10)) // Less than 14 days in advance
                .setVacationEnd(LocalDate.now().plusDays(12))

        employeeService.updateRemainingVacationDays(employee.getId(), employee.getRemainingVacationDays())

        when:
        vacationRequestService.createVacationRequest(vacationRequest)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Vacation request must be submitted at least 14 days in advance."
    }


}
