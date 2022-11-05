package dojo.supermarket.model.stock;

public interface SupermarketCatalog {

    void addProduct(Product product, double price);

    double getUnitPrice(Product product);
}
