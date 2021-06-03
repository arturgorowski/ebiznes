import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {ProductRepositoryService} from './product-repository.service';
import {Observable} from 'rxjs';


@Injectable()
export class ProductDetailsResolveService implements Resolve<any> {
    constructor(private productRepository: ProductRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        const productId = route.params.product_id;
        if (productId) {
            return this.productRepository.getProductDetails(productId);
        }
    }

}
