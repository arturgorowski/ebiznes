import {Component, OnInit} from '@angular/core';
import {CustomerStorage} from '../_helpers/CustomerStorage';
import {Customer} from '../_models/Customer';

@Component({
    selector: 'app-customer',
    templateUrl: './customer.component.html',
    styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {

    user: Customer;

    ngOnInit(): void {
        this.user = CustomerStorage.getUser();
        console.log(this.user);
    }

}
