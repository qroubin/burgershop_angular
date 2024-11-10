import { TestBed } from '@angular/core/testing';

import { BurgerService } from './burger.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Burger } from './burger';

describe('BurgerService', () => {
  let service: BurgerService;

  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute, BurgerService,
          useValue: { paramMap: of({ get: (key: string) => '123' }) }
        },
        BurgerService
      ],
    });
    service = TestBed.inject(BurgerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure that there are no outstanding HTTP requests after each test
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve burgers from the API via GET', () => {
    const dummyBurgers: Burger[] = [{ id: 1, name: 'Cheeseburger', ingredients: ['cheese', 'beef'] }];

    service.getBurgers().subscribe(burgers => {
      expect(burgers.length).toBe(1);
      expect(burgers).toEqual(dummyBurgers);
    });

    const req = httpMock.expectOne(service.burgersApiUrl);
    expect(req.request.method).toBe('GET');
    req.flush({ _embedded: { burgerList: dummyBurgers } });
  });

  it('should retrieve a single burger from the API via GET', () => {
    const dummyBurger: Burger = { id: 1, name: 'Cheeseburger', ingredients: ['cheese', 'beef'] };

    service.getBurger(1).subscribe(burger => {
      expect(burger).toEqual(dummyBurger);
    });

    const req = httpMock.expectOne(`${service.burgersApiUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyBurger);
  });

  it('should delete a burger via DELETE', () => {
    const dummyBurger: Burger = { id: 1, name: 'Cheeseburger', ingredients: ['cheese', 'beef'] };

    service.deleteBurger(1).subscribe(burger => {
      expect(burger).toEqual(dummyBurger);
    });

    const req = httpMock.expectOne(`${service.burgersApiUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(dummyBurger);
  });

  it('should add a burger via POST', () => {
    const newBurger: Burger = { id: 2, name: 'Double Cheeseburger', ingredients: ['cheese', 'beef', 'bacon'] };

    service.addBurger(newBurger).subscribe(burger => {
      expect(burger).toEqual(newBurger);
    });

    const req = httpMock.expectOne(service.burgersApiUrl);
    expect(req.request.method).toBe('POST');
    req.flush(newBurger);
  });
});