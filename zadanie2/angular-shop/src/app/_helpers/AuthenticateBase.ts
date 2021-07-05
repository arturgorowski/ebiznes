import {Customer} from '../_models/Customer';
import {CustomerStorage} from './CustomerStorage';
import {Router} from '@angular/router';

export abstract class AuthenticateBase {

    protected constructor(protected router: Router) { }

    onSubmitSuccess(data: Customer) {
        const user: Customer = {
            id: data.id,
            firstName: data.firstName,
            lastName: data.lastName,
            username: data.username,
            userId: data.userId,
            isAuthenticated: true
        };
        CustomerStorage.setUser(user);
        this.router.navigate(['/products']);
    }
}
