export interface VacationRequest {
  employeeId: number;
  vacationStart: Date;
  vacationEnd: Date;
  comment: string;
  submittedAt: Date;
}
