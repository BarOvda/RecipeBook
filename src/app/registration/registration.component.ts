import { Component, OnInit, Output } from '@angular/core';
import { User } from '../user';
import { UserRedistrationService } from '../user-redistration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user: User;

  constructor(private service: UserRedistrationService, private router: Router) {
    this.user = new User();
  }

  ngOnInit(): void {
  }

  onSubmitRegi() {
    this.service.save(this.user).subscribe(result => this.gotoUserList());
  }

  gotoUserList() {
    this.router.navigate(['']);
  }

}
