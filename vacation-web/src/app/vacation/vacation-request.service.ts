import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { VacationRequest } from './vacation-request';

@Injectable({providedIn: 'root'})
export class VacationRequestService {

  constructor(private readonly http: HttpClient) {
  }

  public getVacationRequests(): Observable<VacationRequest[]> {
    return this.http.get<VacationRequest[]>('/api/vacation-requests');
  }

  public saveVacationRequest(request: VacationRequest): Observable<VacationRequest> {
    return this.http.post<VacationRequest>('/api/vacation-requests', request, {
      headers: {'Content-Type': 'application/json'}
    })
  }

  deleteVacationRequest(id: number): Observable<void> {
    return this.http.delete<void>(`/api/vacation-requests/${id}`);
  }

  public updateVacationRequest(request: VacationRequest): Observable<VacationRequest> {
    return this.http.put<VacationRequest>(`/api/vacation-requests/${request.id}`, request, {
      headers: {'Content-Type': 'application/json'}
    });
  }

  public filterVacationRequests(
    employeeId: number | null,
    startDate: Date | null,
    endDate: Date | null
  ): Observable<VacationRequest[]> {
    let params = new HttpParams();
    if (employeeId !== null) {
      params = params.set('employeeId', employeeId);
      console.log(params)
    }
    if (startDate !== null) {
      params = params.set('startDate', startDate.toLocaleDateString('en-CA'));
      console.log(params)
    }
    if (endDate !== null) {
      params = params.set('endDate', endDate.toLocaleDateString('en-CA'));
      console.log(params);
    }
    return this.http.get<VacationRequest[]>('/api/vacation-requests/filter', { params });
  }


}

