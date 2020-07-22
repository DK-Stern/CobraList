import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButton, MatIconRegistry, MatStepper } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { JoinPartyService } from './join-party.service';
import { FindPartyService } from '../find-party/find-party.service';
import { FindPartyDto } from '../find-party/find-party.dto';
import { JoinPartyDto } from './join-party.dto';
import { loginGuestSuccess } from '../../authentication/store/auth.actions';
import { Store } from '@ngrx/store';
import { AppState } from '../../storage/app-state.reducer';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserDto } from '../../user/user.dto';
import {
  LocalStorageService,
  STORAGE_KEY,
} from '../../storage/local-storage.service';
import { UserRoles } from '../../user/user.roles';

@Component({
  selector: 'app-join-party',
  templateUrl: './join-party.component.html',
  styleUrls: ['./join-party.component.scss'],
})
export class JoinPartyComponent implements OnInit, AfterViewInit {
  @ViewChild('findPartyButton', { static: false }) findPartyButton: MatButton;

  party: FindPartyDto;
  hasPassword = true;
  partyForm: FormGroup;
  passwordForm: FormGroup;
  joinForm: FormGroup;
  partyId = null;

  constructor(
    private formBuilder: FormBuilder,
    private findPartyService: FindPartyService,
    private joinPartyService: JoinPartyService,
    private store: Store<AppState>,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private localStorageService: LocalStorageService,
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer
  ) {
    iconRegistry.addSvgIcon(
      'create',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/create.svg')
    );
    iconRegistry.addSvgIcon(
      'done',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/done.svg')
    );
    iconRegistry.addSvgIcon(
      'warning',
      sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/warning.svg')
    );
  }

  ngOnInit() {
    this.partyForm = this.formBuilder.group({
      partyCodeField: ['', Validators.required],
    });
    this.passwordForm = this.formBuilder.group({
      passwordField: ['', Validators.required],
    });
    this.joinForm = this.formBuilder.group({
      guestName: ['', Validators.required],
    });
    this.route.paramMap.subscribe((params) => {
      this.partyId = params.get('id');
    });
  }

  ngAfterViewInit() {
    if (this.partyId != null) {
      this.findPartyButton._getHostElement().click();
    }
  }

  findPartyById(stepper: MatStepper) {
    const partyIdField = this.partyForm.get('partyCodeField');

    this.findPartyService.findParty(partyIdField.value).subscribe(
      (party) => {
        this.party = party;
        this.hasPassword = party.hasPassword;
        stepper.next();
      },
      () => partyIdField.setErrors({ partyNotFound: true })
    );
  }

  joinParty() {
    let guestName: string = this.joinForm.get('guestName').value;

    const joinPartyDto: JoinPartyDto = {
      partyCode: this.partyForm.get('partyCodeField').value,
      partyPassword: this.passwordForm.get('passwordField').value,
      guestName,
    };

    this.joinPartyService.joinParty(joinPartyDto).subscribe(
      (response) => {
        guestName = this.joinForm.get('guestName').value;
        const guest: UserDto = {
          id: null,
          name: guestName,
          email: null,
          authorities: [UserRoles.GUEST],
        };
        this.localStorageService.saveItem(STORAGE_KEY.TOKEN, response.token);
        this.localStorageService.saveItem(STORAGE_KEY.USER, guest);
        this.store.dispatch(
          loginGuestSuccess({ token: response.token, guest })
        );
        this.router
          .navigate(['/party', response.partyCode])
          .catch((error) => console.log(error));
      },
      (error) => {
        if (error.status === 403) {
          const passwordField = this.passwordForm.get('passwordField');
          passwordField.setErrors({ wrongPassword: true });
        } else if (error.status === 409) {
          const guestNameField = this.joinForm.get('guestName');
          guestNameField.setErrors({ nameAlreadyExists: true });
        }

        this.snackBar.open(error.error.message, 'OK', {
          duration: 5000,
        });
      }
    );
  }
}
