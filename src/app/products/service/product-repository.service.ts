import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable()
export class ProductRepositoryService {

    private productUrl = environment.apiHost + '/products';

    constructor(private http: HttpClient) { }

    getProducts() {
        return this.http.get(this.productUrl);
    }

    getProductDetails(productId: number) {
        const queryUrl = environment.apiHost + '/product/' + productId;
        return this.http.get(queryUrl);
    }

    getProductsByCategory(id: number) {
        const queryUrl = this.productUrl + '/category/' + id;
        return this.http.get(queryUrl);
    }
}
