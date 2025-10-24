import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Order order = new Order();
        order.addProduct(new Product("Айфон", 4356437, 1));
        order.addProduct(new Product("Чехол", 4324, 2));

        order.setPayment(new CreditCardPayment());
        order.setDelivery(new CourierDelivery());
        order.setNotification(new EmailNotification());

        DiscountCalculator discountCalculator = new DiscountCalculator();
        double total = order.calculateTotal(discountCalculator);

        order.getPayment().ProcessPayment(total);

        order.getDelivery().DeliverOrder(order);

        order.getNotification().SendNotification("Ваш заказ оформлен. Сумма к оплате: " + total + " тг.");
    }
}

class Product {
    private final String name;
    private final double price;
    private final int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}

class Order {
    private final List<Product> products = new ArrayList<>();
    private IPayment payment;
    private IDelivery delivery;
    private INotification notification;

    public void addProduct(Product p) {
        products.add(p);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setPayment(IPayment payment) {
        this.payment = payment;
    }

    public void setDelivery(IDelivery delivery) {
        this.delivery = delivery;
    }

    public void setNotification(INotification notification) {
        this.notification = notification;
    }
    public IPayment getPayment() {
        return payment;
    }

    public IDelivery getDelivery() {
        return delivery;
    }

    public INotification getNotification() {
        return notification;
    }

    public double calculateTotal(DiscountCalculator discountCalculator) {
        double sum = 0;
        for (Product p : products) {
            sum += p.getTotalPrice();
        }
        return discountCalculator.applyDiscount(sum);
    }
}

interface IPayment {
    void ProcessPayment(double amount);
}

class CreditCardPayment implements IPayment {
    public void ProcessPayment(double amount) {
        System.out.println("Оплата картой на сумму " + amount + " тг.");
    }
}

class PayPalPayment implements IPayment {
    public void ProcessPayment(double amount) {
        System.out.println("Оплата через PayPal на сумму " + amount + " тг.");
    }
}

class BankTransferPayment implements IPayment {
    public void ProcessPayment(double amount) {
        System.out.println("Оплата банковским переводом на сумму " + amount + " тг.");
    }
}

interface IDelivery {
    void DeliverOrder(Order order);
}

class CourierDelivery implements IDelivery {
    public void DeliverOrder(Order order) {
        System.out.println("Доставка курьером оформлена.");
    }
}

class PostDelivery implements IDelivery {
    public void DeliverOrder(Order order) {
        System.out.println("Доставка почтой оформлена.");
    }
}

class PickUpPointDelivery implements IDelivery {
    public void DeliverOrder(Order order) {
        System.out.println("Самовывоз оформлен.");
    }
}

interface INotification {
    void SendNotification(String message);
}

class EmailNotification implements INotification {
    public void SendNotification(String message) {
        System.out.println("Email: " + message);
    }
}
class SmsNotification implements INotification {
    public void SendNotification(String message) {
        System.out.println("SMS: " + message);
    }
}


class DiscountCalculator {

    public double applyDiscount(double total) {
        if (total > 300000) {
            System.out.println("Применена скидка 5%.");
            return total * 0.95;
        }
        return total;
    }
}
