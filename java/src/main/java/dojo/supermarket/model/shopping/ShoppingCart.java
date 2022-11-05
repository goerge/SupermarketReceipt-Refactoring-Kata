package dojo.supermarket.model.shopping;

import dojo.supermarket.model.billing.Offer;
import dojo.supermarket.model.stock.Product;
import dojo.supermarket.model.billing.SpecialOfferType;
import dojo.supermarket.model.billing.Discount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
