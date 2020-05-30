import { User } from './../../user';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-users-details',
  templateUrl: './users-details.component.html',
  styleUrls: ['./users-details.component.css']
})
export class UsersDetailsComponent implements OnInit {
  @Input() user:User;
  constructor() { }

  ngOnInit(): void {
  }

}
