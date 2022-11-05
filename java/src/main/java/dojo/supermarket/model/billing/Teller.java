package dojo.supermarket.model.billing;

import dojo.supermarket.model.shopping.ProductQuantity;
import dojo.supermarket.model.shopping.ShoppingCart;
import dojo.supermarket.model.stock.Product;
import dojo.supermarket.model.stock.SupermarketCatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Teller {

    private final SupermarketCatalog catalog;
    private final Map<Product, Offer> offers = new HashMap<>();

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        offers.put(product, new Offer(offerType, product, argument));
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        Receipt receipt = new Receipt();
        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq: productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            receipt.addProduct(p, quantity, unitPrice, price);
        }
        handleOffers(offers, catalog::getUnitPrice, theCart.productQuantities()).forEach(receipt::addDiscount);

        return receipt;
    }

    public List<Discount> handleOffers(Map<Product, Offer> offers, Function<Product, Double> getUnitPrice, Map<Product, Double> productQuantities) {

        List<Discount> discounts = new ArrayList<>();
        for (Map.Entry<Product, Double> entry: productQuantities.entrySet()) {
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
        if (offer.offerType == SpecialOfferType.TEN_PERCENT_DISCOUNT) {
            return new Discount(product, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
        }

        int quantityAsInt = (int) quantity;
        if (offer.offerType == SpecialOfferType.TWO_FOR_AMOUNT && quantityAsInt >= 2) {
            return calculateTwoForAmmountDiscount(product, quantity, offer, unitPrice, quantityAsInt);
        }
        if (offer.offerType == SpecialOfferType.THREE_FOR_TWO && quantityAsInt > 2) {
            return calculateThreeForTwoDiscount(product, quantity, offer, unitPrice, quantityAsInt);
        }
        if (offer.offerType == SpecialOfferType.FIVE_FOR_AMOUNT && quantityAsInt >= 5) {
            return calculateFiveForAmountDiscount(product, quantity, offer, unitPrice, quantityAsInt);
        }
        return null;
    }

    private static Discount calculateFiveForAmountDiscount(Product product, double quantity, Offer offer, double unitPrice, int quantityAsInt) {
        int numberOfBulks = quantityAsInt / getBulkSize(offer);
        double discountTotal = unitPrice * quantity - (offer.argument * numberOfBulks + quantityAsInt % 5 * unitPrice);
        return new Discount(product, 5 + " for " + offer.argument, -discountTotal);
    }

    private static Discount calculateThreeForTwoDiscount(Product product, double quantity, Offer offer, double unitPrice, int quantityAsInt) {
        int numberOfBulks = quantityAsInt / getBulkSize(offer);
        double discountAmount = quantity * unitPrice - ((numberOfBulks * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
        return new Discount(product, "3 for 2", -discountAmount);
    }

    private static Discount calculateTwoForAmmountDiscount(Product product, double quantity, Offer offer, double unitPrice, int quantityAsInt) {
        int intDivision = quantityAsInt / 2;
        double pricePerUnit = offer.argument * intDivision;
        double theTotal = (quantityAsInt % 2) * unitPrice;
        double total = pricePerUnit + theTotal;
        double discountN = unitPrice * quantity - total;
        return new Discount(product, "2 for " + offer.argument, -discountN);
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
