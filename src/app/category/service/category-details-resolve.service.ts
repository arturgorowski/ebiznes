import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {CategoryRepositoryService} from './category-repository.service';


@Injectable()
export class CategoryDetailsResolveService implements Resolve<any> {
    constructor(private categoryRepository: CategoryRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        const category_id = route.params.category_id;
        if (category_id) {
            return this.categoryRepository.getCategoriesById(category_id);
        }
    }

}
