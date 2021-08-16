import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import swal from 'sweetalert2';
import {AuthenticationToken, RegisterData} from '../_models/Auth';
import {AuthRepositoryService} from '../_services/auth-repository.service';
import {switchMap} from 'rxjs/operators';
import {CustomerRepositoryService} from '../customer/service/customer-repository.service';
import {Customer} from '../_models/Customer';
import {Router} from '@angular/router';
import {AuthenticateBase} from '../_helpers/AuthenticateBase';
import {environment} from "../../environments/environment";

const REGISTER_DATA = {
    email: '',
    password: ''
};

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent extends AuthenticateBase implements OnInit {

    registerData: RegisterData = {...REGISTER_DATA};
    registerForm: FormGroup;
    disableRegisterButton = false;
    constructor(protected authRepository: AuthRepositoryService,
                protected customerRepository: CustomerRepositoryService,
                protected router: Router) {
        super(router);
    }

    ngOnInit(): void {
        this.initRegisterForm();
    }

    onRegisterSubmit() {
        this.disableRegisterButton = true;
        this.authRepository.signUp(this.registerForm.value)
            .pipe(switchMap((data: AuthenticationToken) => {
                const customer: Customer = {
                    id: 1,
                    username: data.email,
                    firstName: 'Imie',
                    lastName: 'Nazwisko',
                    userId: data.id,
                    address: 1
                };
                return this.customerRepository.addCustomer(customer);
            })).subscribe(
            (data: Customer) => this.onSubmitSuccess(data),
            () => {
                swal('Konto o podanych danych już istnieje. Spróbuj ponownie.');
                this.disableRegisterButton = false;
            });
    }

    onGoogleRegisterSubmit() {
        // this.authRepository.signInWithGoogle().subscribe();
        window.location.href = environment.apiHost + '/authenticate/google';
    }

    initRegisterForm() {
        this.registerForm = new FormGroup({
            email: new FormControl(this.registerData.email, [
                Validators.required,
                Validators.email
            ]),

            password: new FormControl(this.registerData.password, [
                Validators.required,
            ])
        });
    }

    get email() {
        return this.registerForm.get('email');
    }

    get password() {
        return this.registerForm.get('password');
    }
}
