import { Component, OnInit } from '@angular/core';
import { Element } from '../../element'
import { ElementService } from 'src/app/element.service';
import { UserRedistrationService } from 'src/app/user-redistration.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-recipe-list',
  templateUrl: './recipe-list.component.html',
  styleUrls: ['./recipe-list.component.css']
})
export class RecipeListComponent implements OnInit {
  recipes: Element[] = [];
  element: Element;
  constructor(private elementService: ElementService, private userService: UserRedistrationService, private router: Router) { }

  ngOnInit(): void {
    this.element = new Element();
    this.getAllRecipeByEmail();
  }

  getAllRecipeByEmail() {
    this.elementService.getAllElementByEmail(this.userService.emailName)
      .then(data => {
        data.forEach((element: Element) => {
          this.checkElement(element);
        })
      })
  }

  checkElement(element: Element) {
    if (element.type === 'recipe' && element.createdBy['email'] === this.userService.emailName) {
      this.recipes.push(element);
    }
  }

}
