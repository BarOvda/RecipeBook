import { Component, OnInit } from '@angular/core';
import { Element } from '../../element'
import { ElementService } from 'src/app/element.service';
import { UserRedistrationService } from 'src/app/user-redistration.service';
import { Router } from '@angular/router';
import { Action } from '../../action';


@Component({
  selector: 'app-recipe-list',
  templateUrl: './recipe-list.component.html',
  styleUrls: ['./recipe-list.component.css']
})
export class RecipeListComponent implements OnInit {
  recipes: Element[] = [];
  element: Element;
  page: number = 0;
  pages: number[] = [];

  constructor(private elementService: ElementService, private userService: UserRedistrationService, private router: Router) { }
  setPage(i: number, event: Event) {

    event.preventDefault();
    this.page = i;
    this.getRecipeByEmail();

  }

  ngOnInit(): void {
    this.getRecipeByEmail();
  }


  getRecipeByEmail() {
    var action: Action = new Action();
    this.recipes = [];
    this.pages = [];

    action.type = "getAllUsersRecpies";
    action.invokedBy["email"] = this.userService.emailName;
    action.actionAttributes["page"] = this.page;
    action.element["elementId"] = this.elementService.element.elementId;

    this.elementService.getRecipeByEmail(action).then((data) => {
      this.recipes = data;

      for (let i = 0; i < (parseInt(("" + (this.recipes.length / 10 + 1)))); i++) {
        this.pages.push(i);
      }
    })
  }
}
