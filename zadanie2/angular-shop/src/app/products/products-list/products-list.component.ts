import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Product} from '../../_models/Product';
import {Categories} from '../../_models/Categories';
import {ProductRepositoryService} from '../service/product-repository.service';

@Component({
    selector: 'app-products-list',
    templateUrl: './products-list.component.html',
    styleUrls: ['./products-list.component.scss']
})
export class ProductsListComponent implements OnInit, OnDestroy {

    products: Product[];
    filtered: Product[];
    category: Categories;
    searchText = '';
    constructor(protected route: ActivatedRoute,
                protected productRepository: ProductRepositoryService) {
    }

    ngOnInit(): void {
        this.loadCategory();
        this.loadProduct();
    }

    ngOnDestroy() {
        this.products = [];
        this.filtered = [];
        this.category = null;
    }

    loadProduct() {
        if (this.category) {
            this.getProductByCategory(this.category.id);
        } else {
            this.products = this.route.snapshot.data.products;
            this.filtered = this.route.snapshot.data.products;
        }
    }

    loadCategory() {
        this.category = this.route.snapshot.data.category;
    }

    getProductByCategory(categoryId) {
        this.productRepository.getProductsByCategory(categoryId).subscribe((products: Product[]) => {
            this.products = products;
            this.filtered = products;
        });
    }

    searchProduct() {
        this.filtered = this.products.filter((product: Product) =>
            product.name.toUpperCase().includes(this.searchText.toUpperCase())
        );
    }

    selectedCategory(event) {
        this.category = event;
        this.getProductByCategory(this.category.id);
    }
}
