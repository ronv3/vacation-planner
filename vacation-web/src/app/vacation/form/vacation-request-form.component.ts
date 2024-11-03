import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../employee.service';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Employee } from '../employee';
import {DialogModule} from "primeng/dialog";
import {DropdownModule} from "primeng/dropdown";
import {CalendarModule} from "primeng/calendar";
import {PrimeNGConfig} from "primeng/api";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {InputTextareaModule} from "primeng/inputtextarea";
import {VacationRequest} from "../vacation-request";
import {VacationRequestService} from "../vacation-request.service";

export function dateRangeValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value || value.length !== 2) {
      return null;
    }

    const endDate = value[1];
    return endDate ? null : { invalidDateRange: true };
  };
}

@Component({
  selector: 'vacation-request-form',
  standalone: true,
  templateUrl: './vacation-request-form.component.html',
  imports: [
    DialogModule, ButtonModule, InputTextModule, ReactiveFormsModule, DropdownModule, CalendarModule, NgIf, InputTextareaModule
  ],
  styleUrls: ['./vacation-request-form.component.scss']
})
export class VacationRequestFormComponent implements OnInit {
  public employees: Employee[] = [];
  public vacationRequestForm: FormGroup;
  public isModalOpen: boolean = false;
  date1: Date | undefined;

  constructor(
    private employeeService: EmployeeService,
    private vacationRequestService: VacationRequestService,
    private fb: FormBuilder,
    private http: HttpClient,
    private primengConfig: PrimeNGConfig
  ) {
    this.vacationRequestForm = this.fb.group({
      employeeId: ['', Validators.required],
      vacationRange: [null, [Validators.required, dateRangeValidator()]],
      comments: ['', Validators.maxLength(200)]
    });
  }

  ngOnInit(): void {
    this.loadEmployees();
    this.primengConfig.ripple = true;
  }

  loadEmployees(): void {
    this.employeeService.getEmployees().subscribe(response => {
      this.employees = response;
    });
  }

  openModal(): void {
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.vacationRequestForm.reset();
  }

  onSubmit(): void {
    if (this.vacationRequestForm.valid) {
      const formValues = this.vacationRequestForm.value;

      const requestData : VacationRequest = {
        employeeId: formValues.employeeId,
        vacationStart: new Date(formValues.vacationRange[0]),
        vacationEnd: new Date(formValues.vacationRange[1]),
        comment: formValues.comments,
        submittedAt: new Date()
      };

      this.vacationRequestService.saveVacationRequest(requestData);

    }
  }
}
