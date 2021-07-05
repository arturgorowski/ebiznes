import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {LoginData} from '../_models/Auth';

@Injectable({
    providedIn: 'root'
})
export class AuthRepositoryService {
    constructor(private http: HttpClient) { }

    signUp(registerData) {
        const queryUrl = environment.apiHost + '/signUp';
        return this.http.post(queryUrl, registerData);
    }

    signIn(loginData: LoginData) {
        const queryUrl = environment.apiHost + '/signIn';
        return this.http.post(queryUrl, {
            email: loginData.login,
            password: loginData.loginPassword
        });
    }

    signOut() {
        const queryUrl = environment.apiHost + '/signOut';
        return this.http.post(queryUrl, {});
    }
}
