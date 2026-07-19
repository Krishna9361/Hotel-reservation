package hotel;

public class PaymentProcessor {
    public boolean processPayment(String guestName, double amount) {
        System.out.println("Processing payment for " + guestName + "...");
        System.out.println("Amount charged: $" + String.format("%.2f", amount));
        System.out.println("Payment approved.");
        return true;
    }
}
