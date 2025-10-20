public abstract class Employee {
    protected String name;
    protected int id;
    protected String position;


    public Employee(String name, int id, String position) {
        this.name = name;
        this.id = id;
        this.position = position;
    }

    public abstract double calculateSalary();


    public void showDetails() {
        System.out.println("Name: " + name);
        System.out.println("ID: " + id);
        System.out.println("Position: " + position);
        System.out.println("Salary: " + calculateSalary());
        System.out.println("---------------------------");
    }
}
