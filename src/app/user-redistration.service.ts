import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from './user';
import { Action } from './action'
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

  public save(user: User): Promise<any> {
    this.emailName = user.email;
    return this.http.post<User>("http://localhost:8091/acs/users", user).toPromise();
  }

  public check(email: string): Promise<any> {
    this.emailName = email;
    return this.http.get<User>("http://localhost:8091/acs/users/login/" + email).toPromise();
  }

  public changeDetails(user: User) {
    return this.http.put<User>('http://localhost:8091/acs/users/' + this.emailName, user);
  }

  public searchUser(action: Action): Promise<any> {
    return this.http.post<Action>('http://localhost:8091/acs/action', action).toPromise();
  }


}
