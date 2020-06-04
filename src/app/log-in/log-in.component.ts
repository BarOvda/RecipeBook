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
  isValidEmail:Boolean;
  constructor(private service: UserRedistrationService, private router: Router, private elementService: ElementService) { }

  ngOnInit(): void {
    this.isValidEmail = true;
  }

  onSubmitLogIn() {
    this.isValidEmail=true;

    this.service.check(this.email) .catch((err: HttpErrorResponse) => {
      // simple logging, but you can do a lot more, see below
      
      console.log('An error occurred:', err.error);
      this.isValidEmail=false;
      
    }).then((result: User) => {
      console.log(this.isValidEmail);
      if(this.isValidEmail){
        this.service.emailName = result.email;
      console.log(result);
     
      console.log(result);
      this.elementService.specificUser(this.service.emailName, result.email);
      
      this.gotoUserList();
      }
    });


    
  }

  gotoUserList() {
    this.router.navigate(['/feed']);
  }

isValidEmailCheack(){return this.isValidEmail}


}
