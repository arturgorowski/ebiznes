import {Component, Input} from '@angular/core';
import {Review} from '../../_models/Review';

@Component({
    selector: 'app-review-details',
    templateUrl: './review-details.component.html',
    styleUrls: ['./review-details.component.scss']
})
export class ReviewDetailsComponent {
    @Input() review: Review;
}
