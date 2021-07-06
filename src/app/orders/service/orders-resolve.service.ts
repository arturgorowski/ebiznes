import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {OrderRepositoryService} from './order-repository.service';
import {CustomerStorage} from '../../_helpers/CustomerStorage';

@Injectable()
export class OrdersResolveService implements Resolve<any> {
    constructor(private orderRepository: OrderRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        const customerId = CustomerStorage.getUser().id;
        return this.orderRepository.getCustomerOrders(customerId);
    }

}
