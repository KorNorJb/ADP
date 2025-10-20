import java.util.ArrayList;
import java.util.List;

public class Fleet {
    private List<Garage> garages = new ArrayList<>();

    public void addGarage(Garage garage) {
        garages.add(garage);
        System.out.println("Добавлен гараж в автопарк.");
    }

    public void removeGarage(Garage garage) {
        garages.remove(garage);
        System.out.println("Гараж удалён из автопарка.");
    }

    public void showFleetInfo() {
        System.out.println("\n🏢 Автопарк:");
        for (Garage g : garages) {
            g.showGarageInfo();
        }
    }

    public void findVehicleByBrand(String brand) {
        System.out.println("\n🔍 Поиск транспорта по марке: " + brand);
        for (Garage g : garages) {
            for (Vehicle v : g.getVehicles()) {
                if (v.brand.equalsIgnoreCase(brand)) {
                    v.showDetails();
                }
            }
        }
    }
    public List<Garage> getGarages() {
        return garages;
    }

}
