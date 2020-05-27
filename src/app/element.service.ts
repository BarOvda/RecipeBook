import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Element } from './element'


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

  public getAllElementByEmail(email: string): Promise<any> {
    return this.http.get("http://localhost:8091/acs/elements/" + email).toPromise();
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


}
