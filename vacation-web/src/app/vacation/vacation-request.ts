export interface VacationRequest {
  id: number;
  employeeName: string;
  vacationStart: Date;
  vacationEnd: Date;
  comment: string;
  submittedAt: Date;
}
