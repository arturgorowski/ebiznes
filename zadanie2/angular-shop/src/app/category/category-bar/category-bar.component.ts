import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CategoryRepositoryService} from '../service/category-repository.service';
import {Categories} from '../../_models/Categories';

@Component({
    selector: 'app-category-bar',
    templateUrl: './category-bar.component.html',
    styleUrls: ['./category-bar.component.scss']
})
export class CategoryBarComponent implements OnInit {

    categories: Categories[] = [];
    activeCategory = null;
    @Output() selectedCategory = new EventEmitter<Categories>();
    constructor(private categoryRepository: CategoryRepositoryService) { }

    ngOnInit(): void {
        this.loadCategories();
    }

    loadCategories() {
        this.categoryRepository.getCategories().subscribe((categories: Categories[]) => {
            this.categories = categories;
        });
    }

    categorySelected(index: number) {
        this.activeCategory = index;
        this.selectedCategory.emit(this.categories[index]);
    }

}
