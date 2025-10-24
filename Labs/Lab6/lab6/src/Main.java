import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите задачу:");
        System.out.println("1 Расчет стоимости доставки");
        System.out.println("2 Мониторинг погоды");
        String taskChoice = scanner.nextLine();

        if (taskChoice.equals("1")) {
            runStrategyExample(scanner);
        } else if (taskChoice.equals("2")) {
            runObserverExample();
        } else {
            System.out.println("Неверный выбор.");
        }
    }

    //СТРАТЕГИЯ

    private static void runStrategyExample(Scanner scanner) {
        DeliveryContext deliveryContext = new DeliveryContext();

        System.out.println("Выберите тип доставки: 1 - Стандартная, 2 - Экспресс, 3 - Международная, 4 - Ночная");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                deliveryContext.setShippingStrategy(new StandardShippingStrategy());
                break;
            case "2":
                deliveryContext.setShippingStrategy(new ExpressShippingStrategy());
                break;
            case "3":
                deliveryContext.setShippingStrategy(new InternationalShippingStrategy());
                break;
            case "4":
                deliveryContext.setShippingStrategy(new NightShippingStrategy());
                break;
            default:
                System.out.println("Неверный выбор.");
                return;
        }

        System.out.print("Введите вес посылки (кг): ");
        double weight = readPositiveDouble(scanner);

        System.out.print("Введите расстояние доставки (км): ");
        double distance = readPositiveDouble(scanner);

        double cost = deliveryContext.calculateCost(weight, distance);
        System.out.printf("Стоимость доставки: %.2f₸\n", cost);
    }

    private static double readPositiveDouble(Scanner scanner) {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.print("Введите положительное значение: ");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Некорректный ввод. Введите число: ");
            }
        }
    }

    interface IShippingStrategy {
        double calculateShippingCost(double weight, double distance);
    }

    static class StandardShippingStrategy implements IShippingStrategy {
        public double calculateShippingCost(double weight, double distance) {
            return weight * 0.5 + distance * 0.1;
        }
    }

    static class ExpressShippingStrategy implements IShippingStrategy {
        public double calculateShippingCost(double weight, double distance) {
            return (weight * 0.75 + distance * 0.2) + 10;
        }
    }

    static class InternationalShippingStrategy implements IShippingStrategy {
        public double calculateShippingCost(double weight, double distance) {
            return weight * 1.0 + distance * 0.5 + 15;
        }
    }

    static class NightShippingStrategy implements IShippingStrategy {
        public double calculateShippingCost(double weight, double distance) {
            return (weight * 0.6 + distance * 0.15) + 20;
        }
    }

    static class DeliveryContext {
        private IShippingStrategy shippingStrategy;

        public void setShippingStrategy(IShippingStrategy strategy) {
            this.shippingStrategy = strategy;
        }

        public double calculateCost(double weight, double distance) {
            if (shippingStrategy == null) {
                throw new IllegalStateException("Стратегия доставки не установлена.");
            }
            return shippingStrategy.calculateShippingCost(weight, distance);
        }
    }

    //НАБЛЮДАТЕЛЬ

    private static void runObserverExample() {
        WeatherStation weatherStation = new WeatherStation();

        WeatherDisplay mobileApp = new WeatherDisplay("Мобильное приложение");
        WeatherDisplay digitalBillboard = new WeatherDisplay("Электронное табло");
        WeatherDisplay emailAlert = new WeatherDisplay("Email-оповещение");

        weatherStation.registerObserver(mobileApp);
        weatherStation.registerObserver(digitalBillboard);
        weatherStation.registerObserver(emailAlert);

        weatherStation.setTemperature(25.0f);
        weatherStation.setTemperature(30.0f);

        weatherStation.removeObserver(digitalBillboard);
        weatherStation.setTemperature(28.0f);
    }

    interface IObserver {
        void update(float temperature);
    }

    interface ISubject {
        void registerObserver(IObserver observer);
        void removeObserver(IObserver observer);
        void notifyObservers();
    }

    static class WeatherStation implements ISubject {
        private final List<IObserver> observers = new ArrayList<>();
        private float temperature;

        public void registerObserver(IObserver observer) {
            observers.add(observer);
        }

        public void removeObserver(IObserver observer) {
            if (!observers.remove(observer)) {
                System.out.println("Ошибка: наблюдатель не найден.");
            }
        }

        public void notifyObservers() {
            for (IObserver observer : observers) {
                observer.update(temperature);
            }
        }

        public void setTemperature(float newTemperature) {
            if (Float.isNaN(newTemperature)) {
                System.out.println("Ошибка: некорректное значение температуры.");
                return;
            }
            System.out.println("Изменение температуры: " + newTemperature + "°C");
            this.temperature = newTemperature;
            notifyObservers();
        }
    }

    static class WeatherDisplay implements IObserver {
        private final String name;

        public WeatherDisplay(String name) {
            this.name = name;
        }

        public void update(float temperature) {
            System.out.println(name + " показывает новую температуру: " + temperature + "°C");
        }
    }

    static class EmailNotification implements IObserver {
        private final String name;

        public EmailNotification(String name) {
            this.name = name;
        }

        public void update(float temperature) {
            System.out.println(name + ": Отправлено уведомление о температуре " + temperature + "°C");
        }
    }
}