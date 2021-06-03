import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {CategoryRepositoryService} from './category-repository.service';


@Injectable()
export class CategoryResolveService implements Resolve<any> {
    constructor(private categoryRepository: CategoryRepositoryService) { }

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        return this.categoryRepository.getCategories();
    }

}
