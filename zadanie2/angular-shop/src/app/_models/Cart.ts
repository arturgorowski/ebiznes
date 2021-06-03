export interface Cart {
    id: number;
    customer: number;
    productsQuantity: number;
    totalProductsPrice: number;
    coupon: number;
    createdAt: string;
}

export interface CartItem {
    id: number;
    cart: number;
    product: number;
    productQuantity: number;
}
