import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {CartRepositoryService} from './cart-repository.service';

@Injectable()
export class CartResolveService implements Resolve<any> {
    constructor(private cartRepository: CartRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        return this.cartRepository.getCart();
    }

}
