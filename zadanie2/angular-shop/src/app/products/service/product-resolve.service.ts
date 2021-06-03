import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {ProductRepositoryService} from './product-repository.service';
import {Observable} from 'rxjs';


@Injectable()
export class ProductResolveService implements Resolve<any> {
    constructor(private productRepository: ProductRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        const categoryId = route.params.category_id;
        if (categoryId) {
            console.log('produkty z kategorii', categoryId);
            return this.productRepository.getProductsByCategory(categoryId);
        } else {
            console.log('wszystkie produkty');
            return this.productRepository.getProducts();
        }
    }

}
