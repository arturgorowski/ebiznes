import {Component, OnInit} from '@angular/core';
import {Order} from '../../_models/Order';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'app-orders-list',
    templateUrl: './orders-list.component.html',
    styleUrls: ['./orders-list.component.scss']
})
export class OrdersListComponent implements OnInit {

    orders: Order[] = [];
    constructor(protected route: ActivatedRoute) { }

    ngOnInit(): void {
        this.loadOrders();
    }

    loadOrders() {
        this.orders = this.route.snapshot.data.orders;
    }
}
