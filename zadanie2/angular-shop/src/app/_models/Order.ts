export interface Order {
    id: number;
    createdAt: string;
    customer: number;
    isPaid: boolean;
    paidAt: string;
    totalOrderValue: number;
    coupon: number;
}


export interface OrderItem {
    id: number;
    order: number;
    product: number;
}
