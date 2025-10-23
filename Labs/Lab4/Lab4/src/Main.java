import java.util.*;

// MAIN
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Transport> createdTransports = new ArrayList<>();


        boolean running = true;
        while (running) {
            System.out.println("\nДоступные типы транспорта:");
            System.out.println("1 - Автомобиль");
            System.out.println("2 - Мотоцикл");
            System.out.println("3 - Самолет");
            System.out.println("4 - Велосипед");
            System.out.println("0 - Показать все созданные транспорты и выйти");
            System.out.print("Выберите тип транспорта (0-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            if (choice == 0) {
                running = false;
                break;
            }

            System.out.print("Введите модель транспорта: ");
            String model = scanner.nextLine();

            System.out.print("Введите максимальную скорость (км/ч): ");
            int speed = scanner.nextInt();
            scanner.nextLine();

            TransportFactory factory = null;

            switch (choice) {
                case 1:
                    factory = new CarFactory();
                    break;
                case 2:
                    factory = new MotorcycleFactory();
                    break;
                case 3:
                    factory = new PlaneFactory();
                    break;
                case 4:
                    factory = new BicycleFactory();
                    break;
                default:
                    System.out.println("Неверный выбор!");
                    continue;
            }

            Transport transport = factory.createTransport(model, speed);
            createdTransports.add(transport);

            System.out.println("\n=== Создан транспорт ===");
            System.out.println("Тип: " + transport.getClass().getSimpleName());
            System.out.println("Модель: " + model);
            System.out.println("Скорость: " + speed + " км/ч");

            transport.move();
            transport.fuelUp();
        }

        System.out.println("\nВсе созданные транспорты");
        if (createdTransports.isEmpty()) {
            System.out.println("Вы не создали ни одного транспорта.");
        } else {
            for (int i = 0; i < createdTransports.size(); i++) {
                System.out.println((i + 1) + ". " + createdTransports.get(i).getInfo());
            }
        }

        System.out.println("\nГотово!");
        scanner.close();
    }
}


// Transport Interface
interface Transport {
    void move();
    void fuelUp();
    String getInfo(); // добавлено для описания
}


// Transport Implementations
class Car implements Transport {
    private String model;
    private int speed;

    public Car(String model, int speed) {
        this.model = model;
        this.speed = speed;
    }

    @Override
    public void move() {
        System.out.println("Автомобиль " + model + " едет со скоростью " + speed + " км/ч.");
    }

    @Override
    public void fuelUp() {
        System.out.println("Автомобиль " + model + " заправляется бензином.");
    }

    @Override
    public String getInfo() {
        return "Автомобиль: " + model + " (" + speed + " км/ч)";
    }
}

class Motorcycle implements Transport {
    private String model;
    private int speed;

    public Motorcycle(String model, int speed) {
        this.model = model;
        this.speed = speed;
    }

    @Override
    public void move() {
        System.out.println("Мотоцикл " + model + " едет со скоростью " + speed + " км/ч.");
    }

    @Override
    public void fuelUp() {
        System.out.println("Мотоцикл " + model + " заправляется бензином.");
    }

    @Override
    public String getInfo() {
        return "Мотоцикл: " + model + " (" + speed + " км/ч)";
    }
}

class Plane implements Transport {
    private String model;
    private int speed;

    public Plane(String model, int speed) {
        this.model = model;
        this.speed = speed;
    }

    @Override
    public void move() {
        System.out.println("Самолет " + model + " летит со скоростью " + speed + " км/ч.");
    }

    @Override
    public void fuelUp() {
        System.out.println("Самолет " + model + " заправляется керосином.");
    }

    @Override
    public String getInfo() {
        return "Самолет: " + model + " (" + speed + " км/ч)";
    }
}

class Bicycle implements Transport {
    private String model;
    private int speed;

    public Bicycle(String model, int speed) {
        this.model = model;
        this.speed = speed;
    }

    @Override
    public void move() {
        System.out.println("Велосипед " + model + " движется со скоростью " + speed + " км/ч.");
    }

    @Override
    public void fuelUp() {
        System.out.println("Велосипед " + model + " тренит твои ноги!!!");
    }

    @Override
    public String getInfo() {
        return "Велосипед: " + model + " (" + speed + " км/ч)";
    }
}


// Transport Factories
abstract class TransportFactory {
    public abstract Transport createTransport(String model, int speed);
}

class CarFactory extends TransportFactory {
    @Override
    public Transport createTransport(String model, int speed) {
        return new Car(model, speed);
    }
}

class MotorcycleFactory extends TransportFactory {
    @Override
    public Transport createTransport(String model, int speed) {
        return new Motorcycle(model, speed);
    }
}

class PlaneFactory extends TransportFactory {
    @Override
    public Transport createTransport(String model, int speed) {
        return new Plane(model, speed);
    }
}

class BicycleFactory extends TransportFactory {
    @Override
    public Transport createTransport(String model, int speed) {
        return new Bicycle(model, speed);
    }
}
