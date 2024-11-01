import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { VacationRequest } from '../vacation-request';
import { VacationRequestService } from '../vacation-request.service';

@Component({
  standalone: true,
  selector: 'vacation-list',
  imports: [NgFor],
  templateUrl: 'vacation-list.component.html',
  styleUrl: 'vacation-list.component.scss'
})
export class VacationListComponent implements OnInit {

  public vacations: VacationRequest[] = [];

  constructor(private vacationRequestService: VacationRequestService) {
  }

  public ngOnInit(): void {
    this.vacationRequestService.getVacationRequests().subscribe(response => this.vacations = response);
  }

}
