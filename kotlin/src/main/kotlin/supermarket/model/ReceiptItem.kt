package supermarket.model

data class ReceiptItem(val item: CartItem, val price: Double, val totalPrice: Double) {

	val product
		get() = item.product

	val quantity
		get() = item.quantity
}
