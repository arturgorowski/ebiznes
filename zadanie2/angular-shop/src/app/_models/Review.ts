import {Product} from './Product';
import {Customer} from './Customer';

export interface Review {
    id: number;
    product: Product;
    customer: Customer;
    content: string;
    score: number;
}
