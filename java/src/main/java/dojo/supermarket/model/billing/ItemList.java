package dojo.supermarket.model.billing;

import dojo.supermarket.model.stock.Product;

import java.util.List;

public interface ItemList {

    List<Item> getItems();

    Double quantityOf(Product product);

    class Item {

        private final Product product;
        private final Double quantity;

        public Item(Product product, Double quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public Double getQuantity() {
            return quantity;
        }
    }
}
