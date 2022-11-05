package supermarket.model

import java.util.HashMap


class Teller(private val catalog: SupermarketCatalog) {
    private val offers = HashMap<Product, Offer>()

    fun addSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        this.offers[product] = Offer(offerType, product, argument)
    }

    fun checksOutArticlesFrom(cart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val productQuantities = cart.getItems()
        for (pq in productQuantities) {
            val product = pq.product
            val quantity = pq.quantity
            val unitPrice = this.catalog.getUnitPrice(product)
            val price = quantity * unitPrice

            val item = ReceiptItem(product, quantity, unitPrice, price)
            receipt.addLineItem(item)
        }
        cart.handleOffers(receipt, this.offers, this.catalog)

        return receipt
    }

}
