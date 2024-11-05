import {Component, OnInit, ViewChild} from '@angular/core';
import { NgFor } from '@angular/common';
import { VacationRequest } from '../vacation-request';
import { VacationRequestService } from '../vacation-request.service';
import {VacationRequestFormComponent} from "../form/vacation-request-form.component";
import {Employee} from "../employee";
import {EmployeeService} from "../employee.service";
import {ButtonDirective} from "primeng/button";

@Component({
  standalone: true,
  selector: 'vacation-list',
  imports: [NgFor, VacationRequestFormComponent, ButtonDirective],
  templateUrl: 'vacation-list.component.html',
  styleUrl: 'vacation-list.component.scss'
})
export class VacationListComponent implements OnInit {
  @ViewChild(VacationRequestFormComponent) vacationRequestFromComponent!: VacationRequestFormComponent;

  public vacations: VacationRequest[] = [];
  public employees: Employee[] = [];
  public selectedVacation: VacationRequest | null = null;

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
    this.loadVacationRequests()
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
          this.loadVacationRequests();
        });
    } else {
      console.warn("Vacation ID is undefined or null. Cannot delete.");
    }
    }
  }
}
