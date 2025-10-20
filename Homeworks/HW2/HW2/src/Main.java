// Пример реализации принципов DRY, KISS и YAGNI в одном файле
public class Main {
    public static void main(String[] args) {
        // DRY
        System.out.println("Принцип DRY");
        Logger logger = new Logger();
        logger.log("info", "Какая то информация");
        logger.log("warning", "Предупреждение");
        logger.log("error", "Ошибка!");

        DatabaseService db = new DatabaseService();
        db.connect();

        LoggingService logService = new LoggingService();
        logService.log("Тест");

        // KISS
        System.out.println("\nПринцип KISS ");
        KISSExamples kiss = new KISSExamples();
        kiss.processNumbers(new int[]{-1, 0, 5, 3});
        kiss.printPositiveNumbers(new int[]{1, -4, 7});
        System.out.println("Результат деления: " + kiss.divide(10, 0));

        // === YAGNI ===
        System.out.println("\nПринцип YAGNI ");
        User user = new User("Дмитрий", "najomob@gmail.com");
        user.saveToDatabase();

        FileReader reader = new FileReader();
        reader.readFile("data.txt");

        ReportGenerator report = new ReportGenerator();
        report.generatePdfReport();
    }
}

// DRY (Don't Repeat Yourself)
class Logger {
    public void log(String level, String message) {
        System.out.println(level.toUpperCase() + ": " + message);
    }
}

class AppConfig {
    public static final String CONNECTION_STRING =
            "Server=myServer;Database=myDb;User Id=myUser;Password=myPass;";
}

class DatabaseService {
    public void connect() {
        System.out.println("Подключаемся к БД: " + AppConfig.CONNECTION_STRING);
    }
}

class LoggingService {
    public void log(String message) {
        System.out.println("Записываем лог в БД (" + AppConfig.CONNECTION_STRING + "): " + message);
    }
}

// KISS (Keep It Simple, Stupid)
class KISSExamples {
    public void processNumbers(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return;
        }

        for (int n : numbers) {
            if (n > 0) {
                System.out.println(n);
            }
        }
    }

    public void printPositiveNumbers(int[] numbers) {
        for (int n : numbers) {
            if (n > 0) {
                System.out.println(n);
            }
        }
    }

    public int divide(int a, int b) {
        if (b == 0) {
            return 0;
        }
        return a / b;
    }
}

// 3. YAGNI (You Ain’t Gonna Need It)
class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void saveToDatabase() {
        System.out.println("Сохраняем пользователя " + name + " в базу данных");
    }
}

class FileReader {
    public String readFile(String filePath) {
        System.out.println("Чтение файла: " + filePath);
        return "file content";
    }
}

class ReportGenerator {
    public void generatePdfReport() {
        System.out.println("Генерация PDF отчета...");
    }
}
