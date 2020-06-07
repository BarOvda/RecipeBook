import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserRedistrationService } from '../user-redistration.service';
import { Router, RouterModule } from '@angular/router';
import { ElementService } from '../element.service';
import { User } from '../user';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit {

  email: string;
  isValidEmail: Boolean;
  constructor(private service: UserRedistrationService, private router: Router, private elementService: ElementService) { }

  ngOnInit(): void {
    this.isValidEmail = true;
  }

  onSubmitLogIn() {
    this.isValidEmail = true;

    this.service.check(this.email).catch((err: HttpErrorResponse) => {

      console.log('An error occurred:', err.error);
      this.isValidEmail = false;

    }).then((result: User) => {

      if (this.isValidEmail) {
        this.service.emailName = result.email;
        this.elementService.specificUser(this.service.emailName, result.email);

      }
    });
  }

  gotoUserList() {
    this.router.navigate(['/feed']);
  }

  isValidEmailCheack() { return this.isValidEmail }


}
