package dojo.supermarket.model.shopping;

import dojo.supermarket.model.stock.Product;

public class ProductQuantity {

    private final Product product;
    private final double quantity;

    public ProductQuantity(Product product, double weight) {
        this.product = product;
        this.quantity = weight;
    }

    public Product getProduct() {
        return product;
    }

    public double getQuantity() {
        return quantity;
    }
}
