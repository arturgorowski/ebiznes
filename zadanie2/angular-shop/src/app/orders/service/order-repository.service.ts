import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable()
export class OrderRepositoryService {

    constructor(private http: HttpClient) { }

    getOrders() {
        const queryUrl = environment.apiHost + '/orders';
        return this.http.get(queryUrl);
    }

    getOrder(id: number) {
        const queryUrl = environment.apiHost + '/order/' + id;
        return this.http.get(queryUrl);
    }

    getCustomerOrders(customerId: number) {
        const queryUrl = environment.apiHost + '/customerorder/' + customerId;
        return this.http.get(queryUrl);
    }
}
