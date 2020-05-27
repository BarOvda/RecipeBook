import { TestBed } from '@angular/core/testing';

import { UserRedistrationService } from './user-redistration.service';

describe('UserRedistrationService', () => {
  let service: UserRedistrationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserRedistrationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
