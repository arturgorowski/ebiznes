export interface Review {
    id: number;
    product: number;
    productName?: string;
    customer: number;
    customerName?: string;
    content: string;
    score: number;
}
