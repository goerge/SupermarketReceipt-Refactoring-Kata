package dojo.supermarket.model.stock;

import dojo.supermarket.model.stock.Product;

public interface SupermarketCatalog {

    void addProduct(Product product, double price);

    double getUnitPrice(Product product);
}
