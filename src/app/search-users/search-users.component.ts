import { ActivatedRoute } from '@angular/router';
import { Action } from './../action';
import { UserRedistrationService } from 'src/app/user-redistration.service';
import { User } from './../user';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-search-users',
  templateUrl: './search-users.component.html',
  styleUrls: ['./search-users.component.css']
})
export class SearchUsersComponent implements OnInit {
  searchResult : User[] = [];

  action :Action;
  pageIndex:Number;
  constructor(private service: UserRedistrationService,private router:ActivatedRoute) { }

  ngOnInit(): void {
    this.pageIndex = 0;
    this.action = new Action();
    this.action.actionAttributes["username"] = this.router.snapshot.params['username'];

    this.action.type="searchUser";
    this.action.invokedBy["email"] = this.service.emailName; 
    console.log(this.service.emailName);
    console.log(this.action.invokedBy["email"]);
    console.log(this.action.actionAttributes["username"]);
    this.service.searchUser(this.action).then(data=>
      {
        data.forEach((user: User) => {
          this.searchResult.push(user);  
                
        })
         });
      
  }

}
