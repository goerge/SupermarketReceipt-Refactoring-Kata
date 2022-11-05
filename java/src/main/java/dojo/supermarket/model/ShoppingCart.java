package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    private final Map<Product, Double> productQuantities = new HashMap<>();

    List<ProductQuantity> getItems() {
        return Collections.unmodifiableList(items);
    }

    void addItem(Product product) {
        addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
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

    List<Discount> handleOffers(Map<Product, Offer> offers, Function<Product, Double> getUnitPrice) {

        List<Discount> discounts = new ArrayList<>();
        for (Map.Entry<Product, Double> entry: productQuantities().entrySet()) {
            Product product = entry.getKey();
            double quantity = entry.getValue();
            if (offers.containsKey(product)) {
                Offer offer = offers.get(product);
                Discount discount = calculateDiscount(getUnitPrice, product, quantity, offer);
                if (discount != null) {
                    discounts.add(discount);
                }
            }
        }
        return discounts;
    }

    private static Discount calculateDiscount(Function<Product, Double> getUnitPrice, Product product, double quantity, Offer offer) {
        double unitPrice = getUnitPrice.apply(product);
        int quantityAsInt = (int) quantity;
        if (offer.offerType == SpecialOfferType.TWO_FOR_AMOUNT && quantityAsInt >= 2) {
            int intDivision = quantityAsInt / 2;
            double pricePerUnit = offer.argument * intDivision;
            double theTotal = (quantityAsInt % 2) * unitPrice;
            double total = pricePerUnit + theTotal;
            double discountN = unitPrice * quantity - total;
            return new Discount(product, "2 for " + offer.argument, -discountN);
        }
        int numberOfBulks = quantityAsInt / getBulkSize(offer);
        if (offer.offerType == SpecialOfferType.THREE_FOR_TWO && quantityAsInt > 2) {
            double discountAmount = quantity * unitPrice - ((numberOfBulks * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
            return new Discount(product, "3 for 2", -discountAmount);
        }
        if (offer.offerType == SpecialOfferType.TEN_PERCENT_DISCOUNT) {
            return new Discount(product, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
        }
        if (offer.offerType == SpecialOfferType.FIVE_FOR_AMOUNT && quantityAsInt >= 5) {
            double discountTotal = unitPrice * quantity - (offer.argument * numberOfBulks + quantityAsInt % 5 * unitPrice);
            return new Discount(product, 5 + " for " + offer.argument, -discountTotal);
        }
        return null;
    }

    private static int getBulkSize(Offer offer) {
        int bulkSize = 1;
        if (offer.offerType == SpecialOfferType.THREE_FOR_TWO) {
            bulkSize = 3;
        } else if (offer.offerType == SpecialOfferType.TWO_FOR_AMOUNT) {
            bulkSize = 2;
        }
        if (offer.offerType == SpecialOfferType.FIVE_FOR_AMOUNT) {
            bulkSize = 5;
        }
        return bulkSize;
    }
}
