import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserRedistrationService } from '../user-redistration.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit {

  email: string;

  constructor(private service: UserRedistrationService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmitLogIn() {

    this.service.check(this.email).subscribe(result => {
      this.gotoUserList();
    });
  }

  gotoUserList() {
    this.router.navigate(['']);
  }




}
