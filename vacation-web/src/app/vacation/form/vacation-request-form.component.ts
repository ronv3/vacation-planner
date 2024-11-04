import {Component, EventEmitter, OnInit, Output} from '@angular/core';
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
  public date1: Date | undefined;
  @Output() requestSubmitted = new EventEmitter<void>();

  constructor(
    private employeeService: EmployeeService,
    private vacationRequestService: VacationRequestService,
    private fb: FormBuilder,
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

      const requestData: VacationRequest = {
        employeeId: formValues.employeeId,
        vacationStart: formValues.vacationRange[0].toISOString().split('T')[0],
        vacationEnd: formValues.vacationRange[1].toISOString().split('T')[0],
        comment: formValues.comments,
        submittedAt: new Date().toISOString().split('.')[0]
      };

      this.vacationRequestService.saveVacationRequest(requestData).subscribe({
        next: (response) => {
          console.log('Vacation request submitted:', response);
          this.requestSubmitted.emit();
          this.closeModal();
        },
        error: (error) => {
          console.error('Error submitting vacation request:', error);
        },
        complete: () => {
          console.log('Request completed');
        }
      });

    }
  }
}
