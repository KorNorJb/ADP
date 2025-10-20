public class Moto extends Vehicle{
    private String bodyType;
    private boolean hasSideBox;

    public Moto(String brand, String model, int year, String bodyType, boolean hasSideBox) {
        super(brand, model, year);
        this.bodyType = bodyType;
        this.hasSideBox = hasSideBox;
    }

    @Override
    public void startEngine() {
        System.out.println("Мотоцикл " + brand + " завёл двигатель.");
    }
    @Override
    public void stopEngine() {
        System.out.println("Мотоцикл " + brand + " заглушил двигатель.");
    }

    @Override
    public void showDetails() {
        super.showDetails();
        System.out.println("Тип кузова: " + bodyType);
        System.out.println("Наличие бокса: " + (hasSideBox ? "Да" : "Нет"));
        System.out.println("---------------------------");
    }
}
