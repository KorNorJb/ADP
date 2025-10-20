public class Main {
    public static void main(String[] args) {
        System.out.println("=== SRP (Single Responsibility Principle) ===");
        Order order = new Order("Asus Rog Strix", 1, 800000);
        PriceCalculator calculator = new PriceCalculator();
        PaymentProcessor payment = new PaymentProcessor();
        EmailSender emailSender = new EmailSender();

        double total = calculator.calculateTotalPrice(order);
        System.out.println("Итоговая цена: " + total);
        payment.processPayment("VISA");
        emailSender.sendConfirmationEmail("najomob@gmail.com");

        System.out.println("\n=== OCP (Open/Closed Principle) ===");
        Employee Dmitriy = new PermanentEmployee("Дмитрий", 50000);
        Employee Krisanto = new ContractEmployee("Крисанто", 40000);
        Employee Max = new InternEmployee("Максим", 20000);

        System.out.println(Dmitriy.getName() + ": " + Dmitriy.calculateSalary());
        System.out.println(Krisanto.getName() + ": " + Krisanto.calculateSalary());
        System.out.println(Max.getName() + ": " + Max.calculateSalary());

        System.out.println("\n=== ISP (Interface Segregation Principle) ===");
        AllInOnePrinter aio = new AllInOnePrinter();
        aio.print("Документ 1");
        aio.scan("Документ 2");
        aio.fax("Документ 3");

        BasicPrinter basic = new BasicPrinter();
        basic.print("Документ 4");

        System.out.println("\n=== DIP (Dependency Inversion Principle) ===");
        MessageSender emailService = new EmailNotification();
        MessageSender smsService = new SmsNotification();

        NotificationService notification1 = new NotificationService(emailService);
        notification1.sendNotification("Ваш заказ подтвержден!");

        NotificationService notification2 = new NotificationService(smsService);
        notification2.sendNotification("Ваш заказ доставлен!");
    }
}


// SRP — Single Responsibility Principle
class Order {
    private String productName;
    private int quantity;
    private double price;

    public Order(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}

class PriceCalculator {
    public double calculateTotalPrice(Order order) {
        return order.getQuantity() * order.getPrice() * 0.9;
    }
}

class PaymentProcessor {
    public void processPayment(String paymentDetails) {
        System.out.println("Оплата произведена с помощью: " + paymentDetails);
    }
}

class EmailSender {
    public void sendConfirmationEmail(String email) {
        System.out.println("Письмо-подтверждение отправлено на: " + email);
    }
}

// OCP — Open/Closed Principle

// Абстракция сотрудника
abstract class Employee {
    protected String name;
    protected double baseSalary;

    public Employee(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    public String getName() { return name; }

    public abstract double calculateSalary();
}

// Разные типы сотрудников
class PermanentEmployee extends Employee {
    public PermanentEmployee(String name, double baseSalary) {
        super(name, baseSalary);
    }
    @Override
    public double calculateSalary() {
        return baseSalary * 1.2; // 20% бонус
    }
}

class ContractEmployee extends Employee {
    public ContractEmployee(String name, double baseSalary) {
        super(name, baseSalary);
    }
    @Override
    public double calculateSalary() {
        return baseSalary * 1.1; // 10% бонус
    }
}

class InternEmployee extends Employee {
    public InternEmployee(String name, double baseSalary) {
        super(name, baseSalary);
    }
    @Override
    public double calculateSalary() {
        return baseSalary * 0.8; // 80% от ставки
    }
}


// ISP — Interface Segregation Principle
interface Printer {
    void print(String content);
}

interface Scanner {
    void scan(String content);
}

interface Fax {
    void fax(String content);
}

class AllInOnePrinter implements Printer, Scanner, Fax {
    public void print(String content) {
        System.out.println("Печать: " + content);
    }
    public void scan(String content) {
        System.out.println("Сканирование: " + content);
    }
    public void fax(String content) {
        System.out.println("Отправка факса: " + content);
    }
}

class BasicPrinter implements Printer {
    public void print(String content) {
        System.out.println("Печать: " + content);
    }
}

// DIP — Dependency Inversion Principle
// Интерфейс для отправки уведомлений
interface MessageSender {
    void sendMessage(String message);
}

// Конкретные реализации
class EmailNotification implements MessageSender {
    public void sendMessage(String message) {
        System.out.println("Email отправлен: " + message);
    }
}

class SmsNotification implements MessageSender {
    public void sendMessage(String message) {
        System.out.println("SMS отправлено: " + message);
    }
}

// Класс зависит от абстракции, а не от конкретных реализаций
class NotificationService {
    private MessageSender sender;

    public NotificationService(MessageSender sender) {
        this.sender = sender;
    }

    public void sendNotification(String message) {
        sender.sendMessage(message);
    }
}
