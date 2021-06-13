import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {NavHeaderComponent} from './nav-header/nav-header.component';
import {ProductsListComponent} from './products/products-list/products-list.component';
import {ProductResolveService} from './products/service/product-resolve.service';
import {ProductRepositoryService} from './products/service/product-repository.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {Interceptor} from './_services/interceptor.service';
import { ProductBoxComponent } from './products/product-box/product-box.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from '../material/material.module';
import {FormsModule} from '@angular/forms';
import { CategoryBarComponent } from './category/category-bar/category-bar.component';
import {CategoryResolveService} from './category/service/category-resolve.service';
import {CategoryRepositoryService} from './category/service/category-repository.service';
import {CategoryDetailsResolveService} from './category/service/category-details-resolve.service';
import { ProductDetailsComponent } from './products/product-details/product-details.component';
import {ProductDetailsResolveService} from './products/service/product-details-resolve.service';
import {ReviewRepositoryService} from './reviews/service/review-repository.service';
import { CartComponent } from './cart/cart/cart.component';
import { OrdersListComponent } from './orders/orders-list/orders-list.component';
import { ReviewsListComponent } from './reviews/reviews-list/reviews-list.component';
import { ReviewDetailsComponent } from './reviews/review-details/review-details.component';
import {ReviewsResolveService} from './reviews/service/reviews-resolve.service';
import {OrderRepositoryService} from './orders/service/order-repository.service';
import {OrdersResolveService} from './orders/service/orders-resolve.service';
import {CartRepositoryService} from './cart/service/cart-repository.service';
import {CartResolveService} from './cart/service/cart-resolve.service';
import {MAT_SNACK_BAR_DEFAULT_OPTIONS} from '@angular/material/snack-bar';
import { CartItemComponent } from './cart/cart-item/cart-item.component';
import { OrderDetailsComponent } from './orders/order-details/order-details.component';

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        NavHeaderComponent,
        ProductsListComponent,
        ProductBoxComponent,
        CategoryBarComponent,
        ProductDetailsComponent,
        CartComponent,
        OrdersListComponent,
        ReviewsListComponent,
        ReviewDetailsComponent,
        CartItemComponent,
        OrderDetailsComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MaterialModule,
        FormsModule
    ],
    providers: [
        {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {panelClass: 'custom-snackbar', verticalPosition: 'top', duration: 5000}},
        // {provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true},
        ProductRepositoryService,
        ProductResolveService,
        CategoryRepositoryService,
        CategoryResolveService,
        CategoryDetailsResolveService,
        ProductDetailsResolveService,
        ReviewRepositoryService,
        ReviewsResolveService,
        OrderRepositoryService,
        OrdersResolveService,
        CartRepositoryService,
        CartResolveService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
