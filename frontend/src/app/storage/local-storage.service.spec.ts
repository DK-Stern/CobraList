import {TestBed} from '@angular/core/testing';

import {LocalStorageService, STORAGE_KEY} from './local-storage.service';

describe('LocalStorageService', () => {

  let testSubject: LocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});

    testSubject = TestBed.get(LocalStorageService);
  });

  it('should be created', () => {
    const service: LocalStorageService = TestBed.get(LocalStorageService);
    expect(service).toBeTruthy();
  });

  it('save item', () => {
    // given
    const expectedObject = {user: 'peter'};

    // when
    testSubject.saveItem(STORAGE_KEY.USER, expectedObject);

    // then
    expect(JSON.parse(localStorage.getItem(STORAGE_KEY.USER))).toEqual(expectedObject);
  });

  it('load item', () => {
    // given
    const expectedObject = {user: 'peter'};
    localStorage.setItem(STORAGE_KEY.USER, JSON.stringify(expectedObject));

    // when
    const resultedObject = testSubject.loadItem(STORAGE_KEY.USER);

    // then
    expect(resultedObject).toEqual(expectedObject);
  });
});
