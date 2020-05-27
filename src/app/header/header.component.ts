import { Component, OnInit, Input } from '@angular/core';
import { UserRedistrationService } from '../user-redistration.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  headerEmail: string;

  constructor(private service: UserRedistrationService) { }

  ngOnInit(): void {
    this.headerEmail = '';
  }

  emailName() {
    return this.headerEmail = this.service.emailName;
  }

}
