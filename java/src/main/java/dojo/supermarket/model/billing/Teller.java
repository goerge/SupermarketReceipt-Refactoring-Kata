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
                    return calculateDiscount(product, itemList.quantityOf(product), offer, priceFor::getUnitPrice);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .forEach(receipt::addDiscount);

        return receipt;
    }

    private static Discount calculateDiscount(Product product, double quantity, Offer offer, Function<Product, Double> getUnitPrice) {
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
