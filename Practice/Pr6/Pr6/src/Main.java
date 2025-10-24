import java.util.*;
import java.util.concurrent.*;

// ====================== ВХОД В ПРОГРАММУ ======================
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Выберите задачу:");
        System.out.println("1 - Стратегия (Travel Booking)");
        System.out.println("2 - Наблюдатель (Stock Exchange)");
        String choice = in.nextLine();

        if (choice.equals("1")) runStrategy(in);
        else if (choice.equals("2")) runObserver();
        else System.out.println("Неверный выбор.");
    }

    private static void runStrategy(Scanner in) {
        System.out.println("=== Travel Booking ===");
        TravelBookingContext context = new TravelBookingContext();

        System.out.println("Выберите транспорт: 1 - Самолет, 2 - Поезд, 3 - Автобус");
        String transport = in.nextLine();
        switch (transport) {
            case "1": context.setStrategy(new AirplaneCostStrategy()); break;
            case "2": context.setStrategy(new TrainCostStrategy()); break;
            case "3": context.setStrategy(new BusCostStrategy()); break;
            default: System.out.println("Неверный выбор."); return;
        }

        System.out.print("Введите расстояние (км): ");
        double distance = Double.parseDouble(in.nextLine());
        System.out.print("Количество пассажиров: ");
        int passengers = Integer.parseInt(in.nextLine());
        System.out.print("Класс обслуживания (economy/business): ");
        String serviceClass = in.nextLine().trim().toLowerCase();
        System.out.print("Есть скидка (дети/пенсионер/нет): ");
        String discount = in.nextLine().trim().toLowerCase();

        double cost = context.calculate(distance, passengers, serviceClass, discount);
        System.out.printf("Стоимость поездки: %.2f%n", cost);
    }

    private static void runObserver() {
        System.out.println("=== Stock Exchange Simulation ===");
        StockSimulation.simulate();
    }
}

// --------- Интерфейс стратегии ---------
interface ICostCalculationStrategy {
    double calculateCost(double distance, int passengers, String serviceClass, String discount);
}

// --------- Стратегии ---------
class AirplaneCostStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, int passengers, String serviceClass, String discount) {
        double base = distance * 10 * passengers;
        if (serviceClass.equals("business")) base *= 1.5;
        if (discount.equals("дети")) base *= 0.7;
        if (discount.equals("пенсионер")) base *= 0.8;
        return base;
    }
}

class TrainCostStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, int passengers, String serviceClass, String discount) {
        double base = distance * 3 * passengers;
        if (serviceClass.equals("business")) base *= 1.3;
        if (discount.equals("дети")) base *= 0.5;
        if (discount.equals("пенсионер")) base *= 0.85;
        return base;
    }
}

class BusCostStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, int passengers, String serviceClass, String discount) {
        double base = distance * 2 * passengers;
        if (serviceClass.equals("business")) base *= 1.2;
        if (discount.equals("дети")) base *= 0.6;
        if (discount.equals("пенсионер")) base *= 0.9;
        return base;
    }
}

// --------- Контекст ---------
class TravelBookingContext {
    private ICostCalculationStrategy strategy;
    public void setStrategy(ICostCalculationStrategy strategy) { this.strategy = strategy; }
    public double calculate(double distance, int passengers, String serviceClass, String discount) {
        if (strategy == null) throw new IllegalStateException("Стратегия не выбрана.");
        return strategy.calculateCost(distance, passengers, serviceClass, discount);
    }
}

// ====================== НАБЛЮДАТЕЛЬ ======================
interface IObserver {
    void update(String stock, double price);
}

interface ISubject {
    void register(String stock, IObserver observer);
    void remove(String stock, IObserver observer);
    void notifyObservers(String stock);
}

class StockExchange implements ISubject {
    private final Map<String, Double> prices = new ConcurrentHashMap<>();
    private final Map<String, List<IObserver>> observers = new ConcurrentHashMap<>();
    public void setPrice(String stock, double price) {
        prices.put(stock, price);
        System.out.printf("Цена акции %s изменилась: %.2f%n", stock, price);
        notifyObservers(stock);
    }
    public void register(String stock, IObserver observer) {
        observers.computeIfAbsent(stock, k -> new ArrayList<>()).add(observer);
        System.out.println("Добавлен наблюдатель для " + stock);
    }
    public void remove(String stock, IObserver observer) {
        if (observers.containsKey(stock)) observers.get(stock).remove(observer);
        System.out.println("Удален наблюдатель для " + stock);
    }
    public void notifyObservers(String stock) {
        if (!observers.containsKey(stock)) return;
        double price = prices.getOrDefault(stock, 0.0);
        for (IObserver o : observers.get(stock)) {
            CompletableFuture.runAsync(() -> o.update(stock, price));
        }
    }
}

class Trader implements IObserver {
    private final String name;
    public Trader(String name) { this.name = name; }
    public void update(String stock, double price) {
        System.out.println(name + " получил обновление: " + stock + " = " + price);
    }
}

class TradingBot implements IObserver {
    private final String name;
    private final double threshold;
    public TradingBot(String name, double threshold) {
        this.name = name; this.threshold = threshold;
    }
    public void update(String stock, double price) {
        if (price < threshold) System.out.println(name + ": покупаю " + stock);
        else System.out.println(name + ": продаю " + stock);
    }
}

class StockSimulation {
    public static void simulate() {
        StockExchange exchange = new StockExchange();
        Trader t1 = new Trader("Трейдер-Дима");
        Trader t2 = new Trader("Трейдер-Рой");
        TradingBot bot = new TradingBot("Ариф2005", 100.0);
        exchange.register("AAPL", t1);
        exchange.register("AAPL", bot);
        exchange.register("GOOG", t2);

        exchange.setPrice("AAPL", 120.5);
        exchange.setPrice("GOOG", 95.2);
        exchange.setPrice("AAPL", 85.7);
    }
}
