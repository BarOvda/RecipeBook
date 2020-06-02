import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Element } from './element'
import { Action } from './action';


@Injectable({
  providedIn: 'root'
})
export class ElementService {
  element: Element;
  recipeSelected = new EventEmitter<Element>();

  constructor(private http: HttpClient) { }

  public postElement(recipe: Element, email: string): Promise<any> {
    return this.http.post<Element>("http://localhost:8091/acs/elements/" + email, recipe).toPromise();
  }

  public getElementByName(email: string, name: string): Promise<any> {
    return this.http.get<Element>("http://localhost:8091/acs/elements/" + email + "/search/byName/" + name).toPromise();
  }

  public getElementByType(email: string, type: string): Promise<any> {
    return this.http.get<Element>("http://localhost:8091/acs/elements/" + email + "/search/byType/" + type).toPromise();
  }

  public conectBetweenElement(email: string, id: string, childId: any): Promise<any> {
    return this.http.put("http://localhost:8091/acs/elements/" + email + "/" + id + "/children", childId).toPromise();
  }

  public update(email: string, id: string, element: Element): Promise<any> {
    return this.http.put("http://localhost:8091/acs/elements/" + email + "/" + id, element).toPromise();
  }

  public getSpecificElement(email: string, id: string): Promise<any> {
    return this.http.get("http://localhost:8091/acs/elements/" + email + "/" + id).toPromise();
  }

  public getIngridient(email: string, id: string): Promise<any> {
    return this.http.get("http://localhost:8091/acs/elements/" + email + "/" + id + "/children").toPromise();
  }

  public getRecipeByEmail(action: Action): Promise<any> {
    return this.http.post<Action>("http://localhost:8091/acs/actions", action).toPromise();
  }


  public createUserElement(userElement: Element, email: string) {
    this.postElement(userElement, email).then(() => {
      this.getElementByType(email, userElement.type).then((data: Element) => {
        this.element = data[0];
      })
    })
  }
  public specificUser(email: string, name: string) {
    this.getElementByName(email, name).then((data: Element) => {
      this.element = new Element();
      this.element = data[0];
      console.log(this.element);
    })

  }
}



