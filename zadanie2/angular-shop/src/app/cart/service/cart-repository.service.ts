import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {ShopStorage} from '../../_helpers/ShopStorage';
import {EMPTY} from 'rxjs';
import {Cart, CartItem} from '../../_models/Cart';

@Injectable()
export class CartRepositoryService {

    constructor(private http: HttpClient) { }

    getCarts() {
        const queryUrl = environment.apiHost + '/carts';
        return this.http.get(queryUrl);
    }

    addCart(cart: Cart) {
        const queryUrl = environment.apiHost + '/addcart';
        return this.http.post(queryUrl, cart);
    }

    getCart() {
        const cartId = ShopStorage.getCart();
        if (cartId) {
            const queryUrl = environment.apiHost + '/cart/' + cartId;
            return this.http.get(queryUrl);
        } else {
            return EMPTY;
        }
    }

    getCustomerCarts(customerId: number) {
        const queryUrl = environment.apiHost + '/customercart/' + customerId;
        return this.http.get(queryUrl);
    }

    addCartItem(cartItem: CartItem) {
        const queryUrl = environment.apiHost + '/addcartitem';
        return this.http.post(queryUrl, cartItem);
    }
}
