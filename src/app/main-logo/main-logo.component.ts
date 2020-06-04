import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserRedistrationService } from '../user-redistration.service';
import { Element } from '../element';
import { Action } from '../action';
import { ElementService } from '../element.service';

@Component({
  selector: 'app-main-logo',
  templateUrl: './main-logo.component.html',
  styleUrls: ['./main-logo.component.css']
})
export class MainLogoComponent implements OnInit {
  recipeName: string;
  feed:Element[];
  page: number = 0;
  pages: number[] = [];
  
  constructor(private router: Router,private userService: UserRedistrationService,private elementService:ElementService) { }


  ngOnInit(): void {
    this.recipeName = '';
    console.log("feed!");
    this.getFeed();

  } 

  onSearchRecipe() {
    this.router.navigate(['/SearchByRecipeName/'], { queryParams: { searchRecipe: this.recipeName } }).then(() => {
      this.recipeName = '';
    });
  }  

  getFeed() {
    
    console.log("im getting the feed!");
    var action: Action = new Action();
    this.feed = [];
    this.pages = [];

    action.actionAttributes["page"] = this.page;
    action.element["elementId"] = this.elementService.element.elementId;
    action.invokedBy["email"] = this.userService.emailName;
    console.log(this.userService.emailName);
    action.type = "getFeed";
    console.log(action);

    this.userService.getFeed(action).then((data) => {
      this.feed = data;

      for (let i = 0; i < (parseInt(("" + (this.feed.length / 10 + 1)))); i++) {
        this.pages.push(i);
      }
    });
  }

  setPage(i: number, event: Event) {

    event.preventDefault();
    this.page = i;
    this.getFeed();

  }




}
