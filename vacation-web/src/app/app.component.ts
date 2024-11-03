import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { VacationListComponent } from './vacation/list/vacation-list.component';
import {VacationRequestFormComponent} from "./vacation/form/vacation-request-form.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, VacationListComponent, VacationRequestFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

}
