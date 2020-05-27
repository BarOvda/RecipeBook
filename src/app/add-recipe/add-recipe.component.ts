import { Component, OnInit } from '@angular/core';
import { UserRedistrationService } from '../user-redistration.service';
import { Router } from '@angular/router';
import { Element } from '../element'
import { ElementService } from '../element.service';



@Component({
  selector: 'app-add-recipe',
  templateUrl: './add-recipe.component.html',
  styleUrls: ['./add-recipe.component.css']
})
export class AddRecipeComponent implements OnInit {
  recipe: Element;
  ingridient: Element;
  name: string;
  inCount: number;
  element: Element;
  recipeElement: Element;
  ingridientElement: Element;
  count: number;
  image: string;
  step: string;
  ingridientName: string;
  ingridientAmount: string;
  attributeRecipe: Map<string, string> = new Map<string, string>();
  attributeingridient: Map<string, string> = new Map<string, string>();

  press: number;

  childId = {};

  countToExit: number;
  elementNumber: number;




  constructor(private userservice: UserRedistrationService, private elementService: ElementService, private router: Router) {
    this.recipe = new Element();
  }

  ngOnInit(): void {
    this.count = 1;
    this.inCount = 1;
    this.countToExit = 1;
    this.press = 0;
    this.element = new Element();
    this.recipeElement = new Element();
    this.ingridientElement = new Element();
  }

  onSubmitinfro() {
    this.attributeRecipe.set("image", this.image);

  }

  onSubmitAddIngredient() {
    this.attributeingridient.set(this.ingridientName, this.ingridientAmount);
    this.ingridientName = '';
    this.ingridientAmount = '';
    this.inCount++;
  }

  onSubmitAddAttribute() {

    this.attributeRecipe.set("" + this.count, this.step);
    this.count++;
    this.step = '';

  }

  onSubmitFinish() {
    this.saveRecipeInDB();

  }

  saveRecipeInDB() {
    this.recipe.createdBy["email"] = this.userservice.emailName;
    this.recipe.name = this.name;
    this.recipe.type = "recipe";
    this.recipe.active = true;

    this.attributeRecipe.forEach((value, key) => {
      this.recipe.elementAttributes[key] = value;
    })

    this.elementService.postElement(this.recipe, this.userservice.emailName).then(resule => {
      this.elementService.getElementByName(this.userservice.emailName, this.recipe.name).then(data => {
        this.recipeElement = data;
      }).then(() => {
        this.saveIngridientInDB();
      })
    });
  }

  saveIngridientInDB() {

    this.elementNumber = this.attributeingridient.size;
    this.attributeingridient.forEach((value, key) => {
      this.elementService.getElementByName(this.userservice.emailName, key)
        .then(data => {
          this.element = JSON.parse(JSON.stringify(data));
        }
        )
        .then(() => {
          if (Object.keys(this.element).length == 0) {
            this.newIngridient(value, key);
          }
          else {
            this.findIngridientByName(key, value);
          }
        }
        )
    });

  }

  newIngridient(amount: string, name: string) {

    this.ingridientElement.type = "ingridient";
    this.ingridientElement.name = name;
    this.ingridientElement.createdBy["email"] = this.userservice.emailName;
    this.ingridientElement.active = true;

    this.ingridientElement.elementAttributes[this.recipeElement[0].elementId] = amount;

    this.elementService.postElement(this.ingridientElement, this.userservice.emailName)
      .then(() => {
        this.elementService.getElementByName(this.userservice.emailName, name)
          .then(data => {
            this.element = JSON.parse(JSON.stringify(data));
          }).then(() => {
            this.connectBetweenElement(this.element[0].elementId);
          })
          .then(() => {
            this.exit();
          })
      })
  }

  findIngridientByName(name: string, amount: string) {

    this.attributeingridient.clear();

    for (var value in this.element[0].elementAttributes) {
      this.attributeingridient.set(value, this.element[0].elementAttributes[value])
    }
    this.element[0].elementAttributes = {};
    this.attributeingridient.set(this.recipeElement[0].elementId, amount);

    this.attributeingridient.forEach((value, key) => {
      this.element[0].elementAttributes[key] = value;
    })

    this.elementService.update(this.userservice.emailName, this.element[0].elementId, this.element[0]).
      then(data => {
        this.connectBetweenElement(this.element[0].elementId);
      }).then(() => {
        this.exit();
      })
  }

  connectBetweenElement(elementId: string) {

    this.childId["id"] = elementId;

    this.elementService.conectBetweenElement(this.userservice.emailName, this.recipeElement[0].elementId, this.childId)
      .then(() => {
        console.log()
      });
  }

  exit() {

    if (this.countToExit == this.elementNumber) {
      this.router.navigate(['']);

    }
    else {
      this.countToExit++;
    }
  }
}

