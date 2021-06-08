import {Component, Input, OnInit} from '@angular/core';
import {Product} from '../../_models/Product';

@Component({
    selector: 'app-product-box',
    templateUrl: './product-box.component.html',
    styleUrls: ['./product-box.component.scss']
})
export class ProductBoxComponent implements OnInit {

    @Input() product: Product;
    constructor() {
    }

    ngOnInit(): void {
    }

}
