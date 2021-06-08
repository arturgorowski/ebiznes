export interface Cart {
    id: number;
    customer: number;
    totalProductsPrice: number;
    coupon: number;
}

export interface CartItem {
    id: number;
    cart: number;
    product: number;
    productQuantity: number;
}
