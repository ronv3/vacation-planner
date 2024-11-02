export interface VacationRequest {
  id: number;
  employee: string;
  startDate: Date;
  endDate: Date;
  comment: string;
  submittedAt: Date;
}
