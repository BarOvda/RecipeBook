import { Component, OnInit } from '@angular/core';
import { UserRedistrationService } from 'src/app/user-redistration.service';
import { ElementService } from 'src/app/element.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { Element } from '../../element';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.css']
})
export class RecipeDetailComponent implements OnInit {
  recipe: Element;
  id: number;
  count: number;

  ingridient: string[] = [];
  steps: string[] = [];


  constructor(private userService: UserRedistrationService, private elementService: ElementService,
    private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.route.params
      .subscribe(
        (params: Params) => {
          this.id = +params['id'];
          this.steps = [];
          this.ingridient = [];
          this.count = 1;

          this.elementService.getSpecificElement(this.userService.emailName, "" + this.id)
            .then((data) => {
              this.recipe = data;
            }).then(() => {
              var step: string;
              for (var value in this.recipe.elementAttributes) {
                step = "step number " + value + " :" + this.recipe.elementAttributes[value];
                this.steps.push(step);
              }
            }).then(() => {
              this.steps = this.steps.slice(0, this.steps.length - 1);
            }).then(() => {
              var elementIngridient: Element;
              var ingridientToArray: string;
              this.elementService.getIngridient(this.userService.emailName, this.recipe.elementId)
                .then((data) => {
                  data.forEach(element => {
                    elementIngridient = element;
                    ingridientToArray = this.count + ": " + elementIngridient.name + "    "
                      + elementIngridient.elementAttributes[this.recipe.elementId];

                    this.ingridient.push(ingridientToArray);
                    this.count++;
                  });
                })
            })
        }
      )
  }
}
