export interface VacationRequest {
  id?: number | null;
  employeeId: number;
  vacationStart: string;
  vacationEnd: string;
  comment: string;
  submittedAt: string;
}
