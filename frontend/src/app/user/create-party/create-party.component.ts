import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup} from '@angular/forms';
import {BasePlaylistObject} from './base-playlist/base-playlists.object';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {PartyDto} from '../../party/authorized/party.dto';

@Component({
  selector: 'app-create-party',
  templateUrl: './create-party.component.html',
  styleUrls: ['./create-party.component.scss']
})
export class CreatePartyComponent implements OnInit {

  partyForm: FormGroup;
  basePlaylists: BasePlaylistObject[];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private httpClient: HttpClient) {
  }

  ngOnInit() {
    this.partyForm = this.formBuilder.group({
      partyName: ['', this.validatePartyname],
      password: '',
      downVoting: true,
      description: ''
    });

    this.route.data.subscribe((data: { basePlaylists: BasePlaylistObject[] }) => {
      this.basePlaylists = data.basePlaylists;
    });
  }

  validatePartyname(control: AbstractControl) {
    if (control.value.startsWith(' ')) {
      return { validPartyname: true };
    }
    return null;
  }

  createParty() {
    this.httpClient.post<PartyDto>(environment.apiUrl + '/api/party/create', this.partyForm.value)
      .subscribe(party => {
        let partyRoute = this.router.config.find(r => r.path == 'party/:id');
        partyRoute.data = {party: party};
        this.router.navigate(['/party', party.partyId]);
      });
  }

}
