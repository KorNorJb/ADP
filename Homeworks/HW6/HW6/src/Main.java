import java.util.*;

interface IPaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements IPaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплата " + amount + " тенге с банковской карты: " + cardNumber);
    }
}

class PayPalPayment implements IPaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплата " + amount + " тенге через PayPal аккаунт: " + email);
    }
}

class CryptoPayment implements IPaymentStrategy {
    private String walletAddress;

    public CryptoPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплата " + amount + " тенге криптой, кошелёк: " + walletAddress);
    }
}

class PaymentContext {
    private IPaymentStrategy strategy;

    public void setPaymentStrategy(IPaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executePayment(double amount) {
        if (strategy == null) {
            System.out.println("Ошибка: способ оплаты не выбран!");
        } else {
            strategy.pay(amount);
        }
    }
}

interface IObserver {
    void update(String currency, double rate);
}

interface ISubject {
    void addObserver(IObserver o);
    void removeObserver(IObserver o);
    void notifyObservers();
}

class CurrencyExchange implements ISubject {
    private Map<String, Double> rates = new HashMap<>();
    private List<IObserver> observers = new ArrayList<>();

    public void setRate(String currency, double rate) {
        rates.put(currency, rate);
        notifyObservers();
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            for (var entry : rates.entrySet()) {
                observer.update(entry.getKey(), entry.getValue());
            }
        }
    }
}

class BankDisplay implements IObserver {
    @Override
    public void update(String currency, double rate) {
        System.out.println("Банк: Новый курс " + currency + ": " + rate);
    }
}

class MobileAppDisplay implements IObserver {
    @Override
    public void update(String currency, double rate) {
        System.out.println("Мобильное приложение:" + currency + " теперь = " + rate);
    }
}

class AnalyticsSystem implements IObserver {
    @Override
    public void update(String currency, double rate) {
        System.out.println("Аналитическая система: Обновлён курс " + currency + ": " + rate);
    }
}

public class Main {
    public static void main(String[] args) {

        PaymentContext context = new PaymentContext();

        context.setPaymentStrategy(new CreditCardPayment("4362534763346564"));
        context.executePayment(23572384);

        context.setPaymentStrategy(new PayPalPayment("najomob@gmail.com"));
        context.executePayment(2359932);

        context.setPaymentStrategy(new CryptoPayment("3567th563"));
        context.executePayment(343550);


        CurrencyExchange exchange = new CurrencyExchange();

        IObserver bank = new BankDisplay();
        IObserver app = new MobileAppDisplay();
        IObserver analytics = new AnalyticsSystem();

        exchange.addObserver(bank);
        exchange.addObserver(app);
        exchange.addObserver(analytics);

        exchange.setRate("USD", 535);
        exchange.setRate("EUR", 621);

        exchange.removeObserver(app);
        System.out.println("\nОтключение увед\n");

        exchange.setRate("USD", 525);
    }
}
