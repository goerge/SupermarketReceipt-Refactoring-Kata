package supermarket.model

import java.util.HashMap


class Teller(private val catalog: SupermarketCatalog) {
    private val offers = HashMap<Product, Offer>()

    fun addSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        this.offers[product] = Offer(offerType, product, argument)
    }

    fun checksOutArticlesFrom(cart: ShoppingCart): Receipt {
        val receipt = Receipt()
        for (item in cart.getItems()) {
            val unitPrice = getUnitPriceFor(item)
            val item = ReceiptItem(item, unitPrice)
            receipt.addLineItem(item)
        }
        cart.handleOffers(receipt, this.offers, this.catalog)

        return receipt
    }

    private fun getUnitPriceFor(item: CartItem) = this.catalog.getUnitPrice(item.product)

}
