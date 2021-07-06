import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Review} from '../../_models/Review';

@Component({
    selector: 'app-reviews-list',
    templateUrl: './reviews-list.component.html',
    styleUrls: ['./reviews-list.component.scss']
})
export class ReviewsListComponent implements OnInit {

    reviews: Review[] = [];
    constructor(protected route: ActivatedRoute) { }

    ngOnInit(): void {
        this.loadReviews();
    }

    loadReviews() {
        this.reviews = this.route.snapshot.data.reviews;
    }

}
