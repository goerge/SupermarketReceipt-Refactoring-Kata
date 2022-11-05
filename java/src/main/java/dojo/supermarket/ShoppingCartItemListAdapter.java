package dojo.supermarket;

import dojo.supermarket.model.billing.ItemList;
import dojo.supermarket.model.shopping.ShoppingCart;
import dojo.supermarket.model.stock.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartItemListAdapter implements ItemList {

    private final ShoppingCart cart;

    public ShoppingCartItemListAdapter(ShoppingCart cart) {
        this.cart = cart;
    }

    @Override
    public List<Item> getItems() {
        return cart.getItems().stream().map(pq -> new Item(pq.getProduct(), pq.getQuantity())).collect(Collectors.toList());
    }

    @Override
    public Double quantityOf(Product product) {
        return cart.productQuantities().getOrDefault(product, null);
    }
}
