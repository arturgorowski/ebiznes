import {Component, OnInit} from '@angular/core';
import {Cart} from '../../_models/Cart';
import {ActivatedRoute} from '@angular/router';
import {CartRepositoryService} from '../service/cart-repository.service';
import {ShopStorage} from '../../_helpers/ShopStorage';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

    cart: Cart;
    constructor(protected route: ActivatedRoute,
                protected cartRepository: CartRepositoryService) {
    }

    ngOnInit(): void {
        this.loadCart();
    }

    loadCart() {
        this.cart = this.route.snapshot.data.product;
        console.log(this.cart);
        if (!this.cart) {
            console.log('cart sdafsa');
            this.cartRepository.getCustomerCart(1).subscribe((cart: Cart) => {
                ShopStorage.setCart(cart.id);
                this.cart = cart;
            });
        }
    }
}
