import {Product} from './Product';

export interface Order {
    id: number;
    customer: number;
    totalOrderValue: number;
    coupon: number;
    products?: Product[];
    orderItems?: OrderItem[];
    cartId?: number;
}


export interface OrderItem {
    id: number;
    order: number;
    product: number;
}
