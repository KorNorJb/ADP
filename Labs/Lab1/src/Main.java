import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();

        employees.add(new Worker("Ашба Рой", 1, 500, 160));
        employees.add(new Worker("Говоров Ариф", 2, 600, 150));
        employees.add(new Manager("Игнатовский Дмитрий", 3, 100000, 20000));
        employees.add(new Manager("Халепа Максим", 4, 95000, 10000));

        System.out.println("📋 Список сотрудников и их зарплаты:\n");
        for (Employee e : employees) {
            e.showDetails();
        }
    }
}