import {Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {EmployeeService} from '../employee.service';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {Employee} from '../employee';
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
    return endDate ? null : {invalidDateRange: true};
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
  @Input() existingRequest: VacationRequest | null = null;
  @Output() requestSubmitted = new EventEmitter<VacationRequest>();
  @ViewChild('modal')modal!: ElementRef;

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
    if (this.existingRequest) {
      this.populateForm(this.existingRequest);
    }
  }

  populateForm(request: VacationRequest): void {
    this.vacationRequestForm.patchValue({
      employeeId: request.employeeId,
      vacationRange: [new Date(request.vacationStart), new Date(request.vacationEnd)],
      comments: request.comment
    });
  }

  loadEmployees(): void {
    this.employeeService.getEmployees().subscribe(response => {
      this.employees = response;
    });
  }

  openModal(): void {
    this.isModalOpen = true;
    if (this.existingRequest) {
      this.populateForm(this.existingRequest);
    }
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.vacationRequestForm.reset();
    this.existingRequest = null;
  }

  onSubmit(): void {
    if (this.vacationRequestForm.valid) {
      const formValues = this.vacationRequestForm.value;
      const requestData: VacationRequest = {
        id: this.existingRequest ? this.existingRequest.id : null,
        employeeId: formValues.employeeId,
        vacationStart: new Date(formValues.vacationRange[0]).toLocaleDateString('en-CA'),
        vacationEnd: new Date(formValues.vacationRange[1]).toLocaleDateString('en-CA'),
        comment: formValues.comments,
        submittedAt: new Date().toISOString().split('.')[0]
      };


      const submitObservable = requestData.id
          ? this.vacationRequestService.updateVacationRequest(requestData)
          : this.vacationRequestService.saveVacationRequest(requestData);

      submitObservable.subscribe({
        next: updatedOrCreatedRequest => {
          this.requestSubmitted.emit(updatedOrCreatedRequest);
          this.closeModal();
          this.vacationRequestForm.reset();
        },
        error: (errorResponse) => {
          console.error('Unexpected error submitting vacation request:', errorResponse);
          this.vacationRequestForm.setErrors(errorResponse.error);
        }
      });
    }
  }
}
