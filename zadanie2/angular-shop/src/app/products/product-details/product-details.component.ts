import {Component, OnInit} from '@angular/core';
import {ReviewRepositoryService} from '../../reviews/service/review-repository.service';
import {Product} from '../../_models/Product';
import {ActivatedRoute, Router} from '@angular/router';
import {Review} from '../../_models/Review';
import {ShopStorage} from '../../_helpers/ShopStorage';
import {CartRepositoryService} from '../../cart/service/cart-repository.service';
import {CartItem} from '../../_models/Cart';

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
        if (cartId) {
            const cartItem: CartItem = {id: 0, cart: cartId, product: this.product.id, productQuantity: 1};
            this.cartRepository.addCartItem(cartItem).subscribe((cartItems: CartItem[]) => {
                this.router.navigate(['/cart']);
            });
        } else {

        }
        console.log('add item to cart');
    }
}
