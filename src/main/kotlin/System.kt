
import kotlin.system.exitProcess

typealias customizationReturn = Pair<Double, List<String>>

class System {

    fun start() {
        val products = listOf(
            ProductItem("Burger", 5.99, Category.SALT_FOOD),
            ProductItem("chicken", 7.99, Category.SALT_FOOD),
            ProductItem("cheese", 2.99, Category.SALT_FOOD),
            ProductItem("Pizza Salami", 20.99, Category.SALT_FOOD),
            ProductItem("Pizza Margherita", 25.99, Category.SALT_FOOD),
            ProductItem("Pizza Tuna", 30.99, Category.SALT_FOOD),
            ProductItem("Salad", 1.99, Category.SALT_FOOD),
            ProductItem("Cappuccino", 2.49, Category.COFFEE_AND_APPETIZERS),
            ProductItem("Latte", 5.49, Category.COFFEE_AND_APPETIZERS),
            ProductItem("Black Coffee", 1.49, Category.COFFEE_AND_APPETIZERS),
            ProductItem("Chocolate Croissant", 3.49, Category.COFFEE_AND_APPETIZERS),
            ProductItem("Cheese Croissant", 3.49, Category.COFFEE_AND_APPETIZERS),
            ProductItem("Butter Croissant", 3.49, Category.COFFEE_AND_APPETIZERS),
            ProductItem(
                "Egg",
                2.49,
                Category.COFFEE_AND_APPETIZERS
            ),
            ProductItem("Chocolate Cake", 30.99, Category.SWEET_FOOD),
            ProductItem("Cinnamon Cake", 30.99, Category.SWEET_FOOD),
            ProductItem("Fruit Cake", 30.99, Category.SWEET_FOOD),
        )
        val accountManager = AccountManager()
        var user: User? = null

        while (user == null) {
            print("Enter your email: ")
            val email = readlnOrNull()

            print("Enter your password: ")
            val password = readlnOrNull()

            val account = accountManager.login(email ?: "", password ?: "")
            if (account != null) {
                val shoppingCart = ShoppingCart()
                user = User(account, shoppingCart)
                println("Welcome, ${account.name}!")
                println("Please choose the location of the restaurant:")
                println("1. Cairo")
                println("2. Alex")
                println("3. Hurghada")
                readlnOrNull()
            } else {
                println("Invalid login credentials. Please try again.")
            }
        }

        val shoppingCart = user.shoppingCart

        while (true) {
            println("Choose an option:")
            println("1. Add a product to your cart")
            println("2. View your cart")
            println("3. Remove a product from your cart")
            println("4. Checkout")
            println("5. Logout and exit")

            when (readlnOrNull()?.toIntOrNull()) {
                1 -> {
                    println("Choose a category:")
                    println("1. Salt Food")
                    println("2. Coffee & Appetizers")
                    println("3. Sweet Food")
                    print("Enter the category number: ")

                    val selectedCategory: Category? = when (readlnOrNull()) {
                        "1" -> Category.SALT_FOOD
                        "2" -> Category.COFFEE_AND_APPETIZERS
                        "3" -> Category.SWEET_FOOD
                        else -> null
                    }

                    if (selectedCategory != null) {
                        println("Available products in ${selectedCategory.name}:")
                        val categoryProducts =
                            products.filter { it.category == selectedCategory }
                        for ((index, product) in categoryProducts.withIndex()) {
                            println("${index + 1}. ${product.name} - $${product.price}")
                        }

                        print("Enter the product number to add to your cart, or enter '0' to cancel: ")
                        val productNumber = readlnOrNull()?.toIntOrNull()

                        if (productNumber != null && productNumber >= 1 && productNumber <= categoryProducts.size) {
                            val selectedProduct = categoryProducts[productNumber - 1]
                            when (selectedProduct.name) {
                                "Salad" -> {
                                    val customization = askForSaladCustomizations()
                                    selectedProduct.price += customization.first
                                    shoppingCart.addToCart(selectedProduct, customization.second)
                                }

                                in listOf("Cappuccino", "Latte", "Black Coffee") -> {
                                    val customization = askForCoffeeCustomizations()
                                    selectedProduct.price += customization.first
                                    shoppingCart.addToCart(selectedProduct, customization.second)
                                }

                                "Egg" -> {
                                    val customization = askForEggCustomizations()
                                    selectedProduct.price += customization.first
                                    shoppingCart.addToCart(selectedProduct, customization.second)
                                }

                                else -> {
                                    shoppingCart.addToCart(selectedProduct)
                                }
                            }
                            println("Product added to your cart.")
                        } else {
                            println("Addition canceled.")
                        }
                    } else {
                        println("Invalid category choice.")
                    }
                }

                2 -> {
                    if (shoppingCart.shoppingCartSize() == 0) {
                        println("Your cart is empty!")
                        println()
                    } else {
                        shoppingCart.viewCart()
                        println()
                    }
                }

                3 -> {
                    if (shoppingCart.shoppingCartSize() == 0) {
                        println("Your cart in empty!")
                    } else {
                        shoppingCart.viewCart()
                        print("Enter the number of the product to remove, or enter '0' to cancel: ")
                        val productNumber = readlnOrNull()?.toIntOrNull()

                        if (productNumber != null && productNumber >= 1 && productNumber <= shoppingCart.shoppingCartSize()) {
                            val productToRemove = shoppingCart.getProductAtIndex(productNumber - 1)
                            shoppingCart.removeFromCart(productToRemove)
                            println("Product removed from your cart.")
                        } else {
                            println("Invalid product number or canceled.")
                        }
                    }
                }

                4 -> {
                    shoppingCart.viewCart()
                    val totalAmount = shoppingCart.calculateTotalAmount()

                    if (totalAmount == 0.0) {
                        println("You don't have any items to order yet.")
                    } else {
                        println("Total Amount to Pay: $${"%.2f".format(totalAmount)}")

                        println("Choose a payment method:")
                        println("1. PayPal")
                        println("2. Visa")
                        print("Enter the payment method number: ")

                        when (readlnOrNull()?.toIntOrNull()) {
                            1 -> {
                                val paypal = PayPalPayment()
                                val addition = askForAdditionalItemsCost()
                                paypal.processPayment(totalAmount + addition)
                                shoppingCart.clearCart()
                                println("Order confirmed. Thank you for shopping! the order will take 20 Min to be delivered")
                                while (true) {
                                    println("do you want to continue?")
                                    println("1. Yes")
                                    println("2. No")
                                    println("Enter your choice (1/2): ")
                                    when (readlnOrNull()?.toIntOrNull()) {
                                        1 -> {
                                            break
                                        }

                                        2 -> {
                                            println("Goodbye!")
                                            exitProcess(0)
                                        }

                                        else -> {
                                            println("Invalid choice. please enter a 1 or 2 only!")
                                        }
                                    }
                                }
                            }

                            2 -> {
                                val visa = VisaPayment()
                                val addition = askForAdditionalItemsCost()
                                visa.processPayment(totalAmount + addition)
                                shoppingCart.clearCart()
                                println("Order confirmed. Thank you for shopping! the order will take 20 Min to be delivered")
                                while (true) {
                                    println("do you want to continue?")
                                    println("1. Yes")
                                    println("2. No")
                                    println("Enter your choice (1/2): ")
                                    when (readlnOrNull()?.toIntOrNull()) {
                                        1 -> {
                                            break
                                        }

                                        2 -> {
                                            println("Goodbye!")
                                            exitProcess(0)
                                        }

                                        else -> {
                                            println("Invalid choice. please enter a 1 or 2 only!")
                                        }
                                    }
                                }
                            }

                            else -> println("Invalid payment method choice.")
                        }
                    }
                }


                5 -> {
                    println("Logging out. Goodbye!")
                    break
                }

                else -> println("Invalid choice. Please try again.")
            }
        }
    }

    private fun askForAdditionalItemsCost(): Double {
        var additionalCost = 0.0

        val additionalItems = mapOf(
            1 to Pair("mayonnaise", 1.0),
            2 to Pair("ketchup", 1.0),
            3 to Pair("hot sauce", 1.0),
            4 to Pair("water", 20.0),
            5 to Pair("coca-cola", 1.0),
            6 to Pair("juice", 1.0)
        )

        println("Do you want any additional items?")
        println("Additional items:")

        while (true) {
            for ((number, itemInfo) in additionalItems) {
                println("$number. ${itemInfo.first} - $${"%.2f".format(itemInfo.second)}")
            }

            print("Enter the item number (0 to exit): ")
            val selectedItem = readlnOrNull()?.toIntOrNull()

            if (selectedItem == 0) {
                break
            } else {
                val itemPrice = additionalItems[selectedItem]?.second
                if (itemPrice != null) {
                    additionalCost += itemPrice
                    println("Added ${additionalItems[selectedItem]?.first} to your order.")
                } else {
                    println("Invalid item number.")
                }
            }
        }

        if (additionalCost > 0.0) {
            println("Additional items cost added to the total: $${"%.2f".format(additionalCost)}")
        }

        return additionalCost
    }

    private fun askForSaladCustomizations(): customizationReturn {
        var customizationCost = 0.0
        val selectedOptions = mutableListOf<String>()
        val customizationOptions = mapOf(
            1 to Pair("sauce", 5.0),
            2 to Pair("onions", 5.0),
            3 to Pair("chicken", 5.0)
        )

        println("Do you want to add customizations to your Salad?")
        println("Available customizations:")

        while (true) {
            for ((number, itemInfo) in customizationOptions) {
                println("$number. ${itemInfo.first} - $${"%.2f".format(itemInfo.second)}")
            }
            println("Enter the item number (0 to exit): ")
            val input = readlnOrNull()?.toIntOrNull()
            if (input == 0) {
                break
            } else if (input in 1..customizationOptions.size) {
                val selectedOption = customizationOptions[input]
                if (selectedOption != null) {
                    val optionName = selectedOption.first
                    val optionPrice = selectedOption.second
                    customizationCost += optionPrice
                    selectedOptions.add(optionName)
                    println("Added $optionName to your order.")
                } else {
                    println("Invalid item number.")
                }
            } else {
                println("Invalid item. Enter a valid option number or 0 to exit.")
            }
        }

        if (customizationCost > 0.0) {
            println("Customizations cost added to the total: $${"%.2f".format(customizationCost)}")
        }

        return Pair(customizationCost, selectedOptions)
    }

    private fun askForCoffeeCustomizations(): customizationReturn {
        var customizationCost = 0.0
        val selectedOptions = mutableListOf<String>()
        val customizationOptions = mapOf(
            1 to Pair("extra sugar", 5.0),
            2 to Pair("milk", 5.0)
        )

        println("Do you want to add customizations to your Coffee?")
        println("Available customizations:")

        while (true) {
            for ((number, itemInfo) in customizationOptions) {
                println("$number. ${itemInfo.first} - $${"%.2f".format(itemInfo.second)}")
            }
            println("Enter the item number (0 to exit): ")
            val input = readlnOrNull()?.toIntOrNull()
            if (input == 0) {
                break
            } else if (input in 1..customizationOptions.size) {
                val selectedOption = customizationOptions[input]
                if (selectedOption != null) {
                    val optionName = selectedOption.first
                    val optionPrice = selectedOption.second
                    customizationCost += optionPrice
                    selectedOptions.add(optionName)
                    println("Added $optionName to your order.")
                } else {
                    println("Invalid item number.")
                }
            } else {
                println("Invalid item. Enter a valid option number or 0 to exit.")
            }
        }

        if (customizationCost > 0.0) {
            println("Customizations cost added to the total: $${"%.2f".format(customizationCost)}")
        }

        return Pair(customizationCost, selectedOptions)
    }

    private fun askForEggCustomizations(): customizationReturn {
        var customizationCost = 0.0
        val selectedOptions = mutableListOf<String>()
        val customizationOptions = mapOf(
            1 to Pair("cheese", 5.0),
            2 to Pair("tomatoes", 5.0)
        )

        println("Do you want to add customizations to your Egg?")
        println("Available customizations:")

        while (true) {
            for ((number, itemInfo) in customizationOptions) {
                println("$number. ${itemInfo.first} - $${"%.2f".format(itemInfo.second)}")
            }
            println("Enter the item number (0 to exit): ")
            val input = readlnOrNull()?.toIntOrNull()
            if (input == 0) {
                break
            } else if (input in 1..customizationOptions.size) {
                val selectedOption = customizationOptions[input]
                if (selectedOption != null) {
                    val optionName = selectedOption.first
                    val optionPrice = selectedOption.second
                    customizationCost += optionPrice
                    selectedOptions.add(optionName)
                    println("Added $optionName to your order.")
                } else {
                    println("Invalid item number.")
                }
            } else {
                println("Invalid item. Enter a valid option number or 0 to exit.")
            }
        }

        if (customizationCost > 0.0) {
            println("Customizations cost added to the total: $${"%.2f".format(customizationCost)}")
        }

        return Pair(customizationCost, selectedOptions)
    }


}