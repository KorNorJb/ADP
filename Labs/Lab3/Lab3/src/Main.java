import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("SRP");
        List<Product> productList = Arrays.asList(
                new Product("PC", 1200.0),
                new Product("Keyboard", 300.0)
        );
        Invoice invoice = new Invoice(101, productList, 0.2);
        InvoiceCalculator invoiceCalc = new InvoiceCalculator();
        InvoiceRepository invoiceRepo = new InvoiceRepository();

        double totalAmount = invoiceCalc.calculateTotal(invoice);
        System.out.println("Total invoice amount: " + totalAmount);
        invoiceRepo.saveInvoice(invoice);

        System.out.println("\nOCP");
        DiscountCalculator discountCalc = new DiscountCalculator();
        System.out.println("Regular customer: " + discountCalc.calculate(new RegularCustomerDiscount(), 1000));
        System.out.println("Silver customer: " + discountCalc.calculate(new SilverCustomerDiscount(), 1000));
        System.out.println("Gold customer: " + discountCalc.calculate(new GoldCustomerDiscount(), 1000));

        System.out.println("\nISP");
        HumanWorker human = new HumanWorker();
        human.work();
        human.eat();
        human.sleep();

        RobotWorker robot = new RobotWorker();
        robot.work();

        System.out.println("\nDIP");
        NotificationManager emailManager = new NotificationManager(new EmailNotificationService());
        emailManager.notifyUser("Your order has been confirmed!");

        NotificationManager smsManager = new NotificationManager(new SmsNotificationService());
        smsManager.notifyUser("Your verification code is 1934532");
    }
}


// SRP
class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

class Invoice {
    private int invoiceId;
    private List<Product> products;
    private double taxRate;

    public Invoice(int invoiceId, List<Product> products, double taxRate) {
        this.invoiceId = invoiceId;
        this.products = products;
        this.taxRate = taxRate;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getTaxRate() {
        return taxRate;
    }
}

class InvoiceCalculator {
    public double calculateTotal(Invoice invoice) {
        double subtotal = 0;
        for (Product product : invoice.getProducts()) {
            subtotal += product.getPrice();
        }
        return subtotal + (subtotal * invoice.getTaxRate());
    }
}

class InvoiceRepository {
    public void saveInvoice(Invoice invoice) {
        System.out.println("Invoice #" + invoice.getInvoiceId() + " saved successfully.");
    }
}


// OCP
interface DiscountStrategy {
    double applyDiscount(double amount);
}

class RegularCustomerDiscount implements DiscountStrategy {
    public double applyDiscount(double amount) {
        return amount;
    }
}

class SilverCustomerDiscount implements DiscountStrategy {
    public double applyDiscount(double amount) {
        return amount * 0.9;
    }
}

class GoldCustomerDiscount implements DiscountStrategy {
    public double applyDiscount(double amount) {
        return amount * 0.8;
    }
}

class DiscountCalculator {
    public double calculate(DiscountStrategy strategy, double amount) {
        return strategy.applyDiscount(amount);
    }
}


//ISP
interface Worker {
    void work();
}

interface Eater {
    void eat();
}

interface Sleeper {
    void sleep();
}

class HumanWorker implements Worker, Eater, Sleeper {
    public void work() {
        System.out.println("Human is working...");
    }

    public void eat() {
        System.out.println("Human is eating...");
    }

    public void sleep() {
        System.out.println("Human is sleeping...");
    }
}

class RobotWorker implements Worker {
    public void work() {
        System.out.println("Robot is working...");
    }
}


// DIP
interface NotificationService {
    void sendMessage(String message);
}

class EmailNotificationService implements NotificationService {
    public void sendMessage(String message) {
        System.out.println("Email sent: " + message);
    }
}

class SmsNotificationService implements NotificationService {
    public void sendMessage(String message) {
        System.out.println("SMS sent: " + message);
    }
}

class NotificationManager {
    private NotificationService notificationService;

    public NotificationManager(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void notifyUser(String message) {
        notificationService.sendMessage(message);
    }
}
