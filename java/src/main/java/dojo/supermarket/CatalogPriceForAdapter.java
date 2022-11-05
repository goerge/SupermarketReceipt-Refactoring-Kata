package dojo.supermarket;

import dojo.supermarket.model.billing.PriceFor;
import dojo.supermarket.model.stock.Product;
import dojo.supermarket.model.stock.SupermarketCatalog;

public class CatalogPriceForAdapter implements PriceFor {

    private SupermarketCatalog catalog;

    public CatalogPriceForAdapter(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public double getUnitPrice(Product product) {
        return catalog.getUnitPrice(product);
    }
}
