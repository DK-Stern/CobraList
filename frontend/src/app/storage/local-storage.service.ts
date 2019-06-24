import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() {
  }

  saveItem(key: STORAGE_KEY, item: Object) {
    localStorage.setItem(key, JSON.stringify(item));
  }

  loadItem(key: STORAGE_KEY): Object {
    return JSON.parse(localStorage.getItem(key));
  }
}

export enum STORAGE_KEY {
  USER = 'USER'
}
