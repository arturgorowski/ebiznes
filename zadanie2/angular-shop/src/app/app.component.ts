import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, RouterEvent} from '@angular/router';
import {Subscription} from 'rxjs';
import swal from 'sweetalert2';
import {CustomerStorage} from './_helpers/CustomerStorage';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    title = 'angular-shop';
    subscription = new Subscription();
    isAuthenticated: boolean;
    isLoading: boolean;

    constructor(protected router: Router) { }

    ngOnInit() {
        this.subscription.add(this.router.events
            .subscribe((routerEvent: RouterEvent) => {
                this.onRouterEvent(routerEvent);
            }));
    }

    private onRouterEvent(routerEvent: RouterEvent) {
        if (routerEvent instanceof NavigationStart) {
            this.isLoading = true;
        } else if (routerEvent instanceof NavigationEnd
            || routerEvent instanceof NavigationCancel
            || routerEvent instanceof NavigationError) {
            this.isLoading = false;
            if (routerEvent instanceof NavigationError) {
                if (routerEvent.error.error.code === 400) {
                    swal(routerEvent.error.error.message).then(() => {
                        this.router.navigate(['']);
                    });
                }
            }
            window.scrollTo(0, 0);
            this.isAuthenticated = CustomerStorage.isAuthenticated();
        }
    }
}
