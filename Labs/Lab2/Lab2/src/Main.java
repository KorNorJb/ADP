public class Main {

    // DRY
    static class OrderService {
        private void printOrder(String action, String name, int count, double price) {
            double total = count * price;
            System.out.println("Order for " + name + " " + action + ". Total: " + total);
        }

        public void createOrder(String name, int count, double price) {
            printOrder("created", name, count, price);
        }

        public void updateOrder(String name, int count, double price) {
            printOrder("updated", name, count, price);
        }
    }
    static class Vehicle {
        public void start() {
            System.out.println(getClass().getSimpleName() + " is starting");
        }

        public void stop() {
            System.out.println(getClass().getSimpleName() + " is stopping");
        }
    }

    static class Car extends Vehicle {}
    static class Truck extends Vehicle {}

    // KISS

    static class Calculator {
        public void add(int a, int b) {
            System.out.println("Sum = " + (a + b));
        }

        public void subtract(int a, int b) {
            System.out.println("Difference = " + (a - b));
        }
    }
    static class SimpleSingleton {
        private static SimpleSingleton instance;

        private SimpleSingleton() {}

        public static SimpleSingleton getInstance() {
            if (instance == null) {
                instance = new SimpleSingleton();
            }
            return instance;
        }

        public void showMessage() {
            System.out.println("ACTION!!");
        }
    }

    // YAGNI
    static class Circle {
        private double radius;

        public Circle(double radius) {
            this.radius = radius;
        }

        public double area() {
            return Math.PI * radius * radius;
        }
    }
    static class MathOperations {
        public int add(int a, int b) {
            return a + b;
        }
    }

    public static void main(String[] args) {

        System.out.println("DRY");
        OrderService service = new OrderService();
        service.createOrder("Iphone 17", 2, 1400.0);
        service.updateOrder("Iphone 17", 3, 2100.0);

        Car car = new Car();
        car.start();
        car.stop();

        Truck truck = new Truck();
        truck.start();
        truck.stop();

        System.out.println("\nKISS");
        Calculator calc = new Calculator();
        calc.add(20, 12);
        calc.subtract(47, 8);
        SimpleSingleton.getInstance().showMessage();

        System.out.println("\nYAGNI");
        Circle c = new Circle(5);
        System.out.println("Area of circle: " + c.area());

        MathOperations m = new MathOperations();
        System.out.println("Sum: " + m.add(12, 8));
    }
}
