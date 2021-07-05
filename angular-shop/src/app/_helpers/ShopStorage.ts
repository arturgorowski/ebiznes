
const KEY_SHOP = 'shop_ebiznes';
const KEY_CART = 'cart';

interface Shop {
    cart: number;
}

const EMPTY_SHOP_OBJECT: Shop = {
    cart: null
};

export class ShopStorage {

    private static getItem(key) {
        return JSON.parse(localStorage.getItem(key));
    }

    private static setItem(key, value) {
        localStorage.setItem(key, JSON.stringify(value));
    }

    static getShopItem(): Shop {
        if (!this.getItem(KEY_SHOP)) {
            this.setItem(KEY_SHOP, EMPTY_SHOP_OBJECT);
        }

        return this.getItem(KEY_SHOP);
    }

    static removeShopItem(): void {
        localStorage.removeItem(KEY_SHOP);
        this.setItem(KEY_SHOP, EMPTY_SHOP_OBJECT);
    }

    static addItemToShop(objectKey, value) {
        const shop = this.getShopItem();

        Object.keys(shop).forEach(key => {
            if (key === objectKey) {
                shop[key] = value;
                this.setItem(KEY_SHOP, shop);
            }
        });
    }

    static setCart(cart: number) {
        console.log('dodawanie id do storage: ', cart);
        this.addItemToShop(KEY_CART, cart);
    }

    static getCart() {
        return this.getShopItem().cart;
    }

    static clearData(): void {
        localStorage.clear();
    }
}
