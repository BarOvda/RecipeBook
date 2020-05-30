import { Component, OnInit, Input } from '@angular/core';
import { UserRedistrationService } from '../user-redistration.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  headerEmail: string;
  usernameSearch: string;

  constructor(private service: UserRedistrationService, private router: Router) { }

  ngOnInit(): void {
    this.headerEmail = '';
  }

  emailName() {
    return this.headerEmail = this.service.emailName;
  }

  onSearchUser() {
    this.router.navigate(['/search-page/' + this.usernameSearch]);
  }

}
