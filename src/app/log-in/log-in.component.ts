import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserRedistrationService } from '../user-redistration.service';
import { Router, RouterModule } from '@angular/router';
import { ElementService } from '../element.service';
import { User } from '../user';


@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit {

  email: string;

  constructor(private service: UserRedistrationService, private router: Router, private elementService: ElementService) { }

  ngOnInit(): void {
  }

  onSubmitLogIn() {

    this.service.check(this.email).then((result: User) => {
      console.log(result);
      this.elementService.specificUser(this.service.emailName, result.email);

    }).then(() => {
      this.gotoUserList();
    });
  }

  gotoUserList() {
    this.router.navigate(['/feed']);
  }




}
