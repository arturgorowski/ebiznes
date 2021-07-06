import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class Interceptor implements HttpInterceptor {

    constructor(protected router: Router) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (request.url.includes(environment.apiHost)) {
            request = request.clone({
                url: request.url,
                setHeaders: {
                    // 'Token': token,
                    // 'Access-Control-Allow-Origin': '*',
                    Accept: 'application/json',
                    'Content-Type': 'application/json'
                },
                withCredentials: true
            });
        }

        return next.handle(request).pipe(tap((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
                console.log('[EVENT]', event);
            }
        }, (error: any) => {
            if (error instanceof HttpErrorResponse && request.url.includes(environment.apiHost)) {
                this.handleErrors(error);
            }
        }));
    }

    handleErrors(error) {
        console.log('[ERROR]', error);
    }
}

// import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
// import { Injectable } from '@angular/core';
//
// import { Observable, throwError } from 'rxjs';
// import { catchError } from 'rxjs/operators';
//
// @Injectable()
// export class Interceptor implements HttpInterceptor {
//
//     constructor() {
//     }
//
//     public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//         return next
//             .handle(req)
//             .pipe(catchError(err => {
//                 if (err instanceof HttpErrorResponse) {
//                     this.onError(err);
//                 }
//                 return throwError(err);
//             }));
//     }
//
//     /**
//      * Handle http errors.
//      * @param response - ErrorResponse.
//      */
//     private onError(response: HttpErrorResponse): void {
//         const clientErrorMessage = this.handleClientSideError(response.status);
//         if (clientErrorMessage) {
//             // show client side error
//             return;
//         }
//
//         const serverErrorMessage = this.handleServerError(response.error);
//         if (serverErrorMessage) {
//             // show server error
//         }
//     }
//
//     private handleClientSideError(status: number): string | undefined {
//         switch (status) {
//             case 0:
//                 return 'NO INTERNET CONNECTION';
//             case 404:
//                 return 'REQUEST FAILURE';
//             default:
//                 return;
//         }
//     }
//
//     private handleServerError(errorResponse: any): string {
//         // handle server error
//         return '';
//     }
// }

