import { Component, OnInit } from '@angular/core';
import { Element } from '../element';
import { ElementService } from '../element.service';

@Component({
  selector: 'app-recipes',
  templateUrl: './recipes.component.html',
  styleUrls: ['./recipes.component.css']
})
export class RecipesComponent implements OnInit {
  selectedRecipe: Element;

  constructor(private elementService: ElementService) { }

  ngOnInit(): void {
    this.elementService.recipeSelected
      .subscribe(
        (recipe: Element) => {
          this.selectedRecipe = recipe;
        }
      )
  }

}
