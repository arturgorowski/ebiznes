import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CartItem} from '../../_models/Cart';
import {CartRepositoryService} from '../service/cart-repository.service';

@Component({
    selector: 'app-cart-item',
    templateUrl: './cart-item.component.html',
    styleUrls: ['./cart-item.component.scss']
})
export class CartItemComponent implements OnInit {

    @Input() cartItem: CartItem;
    @Input() cartId: number;
    @Output() removeCartItem = new EventEmitter<number>();
    constructor(protected cartRepository: CartRepositoryService) { }

    ngOnInit(): void {
        console.log(this.cartItem);
    }
}
