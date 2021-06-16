import {Component, OnInit} from '@angular/core';
import {Cart, CartItem} from '../../_models/Cart';
import {ActivatedRoute, Router} from '@angular/router';
import {CartRepositoryService} from '../service/cart-repository.service';
import {ShopStorage} from '../../_helpers/ShopStorage';
import {switchMap} from 'rxjs/operators';
import {Order} from '../../_models/Order';
import {OrderRepositoryService} from '../../orders/service/order-repository.service';
import swal from 'sweetalert2';
import {Product} from '../../_models/Product';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

    cart: Cart;
    cartItems: CartItem[];
    totalProductPrice = 0;
    constructor(protected route: ActivatedRoute,
                protected router: Router,
                protected cartRepository: CartRepositoryService,
                protected orderRepository: OrderRepositoryService) {
    }

    ngOnInit(): void {
        this.loadCart();
    }

    loadCart() {
        this.cart = this.route.snapshot.data.cart;
        console.log(this.cart);
        if (!this.cart) {
            this.cartRepository.getCustomerCart(1)
                .pipe(switchMap((cart: Cart) => {
                    ShopStorage.setCart(cart.id);
                    this.cart = cart;
                    return this.loadCartItems(cart.id);
                })).subscribe((cartItems: CartItem[]) => {
                    console.log(cartItems);
                    this.cartItems = cartItems;
                    this.calculatePrice();
            });
        } else {
            this.loadCartItems(this.cart.id).subscribe((cartItem: CartItem[]) => {
                console.log(cartItem);
                this.cartItems = cartItem;
                this.calculatePrice();
            });
        }
    }

    loadCartItems(cartId: number) {
        return this.cartRepository.getCartItems(cartId);
    }

    calculatePrice() {
        this.totalProductPrice = 0;
        // @ts-ignore
        this.cartItems.forEach((cartItem: CartItem) => this.totalProductPrice += cartItem.product.price);
    }

    removeCartItem(cartItem) {
        this.cartRepository.removeCartItem(cartItem, this.cart.id).subscribe((cartItems: CartItem[]) => {
            this.cartItems = cartItems;
            this.calculatePrice();
        });
    }

    order() {
        const products: Product[] = [];
        this.cartItems.forEach((cartItem: CartItem) => products.push(cartItem.product as Product));
        console.log(products);
        const newOrder: Order = {id: 0, customer: 1, totalOrderValue: this.totalProductPrice, coupon: 1, cartId: this.cart.id, products};
        this.orderRepository.addOrder(newOrder).subscribe(result => {
            swal(result);
            ShopStorage.removeShopItem();
            this.router.navigate(['/orders']);
        });
    }
}
