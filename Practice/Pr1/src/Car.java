public class Car extends Vehicle {
    private int doors;
    private String transmission;

    public Car(String brand, String model, int year, int doors, String transmission) {
        super(brand, model, year);
        this.doors = doors;
        this.transmission = transmission;
    }

    @Override
    public void startEngine() {
        System.out.println("Автомобиль " + brand + " завёл двигатель.");
    }

    @Override
    public void stopEngine() {
        System.out.println("Автомобиль " + brand + " заглушил двигатель.");
    }

    @Override
    public void showDetails() {
        System.out.println("Бренд: " + brand);
        System.out.println("Модель: " + model);
        System.out.println("---------------------------");
    }
}
