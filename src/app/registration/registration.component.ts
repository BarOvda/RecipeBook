import { Component, OnInit, Output } from '@angular/core';
import { User } from '../user';
import { UserRedistrationService } from '../user-redistration.service';
import { Router } from '@angular/router';
import { ElementService } from '../element.service';
import { Element } from '../element'
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user: User;
  userElement: Element;
  isValidEmail: Boolean;

  constructor(private service: UserRedistrationService, private router: Router, private elementService: ElementService) {
    this.user = new User();
  }

  ngOnInit(): void {
    this.userElement = new Element();
    this.isValidEmail = true;
  }

  onSubmitRegi() {


    this.isValidEmail = true;
    this.service.save(this.user)
      .catch((err: HttpErrorResponse) => {

        console.log('An error occurred:', err.error);
        this.isValidEmail = false;

      }).

      then((result: User) => {
        if (this.isValidEmail) {
          this.service.emailName = result.email;

          this.userElement.type = "user"
          this.userElement.name = this.user.email;
          this.userElement.createdBy["email"] = this.user.email;
          this.userElement.active = true;

          this.elementService.createUserElement(this.userElement, this.user.email);
        }
      })
  }

  isValidEmailCheack() { return this.isValidEmail; }

}
