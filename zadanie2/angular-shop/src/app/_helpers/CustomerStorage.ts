import {Customer} from '../_models/Customer';

const KEY_USER = 'user';
const KEY_CUSTOMER = 'customer_ebiznes';

interface User {
    user: Customer;
}

const EMPTY_CUSTOMER_OBJECT: User = {
    user: null
};

export class CustomerStorage {

    private static getItem(key) {
        return JSON.parse(localStorage.getItem(key));
    }

    private static setItem(key, value) {
        localStorage.setItem(key, JSON.stringify(value));
    }

    static getCustomerItem(): User {
        if (!this.getItem(KEY_CUSTOMER)) {
            this.setItem(KEY_CUSTOMER, EMPTY_CUSTOMER_OBJECT);
        }

        return this.getItem(KEY_CUSTOMER);
    }

    static removeCustomerItem(): void {
        localStorage.removeItem(KEY_CUSTOMER);
        this.setItem(KEY_CUSTOMER, EMPTY_CUSTOMER_OBJECT);
    }

    static addItemToCustomer(objectKey, value) {
        const customer = this.getCustomerItem();
        Object.keys(customer).forEach(key => {
            if (key === objectKey) {
                customer[key] = value;
                this.setItem(KEY_CUSTOMER, customer);
            }
        });
    }

    static setUser(user: Customer) {
        this.addItemToCustomer(KEY_USER, user);
    }

    static getUser() {
        return this.getCustomerItem().user;
    }

    static isAuthenticated() {
        return !!this.getUser();
    }

    static clearData(): void {
        localStorage.clear();
    }
}
