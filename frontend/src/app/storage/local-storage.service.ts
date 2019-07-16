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

  removeItem(key: STORAGE_KEY) {
    localStorage.removeItem(key);
  }
}

export enum STORAGE_KEY {
  USER = 'USER',
  TOKEN = 'TOKEN'
}
