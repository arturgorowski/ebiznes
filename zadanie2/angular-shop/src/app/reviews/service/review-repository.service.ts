import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable()
export class ReviewRepositoryService {

    constructor(private http: HttpClient) { }

    getReviews() {
        const queryUrl = environment.apiHost + '/reviews';
        return this.http.get(queryUrl);
    }

    getProductReviews(productId: number) {
        const queryUrl = environment.apiHost + '/productreviews/' + productId;
        return this.http.get(queryUrl);
    }

    getCustomerReviews(customerId: number) {
        const queryUrl = environment.apiHost + '/customerreviews/' + customerId;
        return this.http.get(queryUrl);
    }
}
