package supermarket.model

import java.util.*

class Receipt {
    private val items = ArrayList<ReceiptItem>()
    private val discounts = ArrayList<Discount>()

    val totalPrice: Double?
        get() {
            var total = 0.0
            for (item in this.items) {
                total += item.totalPrice
            }
            for (discount in this.discounts) {
                total -= discount.discountAmount
            }
            return total
        }

    fun addLineItem(item: ReceiptItem) {
        this.items.add(item)
    }

    fun getItems(): List<ReceiptItem> {
        return ArrayList(this.items)
    }

    fun addDiscount(discount: Discount) {
        this.discounts.add(discount)
    }

    fun getDiscounts(): List<Discount> {
        return discounts
    }
}
