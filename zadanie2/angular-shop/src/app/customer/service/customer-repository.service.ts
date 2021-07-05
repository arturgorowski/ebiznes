import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Customer} from '../../_models/Customer';


@Injectable()
export class CustomerRepositoryService {

    constructor(private http: HttpClient) { }

    addCustomer(customer: Customer) {
        const queryUrl = environment.apiHost + '/addcustomer';
        return this.http.post(queryUrl, customer);
    }

    getCustomerByUserId(userId: number) {
        const queryUrl = environment.apiHost + '/customeruser/' + userId;
        return this.http.get(queryUrl);
    }
}
