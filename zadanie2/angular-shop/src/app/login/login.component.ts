import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import swal from 'sweetalert2';
import {AuthenticationToken, LoginData} from '../_models/Auth';
import {AuthRepositoryService} from '../_services/auth-repository.service';
import {switchMap} from 'rxjs/operators';
import {CustomerRepositoryService} from '../customer/service/customer-repository.service';
import {Customer} from '../_models/Customer';
import {Router} from '@angular/router';
import {AuthenticateBase} from '../_helpers/AuthenticateBase';

const LOGIN_DATA = {
    login: '',
    loginPassword: ''
};

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent extends AuthenticateBase implements OnInit {

    loginData: LoginData = {...LOGIN_DATA};
    loginForm: FormGroup;
    disableLoginButton = false;
    constructor(protected authRepository: AuthRepositoryService,
                protected customerRepository: CustomerRepositoryService,
                protected router: Router) {
        super(router);
    }

    ngOnInit(): void {
        this.initLoginForm();
    }

    onLoginSubmit() {
        this.disableLoginButton = true;
        this.authRepository.signIn(this.loginForm.value)
            .pipe(switchMap((data: AuthenticationToken) => {
                return this.customerRepository.getCustomerByUserId(data.id);
            })).subscribe(
                (data: Customer) => this.onSubmitSuccess(data),
                () => {
                    swal('Konto o podanych danych nie istnieje. Spróbuj ponownie.');
                    this.disableLoginButton = false;
                });
    }

    initLoginForm() {
        this.loginForm = new FormGroup({
            login: new FormControl(this.loginData.login, [
                Validators.required,
                Validators.email
            ]),

            loginPassword: new FormControl(this.loginData.loginPassword, [
                Validators.required,
            ])
        });
    }

    get login() {
        return this.loginForm.get('login');
    }

    get loginPassword() {
        return this.loginForm.get('loginPassword');
    }

}
