import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserManager manager = new UserManager();

        manager.AddUser(new User("Dima", "najomob@gmail.com", "admin"));
        manager.AddUser(new User("Arif", "Arif2005@gmail.com", "user"));
        manager.AddUser(new User("Krisanto", "Kris2004@gmail.com", "editor"));

        System.out.println("После добавления:");
        printUsers(manager);


        boolean updated = manager.UpdateUser("Arif2005@gmail.com", "Arifff", "power-user");
        System.out.println("Обновление Arif: " + (updated ? "успех" : "не найден"));
        System.out.println("После обновления:");
        printUsers(manager);


        boolean removed = manager.RemoveUser("Kris2004@gmail.com");
        System.out.println("Удаление Krisanto: " + (removed ? "успех" : "не найден"));
        System.out.println("После удаления:");
        printUsers(manager);

        boolean updatedMissing = manager.UpdateUser("who@example.com", "Who", "none");
        System.out.println("Обновление несуществующего: " + (updatedMissing ? "успех" : "не найден"));
    }

    private static void printUsers(UserManager manager) {
        List<User> list = manager.getAll();
        if (list.isEmpty()) {
            System.out.println("(пусто)");
            return;
        }
        for (User u : list) {
            System.out.println(u.getName() + " | " + u.getEmail() + " | " + u.getRole());
        }
    }
}

class User {
    private String name;
    private String email;
    private String role;

    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
}

class UserManager {
    private final List<User> users = new ArrayList<>();


    private int indexOfEmail(String email) {
        for (int i = 0; i < users.size(); i++) {
            String e = users.get(i).getEmail();
            if (e != null && e.equals(email)) return i;
        }
        return -1;
    }


    public void AddUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }


    public boolean RemoveUser(String email) {
        int idx = indexOfEmail(email);
        if (idx >= 0) {
            users.remove(idx);
            return true;
        }
        return false;
    }

    public boolean UpdateUser(String email, String newName, String newRole) {
        int idx = indexOfEmail(email);
        if (idx >= 0) {
            User u = users.get(idx);
            u.setName(newName);
            u.setRole(newRole);
            return true;
        }
        return false;
    }


    public List<User> getAll() {
        return users;
    }
}
