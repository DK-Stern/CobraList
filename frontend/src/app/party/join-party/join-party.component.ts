import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatIconRegistry, MatStepper} from '@angular/material';
import {DomSanitizer} from '@angular/platform-browser';
import {JoinPartyService} from './join-party.service';
import {FindPartyService} from '../find-party/find-party.service';
import {FindPartyDto} from '../find-party/find-party.dto';
import {JoinPartyDto} from './join-party.dto';

@Component({
  selector: 'app-join-party',
  templateUrl: './join-party.component.html',
  styleUrls: ['./join-party.component.scss']
})
export class JoinPartyComponent implements OnInit {

  party: FindPartyDto;
  hasPassword: boolean = true;
  partyForm: FormGroup;
  passwordForm: FormGroup;
  joinForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private findPartyService: FindPartyService,
              private joinPartyService: JoinPartyService,
              iconRegistry: MatIconRegistry,
              sanitizer: DomSanitizer) {
    iconRegistry.addSvgIcon(
      'create',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/create.svg'));
    iconRegistry.addSvgIcon(
      'done',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/done.svg'));
    iconRegistry.addSvgIcon(
      'warning',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/warning.svg'));
  }

  ngOnInit() {
    this.partyForm = this.formBuilder.group({
      partyCodeField: ['', Validators.required]
    });
    this.passwordForm = this.formBuilder.group({
      passwordField: ['', Validators.required]
    });
    this.joinForm = this.formBuilder.group({
      guestName: ['', Validators.required]
    });
  }

  findPartyById(stepper: MatStepper) {
    const partyIdField = this.partyForm.get('partyCodeField');

    this.findPartyService.findParty(partyIdField.value)
      .subscribe(party => {
          this.party = party;
          this.hasPassword = party.hasPassword;
          stepper.next();
        }
        , error => partyIdField.setErrors({'partyNotFound': true}));
  }

  joinParty() {
    const guestName: string = this.joinForm.get('guestName').value;

    let joinPartyDto: JoinPartyDto = {
      partyCode: this.partyForm.get('partyCodeField').value,
      partyPassword: this.passwordForm.get('passwordField').value,
      guestName: guestName,
    };

    this.joinPartyService.joinParty(joinPartyDto);
  }
}