import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {BasePlaylistObject} from './base-playlists.object';
import {EMPTY, Observable, of} from 'rxjs';
import {BasePlaylistService} from './base-playlist.service';
import {mergeMap, take} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class BasePlaylistResolverService implements Resolve<BasePlaylistObject[]> {

  constructor(private basePlaylistService: BasePlaylistService,
              private _snackBar: MatSnackBar) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<BasePlaylistObject[]> | Observable<never> {
    return this.basePlaylistService.getBasePlaylists().pipe(
      take(1),
      mergeMap(basePlaylistsObject => {
        if (basePlaylistsObject) {
          return of(basePlaylistsObject);
        } else {
          this._snackBar.open('Es konnten keine Basis-Playlisten geladen werden.', 'Ok', {
            duration: 0,
          });
          return EMPTY;
        }
      })
    );
  }
}
