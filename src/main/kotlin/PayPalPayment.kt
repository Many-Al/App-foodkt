// neu code


class PayPalPayment : PaymentGateway() {
    override fun processPayment(amount: Double) {
        println("Processing PayPal payment for $${"%.2f".format(amount)}")
    }
}