import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Главный класс
public class Main {
    public static void main(String[] args) {
        System.out.println("Порождающий паттерн: Одиночка (Singleton)\n");

        ConfigurationManager config1 = ConfigurationManager.getInstance();

        config1.loadFromFile("config.txt");

        config1.setSetting("AppName", "TransportSystem");
        config1.setSetting("Version", "1.0");
        config1.setSetting("Language", "RU");

        config1.saveToFile("config.txt");

        ConfigurationManager config2 = ConfigurationManager.getInstance();

        System.out.println("\nОдинаковый ли объект? " + (config1 == config2));

        System.out.println("\nТекущие настройки:");
        for (Map.Entry<String, String> entry : config2.getAllSettings().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        System.out.println("\nПроверка в многопоточном режиме");
        Runnable task = () -> {
            ConfigurationManager instance = ConfigurationManager.getInstance();
            System.out.println(Thread.currentThread().getName() + ": " + instance.hashCode());
        };

        Thread t1 = new Thread(task, "Поток-1");
        Thread t2 = new Thread(task, "Поток-2");
        Thread t3 = new Thread(task, "Поток-3");

        t1.start();
        t2.start();
        t3.start();
    }
}

// Класс ConfigurationManager — Singleton (Одиночка)
class ConfigurationManager {
    private static volatile ConfigurationManager instance;

    private final Map<String, String> settings;

    private ConfigurationManager() {
        settings = new HashMap<>();
        System.out.println("Создан экземпляр ConfigurationManager");
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) { // двойная проверка
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    // Методы для работы с настройками
    public void setSetting(String key, String value) {
        settings.put(key, value);
    }

    public String getSetting(String key) {
        return settings.getOrDefault(key, "Значение не найдено");
    }

    public Map<String, String> getAllSettings() {
        return settings;
    }
    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Настройки сохранены в файл: " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public void loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Файл настроек не найден, создаётся новый при сохранении.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    settings.put(parts[0], parts[1]);
                }
            }
            System.out.println("Настройки загружены из файла: " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
    }
}
