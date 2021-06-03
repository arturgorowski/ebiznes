import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable()
export class CategoryRepositoryService {

    private categoryUrl = environment.apiHost + '/categories';

    constructor(private http: HttpClient) { }

    getCategories() {
        return this.http.get(this.categoryUrl);
    }

    getCategoriesById(categoryId: number) {
        const queryUrl = environment.apiHost + '/category/' + categoryId;
        return this.http.get(queryUrl);
    }
}
