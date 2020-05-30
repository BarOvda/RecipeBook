import { Component, OnInit } from '@angular/core';
import { UserRedistrationService } from '../user-redistration.service';
import { User } from '../user';
import { Router } from '@angular/router';


@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {

  user = new User()

  constructor(private service: UserRedistrationService, private router: Router) { }

  ngOnInit(): void {
    this.service.check(this.service.emailName)
      .then((data: User) => this.user = { ...data });
  }

  onSubmitChange() {
    this.service.changeDetails(this.user).subscribe(result => {
      this.gotoUserList();
    });
  }

  gotoUserList() {
    this.router.navigate(['/feed']);
  }



}