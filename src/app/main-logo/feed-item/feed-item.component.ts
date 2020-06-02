import { Component, OnInit, Input } from '@angular/core';
import { Element } from 'src/app/element';
import { User } from 'src/app/user';
import { UserRedistrationService } from 'src/app/user-redistration.service';

@Component({
  selector: 'app-feed-item',
  templateUrl: './feed-item.component.html',
  styleUrls: ['./feed-item.component.css']
})
export class FeedItemComponent implements OnInit {
  @Input() feedRecipe:Element;
  owner:User;

  constructor(private userService:UserRedistrationService) { }

  ngOnInit(): void {
    this.owner = new User();
    this.userService.getUsersDetails(this.feedRecipe.createdBy["email"]).then((data) => {
      this.owner = data;
    });
  }

}
