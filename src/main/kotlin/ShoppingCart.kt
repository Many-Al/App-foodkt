

class ShoppingCart {
    private val items: MutableList<ProductItem> = mutableListOf()

    fun addToCart(product: ProductItem, customizations: List<String> = emptyList()) {
        val itemWithCustomizations = if (customizations.isNotEmpty()) {
            "${product.name} (${customizations.joinToString(", ")})"
        } else {
            product.name
        }
        val newItem = ProductItem(itemWithCustomizations, product.price, product.category)
        items.add(newItem)
    }

    fun removeFromCart(product: ProductItem) {
        items.remove(product)
    }

    fun viewCart() {
        println("Your Cart:")
        for ((index, item) in items.withIndex()) {
            println("${index + 1}. ${item.name} - $${item.price}")
        }
    }

    fun calculateTotalAmount(): Double {
        return items.sumOf { it.price }
    }

    fun getProductAtIndex(index: Int): ProductItem {
        return items[index]
    }

    fun clearCart() {
        items.clear()
    }

    fun shoppingCartSize() : Int = items.size
}