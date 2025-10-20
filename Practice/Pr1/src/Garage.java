import java.util.ArrayList;
import java.util.List;

public class Garage {
    private List<Vehicle> vehicles = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        System.out.println("Добавлено транспортное средство: " + vehicle.brand);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        System.out.println("Удалено транспортное средство: " + vehicle.brand);
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void showGarageInfo() {
        System.out.println("\n🚗 Содержимое гаража:");
        for (Vehicle v : vehicles) {
            v.showDetails();
        }
    }
}