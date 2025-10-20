import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Fleet fleet = new Fleet();

        while (true) {
            System.out.println("\n===== МЕНЮ АВТОПАРКА =====");
            System.out.println("1. Добавить гараж");
            System.out.println("2. Добавить транспортное средство");
            System.out.println("3. Показать весь автопарк");
            System.out.println("4. Найти транспорт по марке");
            System.out.println("5. Удалить гараж");
            System.out.println("0. Выйти");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Создание нового гаража
                    Garage garage = new Garage();
                    fleet.addGarage(garage);
                    System.out.println("Гараж успешно добавлен!");
                    break;

                case 2:
                    if (fleet.getGarages().isEmpty()) {
                        System.out.println("Сначала создайте хотя бы один гараж!");
                        break;
                    }

                    System.out.println("Выберите гараж (0 - " + (fleet.getGarages().size() - 1) + "): ");
                    int garageIndex = scanner.nextInt();
                    scanner.nextLine();

                    if (garageIndex < 0 || garageIndex >= fleet.getGarages().size()) {
                        System.out.println("Неверный номер гаража!");
                        break;
                    }

                    Garage selectedGarage = fleet.getGarages().get(garageIndex);

                    System.out.println("Выберите тип транспорта: 1 - Автомобиль, 2 - Мотоцикл");
                    int type = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Введите марку: ");
                    String brand = scanner.nextLine();

                    System.out.print("Введите модель: ");
                    String model = scanner.nextLine();

                    System.out.print("Введите год выпуска: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    if (type == 1) {
                        System.out.print("Введите количество дверей: ");
                        int doors = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Введите тип трансмиссии: ");
                        String transmission = scanner.nextLine();

                        Car car = new Car(brand, model, year, doors, transmission);
                        selectedGarage.addVehicle(car);
                        System.out.println("Автомобиль добавлен в гараж!");
                    } else if (type == 2) {
                        System.out.print("Введите тип кузова: ");
                        String bodyType = scanner.nextLine();

                        System.out.print("Есть ли бокс (true/false): ");
                        boolean hasBox = scanner.nextBoolean();

                        Moto moto = new Moto(brand, model, year, bodyType, hasBox);
                        selectedGarage.addVehicle(moto);
                        System.out.println("Мотоцикл добавлен в гараж!");
                    } else {
                        System.out.println("Неверный тип транспорта!");
                    }
                    break;

                case 3:
                    fleet.showFleetInfo();
                    break;

                case 4:
                    System.out.print("Введите марку для поиска: ");
                    String searchBrand = scanner.nextLine();
                    fleet.findVehicleByBrand(searchBrand);
                    break;

                case 5:
                    if (fleet.getGarages().isEmpty()) {
                        System.out.println("Нет гаражей для удаления!");
                        break;
                    }
                    System.out.println("Введите номер гаража для удаления (0 - " + (fleet.getGarages().size() - 1) + "): ");
                    int indexToRemove = scanner.nextInt();
                    scanner.nextLine();

                    if (indexToRemove >= 0 && indexToRemove < fleet.getGarages().size()) {
                        Garage garageToRemove = fleet.getGarages().get(indexToRemove);
                        fleet.removeGarage(garageToRemove);
                        System.out.println("Гараж удалён.");
                    } else {
                        System.out.println("Неверный номер гаража!");
                    }
                    break;

                case 0:
                    System.out.println("Завершение работы программы...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
}
