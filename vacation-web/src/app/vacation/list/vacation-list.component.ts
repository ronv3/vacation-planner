import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { VacationRequest } from '../vacation-request';
import { VacationRequestService } from '../vacation-request.service';
import {VacationRequestFormComponent} from "../form/vacation-request-form.component";
import {Employee} from "../employee";
import {EmployeeService} from "../employee.service";

@Component({
  standalone: true,
  selector: 'vacation-list',
  imports: [NgFor, VacationRequestFormComponent],
  templateUrl: 'vacation-list.component.html',
  styleUrl: 'vacation-list.component.scss'
})
export class VacationListComponent implements OnInit {

  public vacations: VacationRequest[] = [];
  public employees: Employee[] = [];

  constructor(
    private vacationRequestService: VacationRequestService,
    private employeeService: EmployeeService) {
  }

  ngOnInit(): void {
    this.loadVacationRequests();
    this.loadEmployees();
  }

  private loadVacationRequests() : void {
    this.vacationRequestService.getVacationRequests().subscribe(response => {
      this.vacations = response
    });
  }

  loadEmployees(): void {
    this.employeeService.getEmployees().subscribe(response => {
      this.employees = response;
    });
  }

  refreshVacationRequests(createdRequest: VacationRequest): void {
    this.vacations.push(createdRequest);
    console.log(this.vacations)
  }

  getEmployeeName(employeeId: number): string {
    const employee = this.employees.find(emp => emp.id === employeeId);
    return employee ? employee.employeeName : 'Unknown';
  }

  editVacation(vacation: VacationRequest) {
    // TODO: Open the vacation request form with existing data
    // TODO: Logic to pre-fill the form with `vacation` data goes here
    console.log('Editing vacation request:', vacation);
    // TODO: Potentially pass data to the vacation-request-form component for editing
  }

  deleteVacation(vacationId: number | undefined | null) {
    if (vacationId != null) {
      if (confirm('Are you sure you want to delete this vacation request?')) {
        this.vacationRequestService.deleteVacationRequest(vacationId).subscribe(() => {
          console.log('Vacation request deleted successfully');
          this.loadVacationRequests();
        });
    } else {
      console.warn("Vacation ID is undefined or null. Cannot delete.");
    }
    }
  }
}
