package dojo.supermarket.model.billing;

import dojo.supermarket.model.stock.Product;

public interface PriceFor {

    double getUnitPrice(Product product);
}
