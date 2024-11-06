import {Component, OnInit, ViewChild} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {VacationRequest} from '../vacation-request';
import {VacationRequestService} from '../vacation-request.service';
import {VacationRequestFormComponent} from "../form/vacation-request-form.component";
import {Employee} from "../employee";
import {EmployeeService} from "../employee.service";
import {ButtonDirective} from "primeng/button";
import {Calendar, CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";

@Component({
  standalone: true,
  selector: 'vacation-list',
  imports: [NgFor, VacationRequestFormComponent, ButtonDirective, CalendarModule, DropdownModule, NgIf, FormsModule],
  templateUrl: 'vacation-list.component.html',
  styleUrl: 'vacation-list.component.scss'
})
export class VacationListComponent implements OnInit {
  @ViewChild(VacationRequestFormComponent) vacationRequestFromComponent!: VacationRequestFormComponent;
  @ViewChild('startDatePicker') namePicker!: DropdownModule;
  @ViewChild('startDatePicker') startDatePicker!: Calendar;
  @ViewChild('endDatePicker') endDatePicker!: Calendar;

  public vacations: VacationRequest[] = [];
  public employees: Employee[] = [];
  public selectedVacation: VacationRequest | null = null;

  public filteredEmployeeId: number | null = null;
  public filteredStartDate: Date | null = null;
  public filteredEndDate: Date | null = null;

  constructor(
    private vacationRequestService: VacationRequestService,
    private employeeService: EmployeeService) {
  }

  ngOnInit(): void {
    this.loadVacationRequests(this.filteredEmployeeId, this.filteredStartDate, this.filteredEndDate);
    this.loadEmployees();
  }

  loadVacationRequests(filteredEmployeeId: number | null, startDate: any, endDate: any): void {
    this.filteredStartDate = startDate instanceof Date ? startDate : null;
    this.filteredEndDate = endDate instanceof Date ? endDate : null;

    //Troubleshoot log
    console.log(this.filteredEmployeeId)
    console.log(this.filteredStartDate)
    console.log(this.filteredEndDate)
    if (filteredEmployeeId != null || this.filteredStartDate != null || this.filteredEndDate != null) {
      // Call the service method with provided filters
      this.filteredEmployeeId = filteredEmployeeId;
      this.vacationRequestService.filterVacationRequests(
        filteredEmployeeId,
        this.filteredStartDate,
        this.filteredEndDate
      ).subscribe(response => {
        this.vacations = response;
        console.log("Filter applied successfully.");
      });
    } else {
      // Retrieve all vacation requests if no filter is applied
      this.vacationRequestService.getVacationRequests().subscribe(response => {
        this.vacations = response;
        console.log("No filters applied; retrieved all vacation requests.");
      });
    }
  }

  loadEmployees(): void {
    this.employeeService.getEmployees().subscribe(response => {
      this.employees = response;
    });
  }

  refreshVacationRequests(createdRequest: VacationRequest): void {
    this.loadVacationRequests(this.filteredEmployeeId, this.filteredStartDate, this.filteredEndDate)
  }

  getEmployeeName(employeeId: number): string {
    const employee = this.employees.find(emp => emp.id === employeeId);
    return employee ? employee.employeeName : 'Unknown';
  }

  editVacation(vacation: VacationRequest): void {
    this.selectedVacation = vacation;
    this.vacationRequestFromComponent.existingRequest = vacation;
    this.vacationRequestFromComponent.openModal();
    console.log('Editing vacation request:', vacation)
  }

  deleteVacation(vacationId: number | undefined | null) {
    if (vacationId != null) {
      if (confirm('Are you sure you want to delete this vacation request?')) {
        this.vacationRequestService.deleteVacationRequest(vacationId).subscribe(() => {
          console.log('Vacation request deleted successfully');
          this.loadVacationRequests(this.filteredEmployeeId, this.filteredStartDate, this.filteredEndDate);
        });
      } else {
        console.warn("Vacation ID is undefined or null. Cannot delete.");
      }
    }
  }

  clearFilters(): void {
    // Reset filter criteria
    this.filteredEmployeeId = null;
    this.filteredStartDate = null;
    this.filteredEndDate = null;


    // Reload all vacation requests without filters
    this.loadVacationRequests(this.filteredEmployeeId, this.filteredStartDate, this.filteredEndDate);
  }

  protected readonly Date = Date;
}
