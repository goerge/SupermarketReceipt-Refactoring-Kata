package dojo.supermarket.model.shopping;

import dojo.supermarket.model.stock.Product;

import java.util.*;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    private final Map<Product, Double> productQuantities = new HashMap<>();

    public List<ProductQuantity> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(Product product) {
        addItemQuantity(product, 1.0);
    }

    public Map<Product, Double> productQuantities() {
        return Collections.unmodifiableMap(productQuantities);
    }

    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }
}
