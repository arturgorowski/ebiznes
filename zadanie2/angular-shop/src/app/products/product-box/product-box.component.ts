import {Component, Input} from '@angular/core';
import {Product} from '../../_models/Product';

@Component({
    selector: 'app-product-box',
    templateUrl: './product-box.component.html',
    styleUrls: ['./product-box.component.scss']
})
export class ProductBoxComponent {
    @Input() product: Product;
}
