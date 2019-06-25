import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-create-party',
  templateUrl: './create-party.component.html',
  styleUrls: ['./create-party.component.scss']
})
export class CreatePartyComponent implements OnInit {

  partyForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.partyForm = this.formBuilder.group({
      partyName: ['', this.validatePartyname],
      password: '',
      downVoting: true,
      description: ''
    })
  }

  validatePartyname(control: AbstractControl) {
    if (control.value.startsWith(' ')) {
      return { validPartyname: true };
    }
    return null;
  }

  createParty() {
    console.log(this.partyForm.value);
  }

}
