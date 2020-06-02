import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { Action } from '../action';
import { UserRedistrationService } from '../user-redistration.service';
import { ActivatedRoute } from '@angular/router';
import { ElementService } from '../element.service';
import { Element } from '../element';

@Component({
  selector: 'app-search-users',
  templateUrl: './search-users.component.html',
  styleUrls: ['./search-users.component.css']
})
export class SearchUsersComponent implements OnInit {
  searchResult: User[] = [];
  element: Element;
  page: number = 0;
  pages: number[] = [];
  user: string;

  constructor(private userService: UserRedistrationService, private router: ActivatedRoute, private elementService: ElementService) { }

  setPage(i: number, event: Event) {

    event.preventDefault();
    this.page = i;
    this.getUsersByUsername();
  }


  ngOnInit(): void {
    this.getUsersByUsername();
  }

  getUsersByUsername() {
    var action: Action = new Action();

    this.searchResult = [];
    this.pages = [];

    action.type = "searchUser";
    action.invokedBy["email"] = this.userService.emailName;
    action.actionAttributes["page"] = this.page;

    this.router.queryParams.subscribe(params => {
      this.user = params.searchUsername;
    })

    action.actionAttributes["username"] = this.user;
    action.element["elementId"] = this.elementService.element.elementId;


    this.userService.searchUser(action).then((data) => {
      this.searchResult = data;

      for (let i = 0; i < (parseInt(("" + (this.searchResult.length / 10 + 1)))); i++) {
        this.pages.push(i);
      }

      console.log(this.pages);
    })
  }
}
