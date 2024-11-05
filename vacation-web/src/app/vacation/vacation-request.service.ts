import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VacationRequest } from './vacation-request';

@Injectable({ providedIn: 'root' })
export class VacationRequestService {

  constructor(private readonly http: HttpClient) {}

  public getVacationRequests(): Observable<VacationRequest[]> {
    return this.http.get<VacationRequest[]>('/api/vacation-requests');
  }

  public saveVacationRequest(request: VacationRequest): Observable<VacationRequest> {
    return this.http.post<VacationRequest>('/api/vacation-requests', request, {
      headers: { 'Content-Type': 'application/json' }
    })
  }

  deleteVacationRequest(id: number): Observable<void> {
    return this.http.delete<void>(`/api/vacation-requests/${id}`);
  }
}
