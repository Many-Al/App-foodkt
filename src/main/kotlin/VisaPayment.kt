

class VisaPayment : PaymentGateway() {
    override fun processPayment(amount: Double) {
        println("Processing Visa payment for $${"%.2f".format(amount)}")
    }
}