import { Component, OnInit, Input } from '@angular/core';
import { Element } from '../../../element'

@Component({
  selector: 'app-recipe-item',
  templateUrl: './recipe-item.component.html',
  styleUrls: ['./recipe-item.component.css']
})
export class RecipeItemComponent implements OnInit {
  @Input() recipe: Element;
  constructor() { }

  ngOnInit(): void {
  }

}
