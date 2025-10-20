public class Worker extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public Worker(String name, int id, double hourlyRate, int hoursWorked) {
        super(name, id, "Worker");
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }
}
