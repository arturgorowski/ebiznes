import {NgModule} from '@angular/core';
import {Routes, RouterModule, NoPreloading} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {ProductsListComponent} from './products/products-list/products-list.component';
import {ProductResolveService} from './products/service/product-resolve.service';
import {CategoryDetailsResolveService} from './category/service/category-details-resolve.service';
import {ProductDetailsResolveService} from './products/service/product-details-resolve.service';
import {ProductDetailsComponent} from './products/product-details/product-details.component';
import {CartComponent} from './cart/cart/cart.component';
import {OrdersListComponent} from './orders/orders-list/orders-list.component';
import {ReviewsListComponent} from './reviews/reviews-list/reviews-list.component';
import {ReviewsResolveService} from './reviews/service/reviews-resolve.service';
import {OrdersResolveService} from './orders/service/orders-resolve.service';
import {CartResolveService} from './cart/service/cart-resolve.service';
import {LoginComponent} from './login/login.component';
import {RegistrationComponent} from './registration/registration.component';
import {CustomerComponent} from './customer/customer.component';


const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
    },
    {
        path: 'products',
        children: [
            {
                path: '',
                component: ProductsListComponent,
                resolve: {products: ProductResolveService}
            },
            {
                path: ':product_id',
                component: ProductDetailsComponent,
                resolve: {product: ProductDetailsResolveService}
            }
        ]
    },
    {
        path: 'category',
        children: [
            {
                path: ':category_id',
                component: ProductsListComponent,
                resolve: {
                    category: CategoryDetailsResolveService
                }
            }
        ]
    },
    {
        path: 'orders',
        children: [
            {
                path: '',
                component: OrdersListComponent,
                resolve: {orders: OrdersResolveService}
            }
        ]
    },
    {
        path: 'cart',
        children: [
            {
                path: '',
                component: CartComponent,
                resolve: {cart: CartResolveService}
            }
        ]
    },
    {
        path: 'reviews',
        children: [
            {
                path: '',
                component: ReviewsListComponent,
                resolve: {reviews: ReviewsResolveService}
            }
        ]
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'register',
        component: RegistrationComponent
    },
    {
        path: 'profile',
        component: CustomerComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {preloadingStrategy: NoPreloading})],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
