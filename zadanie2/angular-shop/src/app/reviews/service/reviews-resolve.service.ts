import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {ReviewRepositoryService} from './review-repository.service';

@Injectable()
export class ReviewsResolveService implements Resolve<any> {
    constructor(private reviewRepository: ReviewRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        return this.reviewRepository.getReviews();
    }

}
