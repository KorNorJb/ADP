import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

enum LogLevel { INFO, WARNING, ERROR }

class Logger {
    private static Logger instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private static LogLevel currentLevel = LogLevel.INFO;
    private static String logFilePath = "logs.txt";

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null)
                    instance = new Logger();
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public void setLogLevel(LogLevel level) {
        currentLevel = level;
    }

    public void setLogFilePath(String path) {
        logFilePath = path;
    }

    public synchronized void log(String message, LogLevel level) {
        if (level.ordinal() >= currentLevel.ordinal()) {
            try (FileWriter fw = new FileWriter(logFilePath, true)) {
                fw.write("[" + level + "] " + message + "\n");
            } catch (IOException e) {
                System.out.println("Ошибка записи: " + e.getMessage());
            }
        }
    }

    public void readLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null)
                System.out.println(line);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать лог: " + e.getMessage());
        }
    }
}

class LoggerTest implements Runnable {
    private final String name;

    public LoggerTest(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        logger.log(name + " - INFO", LogLevel.INFO);
        logger.log(name + " - WARNING", LogLevel.WARNING);
        logger.log(name + " - ERROR", LogLevel.ERROR);
    }
}

class Computer {
    String CPU;
    String RAM;
    String Storage;
    String GPU;
    String OS;

    @Override
    public String toString() {
        return "Компьютер: CPU - " + CPU + ", RAM - " + RAM + ", Накопитель - " + Storage + ", GPU - " + GPU + ", ОС - " + OS;
    }
}

interface IComputerBuilder {
    void setCPU();
    void setRAM();
    void setStorage();
    void setGPU();
    void setOS();
    Computer getComputer();
}

class OfficeComputerBuilder implements IComputerBuilder {
    private Computer computer = new Computer();
    public void setCPU() { computer.CPU = "Intel core i7"; }
    public void setRAM() { computer.RAM = "16GB"; }
    public void setStorage() { computer.Storage = "1025gb HDD"; }
    public void setGPU() { computer.GPU = "RTX 4060"; }
    public void setOS() { computer.OS = "Windows 11"; }
    public Computer getComputer() { return computer; }
}

class GamingComputerBuilder implements IComputerBuilder {
    private Computer computer = new Computer();
    public void setCPU() { computer.CPU = "Intel i9"; }
    public void setRAM() { computer.RAM = "32GB"; }
    public void setStorage() { computer.Storage = "3TB SSD"; }
    public void setGPU() { computer.GPU = "NVIDIA RTX 5080"; }
    public void setOS() { computer.OS = "Windows 11"; }
    public Computer getComputer() { return computer; }
}

class ComputerDirector {
    private IComputerBuilder builder;
    public ComputerDirector(IComputerBuilder builder) {
        this.builder = builder;
    }
    public void constructComputer() {
        builder.setCPU();
        builder.setRAM();
        builder.setStorage();
        builder.setGPU();
        builder.setOS();
    }
    public Computer getComputer() {
        return builder.getComputer();
    }
}

interface IPrototype {
    IPrototype clone();
}

class Section implements IPrototype {
    private String title;
    private String text;

    public Section(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public void setText(String text) { this.text = text; }

    public String toString() {
        return "Раздел: " + title + " - " + text;
    }

    public IPrototype clone() {
        return new Section(title, text);
    }
}

class Image implements IPrototype {
    private String url;

    public Image(String url) {
        this.url = url;
    }

    public String toString() {
        return "Изображение: " + url;
    }

    public IPrototype clone() {
        return new Image(url);
    }
}

class Document implements IPrototype {
    private String title;
    private String content;
    private List<Section> sections = new ArrayList<>();
    private List<Image> images = new ArrayList<>();

    public Document(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public IPrototype clone() {
        Document clone = new Document(title, content);
        for (Section s : sections)
            clone.addSection((Section) s.clone());
        for (Image i : images)
            clone.addImage((Image) i.clone());
        return clone;
    }

    public String toString() {
        return "Документ: " + title + "\nСодержание: " + content + "\n" + sections + "\n" + images;
    }
}

class DocumentManager {
    public Document createDocument(IPrototype prototype) {
        return (Document) prototype.clone();
    }
}

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.setLogLevel(LogLevel.INFO);
        logger.setLogFilePath("app_log.txt");

        Thread t1 = new Thread(new LoggerTest("Поток 1"));
        Thread t2 = new Thread(new LoggerTest("Поток 2"));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.readLogs();

        IComputerBuilder officeBuilder = new OfficeComputerBuilder();
        ComputerDirector director = new ComputerDirector(officeBuilder);
        director.constructComputer();
        System.out.println("\n" + director.getComputer());

        IComputerBuilder gamingBuilder = new GamingComputerBuilder();
        director = new ComputerDirector(gamingBuilder);
        director.constructComputer();
        System.out.println(director.getComputer());

        Document doc1 = new Document("Отчет", "Основной документ");
        doc1.addSection(new Section("Введение", "Описание проекта"));
        doc1.addImage(new Image("diagram.png"));

        DocumentManager manager = new DocumentManager();
        Document doc2 = manager.createDocument(doc1);
        System.out.println("\nИсходный документ:\n" + doc1);
        System.out.println("\nКлон документа:\n" + doc2);
    }
}
