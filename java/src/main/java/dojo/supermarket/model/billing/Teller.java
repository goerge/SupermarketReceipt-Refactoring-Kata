package dojo.supermarket.model.billing;

import dojo.supermarket.model.stock.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Teller {

    private final PriceFor priceFor;
    private final Map<Product, Offer> offers = new HashMap<>();

    public Teller(PriceFor priceFor) {
        this.priceFor = priceFor;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        offers.put(product, new Offer(offerType, product, argument));
    }

    public Receipt checksOutArticlesFrom(ItemList itemList) {
        Receipt receipt = new Receipt();
        itemList.getItems()
            .forEach(item ->
                receipt.addProduct(item.getProduct(), item.getQuantity(), priceFor.getUnitPrice(item.getProduct()))
            );

        itemList.getItems().stream()
            .map(ItemList.Item::getProduct)
            .collect(Collectors.toSet()).stream()
            .map(product -> {
                Offer offer = offers.getOrDefault(product, null);
                if(offer != null) {
                    return calculateDiscount(product, itemList.quantityOf(product), offer);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .forEach(receipt::addDiscount);

        return receipt;
    }

    private Discount calculateDiscount(Product product, double quantity, Offer offer) {
        double unitPrice = priceFor.getUnitPrice(product);
        return switch (offer.offerType) {
            case TEN_PERCENT_DISCOUNT -> calculateDiscountTenPercent(product, quantity, offer, unitPrice);
            case TWO_FOR_AMOUNT -> calculateTwoForAmountDiscount(product, quantity, offer, unitPrice);
            case THREE_FOR_TWO -> calculateThreeForTwoDiscount(product, quantity, offer, unitPrice);
            case FIVE_FOR_AMOUNT -> calculateFiveForAmountDiscount(product, quantity, offer, unitPrice);
        };
    }

    private static Discount calculateDiscountTenPercent(Product product, double quantity, Offer offer, double unitPrice) {
        return new Discount(product, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
    }

    private static Discount calculateFiveForAmountDiscount(Product product, double quantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) quantity;
        if(quantityAsInt < 5) {
            return null;
        }
        int numberOfBulks = quantityAsInt / 5;
        double discountTotal = unitPrice * quantity - (offer.argument * numberOfBulks + quantityAsInt % 5 * unitPrice);
        return new Discount(product, 5 + " for " + offer.argument, -discountTotal);
    }

    private static Discount calculateThreeForTwoDiscount(Product product, double quantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) quantity;
        if(quantityAsInt <= 2) {
            return null;
        }
        int numberOfBulks = quantityAsInt / 3;
        double discountAmount = quantity * unitPrice - ((numberOfBulks * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
        return new Discount(product, "3 for 2", -discountAmount);
    }

    private static Discount calculateTwoForAmountDiscount(Product product, double quantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) quantity;
        if(quantityAsInt < 2) {
            return null;
        }
        int intDivision = quantityAsInt / 2;
        double pricePerUnit = offer.argument * intDivision;
        double theTotal = (quantityAsInt % 2) * unitPrice;
        double total = pricePerUnit + theTotal;
        double discountN = unitPrice * quantity - total;
        return new Discount(product, "2 for " + offer.argument, -discountN);
    }
}
