import { Action } from './action';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from './user';
import { HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class UserRedistrationService {

  emailName: string = '';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  constructor(private http: HttpClient) { }

  public save(user: User) {
    this.emailName = user.email;
    return this.http.post<User>("http://localhost:8091/acs/users", user);
  }

  public check(email: string) {
    this.emailName = email;
    return this.http.get<User>("http://localhost:8091/acs/users/login/" + email)
  }

  public changeDetails(user: User) {
    return this.http.put<User>('http://localhost:8091/acs/users/' + this.emailName, user);
  }

  public searchUser(action: Action):Promise<any> {
    console.log(action.invokedBy["email"]);
    return this.http.post<Action>('http://localhost:8091/acs/actions' ,action).toPromise();
  }

}
