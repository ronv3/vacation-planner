package com.org.vacation

import com.org.vacation.employee.Employee
import com.org.vacation.employee.EmployeeService
import com.org.vacation.request.VacationRequest
import com.org.vacation.request.VacationRequestRepository
import com.org.vacation.request.VacationRequestService
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
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

    @Shared
    Employee employee = Mock(Employee)
    @Shared
    VacationRequest vacationRequest = Mock(VacationRequest)

    def setup() {
        employeeService.getEmployeeById(0) >> employee
        vacationRequestRepository.getVacationRequests() >> [vacationRequest]
    }

    def "test createVacationRequest"() {
        given: "A valid vacation request"
        vacationRequest.getEmployeeId() >> 1L
        vacationRequest.getVacationStart() >> LocalDate.now().plusDays(20)
        vacationRequest.getVacationEnd() >> LocalDate.now().plusDays(25)
        vacationRequest.getComment() >> "Test comment"

        employee.getRemainingVacationDays() >> 20 // employee has 20 remaining vacation days
        vacationRequestRepository.create(vacationRequest) >> 1L // simulate saving the vacation request and returning an ID

        when: "Creating a new vacation request"
        def createdRequest = vacationRequestService.createVacationRequest(vacationRequest)

        then: "The vacation request is created successfully"
        createdRequest.id == 1L
        createdRequest.getEmployeeId() == 1L
        createdRequest.getVacationStart() == LocalDate.now().plusDays(20)
        createdRequest.getVacationEnd() == LocalDate.now().plusDays(25)
    }


    def "test updateVacationRequest"() {
        given: "An existing vacation request and an updated request"
        vacationRequest.getId() >> 1L
        vacationRequest.getEmployeeId() >> 1L
        vacationRequest.getVacationStart() >> LocalDate.now().plusDays(20)
        vacationRequest.getVacationEnd() >> LocalDate.now().plusDays(25)
        vacationRequest.getComment() >> "Updated comment"

        employee.getRemainingVacationDays() >> 20 // employee has 20 remaining vacation days
        vacationRequestRepository.updateVacationRequest(_, _) >> true // simulate updating the vacation request

        when: "Updating an existing vacation request"
        vacationRequestService.updateVacationRequest(1L, vacationRequest)

        then: "The vacation request is updated successfully"
        1 * vacationRequestRepository.updateVacationRequest(1L, vacationRequest)
    }


    def "test deleteVacationRequest"() {
        given: "An existing vacation request"
        vacationRequest.getId() >> 1L
        vacationRequest.getEmployeeId() >> 1L
        vacationRequest.getVacationStart() >> LocalDate.now().plusDays(20)
        vacationRequest.getVacationEnd() >> LocalDate.now().plusDays(25)

        employee.getRemainingVacationDays() >> 20 // employee has 20 remaining vacation days
        vacationRequestRepository.deleteById(_) >> true // simulate deleting the vacation request

        when: "Deleting a vacation request"
        vacationRequestService.deleteVacationRequest(1L)

        then: "The vacation request is deleted successfully"
        1 * vacationRequestRepository.deleteById(1L)
    }


    def "test getFilteredVacationRequests"() {
        given: "An employee ID and vacation date range"
        def employeeName = "John Doe"
        def startDate = LocalDate.now().plusDays(10)
        def endDate = LocalDate.now().plusDays(20)

        employeeService.getEmployeeById(1L) >> employee
        employee.getEmployeeName() >> employeeName
        vacationRequestRepository.getFiltered(employeeName, startDate, endDate) >> [vacationRequest]

        when: "Getting filtered vacation requests"
        def filteredRequests = vacationRequestService.getFilteredVacationRequests(1L, startDate, endDate)

        then: "The filtered vacation requests are returned"
        filteredRequests.size() == 1
        filteredRequests[0] == vacationRequest
    }


    def "test createVacationRequest throws error if employee does not exist"() {
        given: "A vacation request with a non-existent employee ID"
        vacationRequest.getEmployeeId() >> 999L // non-existent employee ID
        employeeService.getEmployeeById(999L) >> null // mock that employee does not exist

        when: "Creating a new vacation request with an invalid employee"
        vacationRequestService.createVacationRequest(vacationRequest)

        then: "An error is thrown"
        thrown(IllegalArgumentException)
    }


    def "test createVacationRequest throws error if insufficient vacation days"() {
        given: "A vacation request with insufficient vacation days"
        vacationRequest.getEmployeeId() >> 1L
        vacationRequest.getVacationStart() >> LocalDate.now().plusDays(20)
        vacationRequest.getVacationEnd() >> LocalDate.now().plusDays(25)
        vacationRequest.getComment() >> "Test comment"

        employee.getRemainingVacationDays() >> 10 // employee has 10 remaining vacation days
        vacationRequestRepository.create(_) >> 1L

        when: "Creating a vacation request with insufficient vacation days"
        vacationRequestService.createVacationRequest(vacationRequest)

        then: "An error is thrown"
        thrown(IllegalArgumentException)
    }

}
