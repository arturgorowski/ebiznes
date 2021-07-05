import {Component, Input} from '@angular/core';
import {AuthRepositoryService} from '../_services/auth-repository.service';
import {CustomerStorage} from '../_helpers/CustomerStorage';
import {ShopStorage} from '../_helpers/ShopStorage';

@Component({
    selector: 'app-nav-header',
    templateUrl: './nav-header.component.html',
    styleUrls: ['./nav-header.component.scss']
})
export class NavHeaderComponent {

    @Input() isAuthenticated: boolean;

    constructor(protected authRepository: AuthRepositoryService) { }

    logout() {
        this.authRepository.signOut().subscribe(res => console.log(res));
        ShopStorage.clearData();
        CustomerStorage.clearData();
    }
}
