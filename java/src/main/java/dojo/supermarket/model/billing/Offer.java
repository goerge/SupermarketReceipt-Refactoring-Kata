package dojo.supermarket.model.billing;

import dojo.supermarket.model.stock.Product;

public class Offer {

    public SpecialOfferType offerType;
    private final Product product;
    public double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }

    Product getProduct() {
        return product;
    }
}
