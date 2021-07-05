import {Component, Input} from '@angular/core';
import {Order} from '../../_models/Order';

@Component({
    selector: 'app-order-details',
    templateUrl: './order-details.component.html',
    styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent {
    @Input() order: Order;
}
