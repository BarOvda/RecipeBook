import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/user';
import { UserRedistrationService } from 'src/app/user-redistration.service';
import { ElementService } from 'src/app/element.service';
import { Action } from 'src/app/action';

@Component({
  selector: 'app-user-item',
  templateUrl: './user-item.component.html',
  styleUrls: ['./user-item.component.css']
})
export class UserItemComponent implements OnInit {
  @Input() user: User;

  constructor(private userService: UserRedistrationService, private elementService: ElementService) { }

  ngOnInit(): void {
  }

  Follow() {

    var action: Action = new Action();

    action.type = "follow";
    action.invokedBy["email"] = this.userService.emailName;
    action.actionAttributes["followed"] = this.user.email;

    action.element["elementId"] = this.elementService.element.elementId;


    this.userService.follow(action).then((data) => {

    })



  }
}
