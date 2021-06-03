import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {OrderRepositoryService} from './order-repository.service';

@Injectable()
export class OrdersResolveService implements Resolve<any> {
    constructor(private orderRepository: OrderRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        return this.orderRepository.getOrders();
    }

}
