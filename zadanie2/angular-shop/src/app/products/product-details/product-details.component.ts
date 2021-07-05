import {Component, OnInit} from '@angular/core';
import {ReviewRepositoryService} from '../../reviews/service/review-repository.service';
import {Product} from '../../_models/Product';
import {ActivatedRoute, Router} from '@angular/router';
import {Review} from '../../_models/Review';
import {ShopStorage} from '../../_helpers/ShopStorage';
import {CartRepositoryService} from '../../cart/service/cart-repository.service';
import {Cart, NewCartItem} from '../../_models/Cart';
import {switchMap} from 'rxjs/operators';
import swal from 'sweetalert2';
import {CustomerStorage} from '../../_helpers/CustomerStorage';

@Component({
    selector: 'app-product-details',
    templateUrl: './product-details.component.html',
    styleUrls: ['./product-details.component.scss']
})
export class ProductDetailsComponent implements OnInit {

    product: Product;
    reviews: Review[];
    constructor(protected route: ActivatedRoute,
                protected reviewRepository: ReviewRepositoryService,
                protected cartRepository: CartRepositoryService,
                protected router: Router) { }

    ngOnInit(): void {
        this.loadProduct();
    }

    loadProduct() {
        this.product = this.route.snapshot.data.product;
        if (this.product) {
            this.loadProductReview(this.product.id);
        }
    }

    loadProductReview(productId: number) {
        this.reviewRepository.getProductReviews(productId).subscribe((reviews: Review[]) => {
            this.reviews = reviews;
        });
    }

    addItemToCart() {
        const cartId = ShopStorage.getCart();
        console.log(cartId);
        if (!cartId) {
            const user = CustomerStorage.getUser();
            if (user) {
                const newCart: Cart = {id: 0, customer: 1, coupon: 1};
                this.cartRepository.addCart(newCart)
                    .pipe(switchMap((cart: Cart) => {
                        console.log(cart);
                        ShopStorage.setCart(cart.id);
                        return this.addItemCart(cart.id);
                    })).subscribe(result => this.productAddedToCart(result));
            } else {
                swal('Aby dodać produkt do koszyka muszisz się zalogować!');
                this.router.navigate(['/login']);
            }
        } else {
            this.addItemCart(cartId).subscribe(result => this.productAddedToCart(result));
        }
    }

    addItemCart(cartId: number) {
        console.log('dodaj produkt do koszyka o id: ', cartId);
        const cartItem: NewCartItem = {id: 0, cart: cartId, product: this.product.id, productQuantity: 1};
        return this.cartRepository.addCartItem(cartItem);
    }

    productAddedToCart(message) {
        swal(message);
        this.router.navigate(['/cart']);
    }
}
