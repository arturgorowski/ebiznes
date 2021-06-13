import {Product} from './Product';

export interface Cart {
    id: number;
    customer: number;
    coupon: number;
    cartItems?: CartItem[];
}

export interface CartItem {
    id: number;
    cart: number;
    product: Product|number;
    productQuantity: number;
}
