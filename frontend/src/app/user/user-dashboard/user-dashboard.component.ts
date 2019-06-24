import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MatIconRegistry} from '@angular/material';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss']
})
export class UserDashboardComponent implements OnInit {

  constructor(public router: Router, iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
    iconRegistry.addSvgIcon(
      'music-note',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/baseline-music_note-24px.svg'));
  }

  ngOnInit() {
  }

}
