import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Пример паттерна 'Фабричный метод' ===\n");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите тип транспорта:");
        System.out.println("1 - Автомобиль");
        System.out.println("2 - Мотоцикл");
        System.out.println("3 - Грузовик");
        System.out.println("4 - Автобус (новый тип)");

        int choice = scanner.nextInt();
        scanner.nextLine();

        VehicleFactory factory = null;

        switch (choice) {
            case 1:
                System.out.print("Марка: ");
                String brand = scanner.nextLine();
                System.out.print("Модель: ");
                String model = scanner.nextLine();
                System.out.print("Тип топлива: ");
                String fuelType = scanner.nextLine();
                factory = new CarFactory(brand, model, fuelType);
                break;
            case 2:
                System.out.print("Введите тип мотоцикла (спортивный/туристический): ");
                String motoType = scanner.nextLine();
                System.out.print("Введите объем двигателя (в см³): ");
                int engineVolume = scanner.nextInt();
                factory = new MotorcycleFactory(motoType, engineVolume);
                break;
            case 3:
                System.out.print("Введите грузоподъемность (в кг): ");
                int capacity = scanner.nextInt();
                System.out.print("Введите количество осей: ");
                int axles = scanner.nextInt();
                factory = new TruckFactory(capacity, axles);
                break;
            case 4:
                System.out.print("Введите количество мест в автобусе: ");
                int seats = scanner.nextInt();
                System.out.print("Введите тип двигателя (дизель/электро): ");
                scanner.nextLine();
                String engineType = scanner.nextLine();
                factory = new BusFactory(seats, engineType);
                break;
            default:
                System.out.println("Неизвестный тип транспорта!");
                return;
        }

        // Создание транспорта с помощью фабрики
        IVehicle vehicle = factory.createVehicle();

        // Демонстрация работы
        System.out.println("\nСоздан транспорт:");
        vehicle.drive();
        vehicle.refuel();

        scanner.close();
    }
}

// Интерфейс IVehicle
interface IVehicle {
    void drive();
    void refuel();
}

// Конкретные классы транспорта

// Автомобиль
class Car implements IVehicle {
    private String brand;
    private String model;
    private String fuelType;

    public Car(String brand, String model, String fuelType) {
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
    }

    @Override
    public void drive() {
        System.out.println(brand + " " + model + " едет по дороге.");
    }

    @Override
    public void refuel() {
        System.out.println("Заправляем автомобиль топливом: " + fuelType);
    }
}

// Мотоцикл
class Motorcycle implements IVehicle {
    private String type;
    private int engineVolume;

    public Motorcycle(String type, int engineVolume) {
        this.type = type;
        this.engineVolume = engineVolume;
    }

    @Override
    public void drive() {
        System.out.println("Мотоцикл типа " + type + " с двигателем " + engineVolume + " см³ мчится по трассе.");
    }

    @Override
    public void refuel() {
        System.out.println("Заправляем мотоцикл бензином.");
    }
}

// Грузовик
class Truck implements IVehicle {
    private int capacity;
    private int axles;

    public Truck(int capacity, int axles) {
        this.capacity = capacity;
        this.axles = axles;
    }

    @Override
    public void drive() {
        System.out.println("Грузовик перевозит " + capacity + " кг груза на " + axles + " осях.");
    }

    @Override
    public void refuel() {
        System.out.println("Заправляем дизельное топливо для грузовика.");
    }
}

// Автобус
class Bus implements IVehicle {
    private int seats;
    private String engineType;

    public Bus(int seats, String engineType) {
        this.seats = seats;
        this.engineType = engineType;
    }

    @Override
    public void drive() {
        System.out.println("Автобус вмещает " + seats + " пассажиров и выезжает на маршрут.");
    }

    @Override
    public void refuel() {
        System.out.println("Заправляем автобус типом двигателя: " + engineType);
    }
}

// Абстрактная фабрика (основа паттерна)
abstract class VehicleFactory {
    public abstract IVehicle createVehicle(); // фабричный метод
}

// Конкретные фабрики
// Фабрика для автомобиля
class CarFactory extends VehicleFactory {
    private String brand;
    private String model;
    private String fuelType;

    public CarFactory(String brand, String model, String fuelType) {
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
    }

    @Override
    public IVehicle createVehicle() {
        return new Car(brand, model, fuelType);
    }
}

// Фабрика для мотоцикла
class MotorcycleFactory extends VehicleFactory {
    private String type;
    private int engineVolume;

    public MotorcycleFactory(String type, int engineVolume) {
        this.type = type;
        this.engineVolume = engineVolume;
    }

    @Override
    public IVehicle createVehicle() {
        return new Motorcycle(type, engineVolume);
    }
}

// Фабрика для грузовика
class TruckFactory extends VehicleFactory {
    private int capacity;
    private int axles;

    public TruckFactory(int capacity, int axles) {
        this.capacity = capacity;
        this.axles = axles;
    }

    @Override
    public IVehicle createVehicle() {
        return new Truck(capacity, axles);
    }
}

// Фабрика для автобуса (новый тип транспорта)
class BusFactory extends VehicleFactory {
    private int seats;
    private String engineType;

    public BusFactory(int seats, String engineType) {
        this.seats = seats;
        this.engineType = engineType;
    }

    @Override
    public IVehicle createVehicle() {
        return new Bus(seats, engineType);
    }
}
