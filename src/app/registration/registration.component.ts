import { Component, OnInit, Output } from '@angular/core';
import { User } from '../user';
import { UserRedistrationService } from '../user-redistration.service';
import { Router } from '@angular/router';
import { ElementService } from '../element.service';
import { Element } from '../element'

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user: User;
  userElement: Element;

  constructor(private service: UserRedistrationService, private router: Router, private elementService: ElementService) {
    this.user = new User();
  }

  ngOnInit(): void {
    this.userElement = new Element();
  }

  onSubmitRegi() {
    this.service.save(this.user)
      .then(() => {
        this.userElement.type = "user"
        this.userElement.name = this.user.email;
        this.userElement.createdBy["email"] = this.user.email;
        this.userElement.active = true;

        this.elementService.createUserElement(this.userElement, this.user.email);
        this.gotoUserList();
      })
  }

  gotoUserList() {
    this.router.navigate(['/feed']);
  }

}
