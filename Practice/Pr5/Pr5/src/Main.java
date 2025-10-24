import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        //Singleton
        System.out.println("Singleton");
        Logger logger = Logger.getInstance();
        logger.LoadConfig("config.json");
        logger.SetLogLevel(LogLevel.INFO);

        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.submit(() -> {
            logger.Log("Поток-1: инфо", LogLevel.INFO);
            logger.Log("Поток-1: предупреждение", LogLevel.WARNING);
        });
        pool.submit(() -> {
            logger.Log("Поток-2: ошибка", LogLevel.ERROR);
            logger.Log("Поток-2: инфо", LogLevel.INFO);
        });
        pool.submit(() -> {
            logger.Log("Поток-3: предупреждение", LogLevel.WARNING);
            logger.Log("Поток-3: ошибка", LogLevel.ERROR);
        });
        pool.shutdown();
        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}

        LogReader reader = new LogReader(logger.getFilePath());
        reader.ReadLogs(LogLevel.WARNING);

        //Builder
        System.out.println("\nBuilder");
        ReportDirector director = new ReportDirector();
        ReportStyle style = new ReportStyle("white", "black", 12);

        IReportBuilder textBuilder = new TextReportBuilder();
        director.ConstructReport(textBuilder, style);
        Report textReport = textBuilder.GetReport();
        textReport.Export();

        IReportBuilder htmlBuilder = new HtmlReportBuilder();
        director.ConstructReport(htmlBuilder, new ReportStyle("#fff", "#222", 14));
        Report htmlReport = htmlBuilder.GetReport();
        htmlReport.Export();

        IReportBuilder pdfBuilder = new PdfReportBuilder();
        director.ConstructReport(pdfBuilder, new ReportStyle("white", "black", 11));
        Report pdfReport = pdfBuilder.GetReport();
        pdfReport.Export();

        // Prototype
        System.out.println("\nPrototype");
        Weapon sword = new Weapon("Секира", 50);
        Armor armor = new Armor("Нагрудник", 40);
        List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill("Супер пупер удар"), new Skill("Лечение")));

        Character base = new Character("Воин", 100, 20, 15, 10, sword, armor, skills);
        Character clone = base.copy();
        clone.setName("Воин-клон");
        clone.getWeapon().setDamage(60);

        System.out.println("Оригинал: " + base);
        System.out.println("Клон:     " + clone);
    }
}

//SINGLETON
enum LogLevel { INFO, WARNING, ERROR }

class Logger {
    private static volatile Logger instance;
    private LogLevel currentLevel = LogLevel.INFO;
    private String filePath = "app.log";
    private final Object fileLock = new Object();

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) instance = new Logger();
            }
        }
        return instance;
    }

    public void SetLogLevel(LogLevel level) {
        if (level != null) currentLevel = level;
    }

    public void LoadConfig(String configPath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(configPath))).trim();
            Map<String, String> map = parseSimpleJson(json);
            if (map.containsKey("logLevel")) {
                try { currentLevel = LogLevel.valueOf(map.get("logLevel").toUpperCase()); } catch (Exception ignored) {}
            }
            if (map.containsKey("filePath")) {
                filePath = map.get("filePath");
            }
        } catch (IOException e) {
        }
    }

    public void Log(String message, LogLevel level) {
        if (level.ordinal() < currentLevel.ordinal()) return;
        String line = String.format("%s: %s%n", level, message);
        synchronized (fileLock) {
            try (FileWriter fw = new FileWriter(filePath, true)) {
                fw.write(line);
            } catch (IOException e) {
                System.out.println("Ошибка записи лога: " + e.getMessage());
            }
        }
    }

    public String getFilePath() { return filePath; }

    private static Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        Pattern p = Pattern.compile("\"(.*?)\"\\s*:\\s*\"(.*?)\"");
        Matcher m = p.matcher(json);
        while (m.find()) map.put(m.group(1), m.group(2));
        return map;
    }
}

class LogReader {
    private final String filePath;

    public LogReader(String filePath) {
        this.filePath = filePath;
    }

    public void ReadLogs(LogLevel minLevel) {
        System.out.println("Логи уровня " + minLevel + " и выше");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                for (LogLevel lvl : LogLevel.values()) {
                    if (line.startsWith(lvl.toString()) && lvl.ordinal() >= minLevel.ordinal()) {
                        System.out.println(line);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения логов: " + e.getMessage());
        }
    }
}

//BUILDER
class ReportStyle {
    String backgroundColor;
    String fontColor;
    int fontSize;

    public ReportStyle(String backgroundColor, String fontColor, int fontSize) {
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
    }
}

class Report {
    private String header;
    private String content;
    private String footer;
    private final List<Section> sections = new ArrayList<>();
    private ReportStyle style;
    private String format = "TEXT";

    public void setHeader(String header) { this.header = header; }
    public void setContent(String content) { this.content = content; }
    public void setFooter(String footer) { this.footer = footer; }
    public void addSection(String name, String sectionContent) { sections.add(new Section(name, sectionContent)); }
    public void setStyle(ReportStyle style) { this.style = style; }
    public void setFormat(String format) { this.format = format; }

    public void Export() {
        System.out.println("[Export " + format + "]");
        System.out.println("Header: " + header);
        for (Section s : sections) System.out.println("Section: " + s.name + " -> " + s.content);
        System.out.println("Content: " + content);
        System.out.println("Footer: " + footer);
        if (style != null) {
            System.out.println("Style: bg=" + style.backgroundColor + ", font=" + style.fontColor + ", size=" + style.fontSize);
        }
    }

    private static class Section {
        final String name;
        final String content;
        Section(String name, String content) { this.name = name; this.content = content; }
    }
}

interface IReportBuilder {
    void SetHeader(String header);
    void SetContent(String content);
    void SetFooter(String footer);
    void AddSection(String sectionName, String sectionContent);
    void SetStyle(ReportStyle style);
    Report GetReport();
}

class TextReportBuilder implements IReportBuilder {
    private final Report report = new Report();
    public void SetHeader(String header) { report.setHeader(header); }
    public void SetContent(String content) { report.setContent(content); }
    public void SetFooter(String footer) { report.setFooter(footer); }
    public void AddSection(String sectionName, String sectionContent) { report.addSection(sectionName, sectionContent); }
    public void SetStyle(ReportStyle style) { report.setStyle(style); }
    public Report GetReport() { report.setFormat("TEXT"); return report; }
}

class HtmlReportBuilder implements IReportBuilder {
    private final Report report = new Report();
    public void SetHeader(String header) { report.setHeader("<h1>" + header + "</h1>"); }
    public void SetContent(String content) { report.setContent("<p>" + content + "</p>"); }
    public void SetFooter(String footer) { report.setFooter("<footer>" + footer + "</footer>"); }
    public void AddSection(String sectionName, String sectionContent) {
        report.addSection(sectionName, "<section><h2>" + sectionName + "</h2><div>" + sectionContent + "</div></section>");
    }
    public void SetStyle(ReportStyle style) { report.setStyle(style); }
    public Report GetReport() { report.setFormat("HTML"); return report; }
}

class PdfReportBuilder implements IReportBuilder {
    // Без внешних библиотек делаем симуляцию PDF-экспорта
    private final Report report = new Report();
    public void SetHeader(String header) { report.setHeader("[PDF H1] " + header); }
    public void SetContent(String content) { report.setContent("[PDF P] " + content); }
    public void SetFooter(String footer) { report.setFooter("[PDF FOOTER] " + footer); }
    public void AddSection(String sectionName, String sectionContent) {
        report.addSection(sectionName, "[PDF SECTION] " + sectionName + " :: " + sectionContent);
    }
    public void SetStyle(ReportStyle style) { report.setStyle(style); }
    public Report GetReport() { report.setFormat("PDF"); return report; }
}

class ReportDirector {
    public void ConstructReport(IReportBuilder builder, ReportStyle style) {
        builder.SetHeader("Отчет по продажам");
        builder.AddSection("Итоги", "Рост на 12% кв/кв");
        builder.AddSection("Рынки", "APAC, EMEA, NA");
        builder.SetContent("Детальный анализ метрик выручки и маржинальности.");
        builder.SetFooter("© АнализДимы, 2025");
        builder.SetStyle(style);
    }
}

// PROTOTYPE
interface Prototype<T> {
    T copy();
}

class Skill implements Prototype<Skill> {
    private final String name;
    public Skill(String name) { this.name = name; }
    public Skill copy() { return new Skill(name); }
    public String toString() { return name; }
}

class Weapon implements Prototype<Weapon> {
    private String name;
    private int damage;
    public Weapon(String name, int damage) { this.name = name; this.damage = damage; }
    public Weapon copy() { return new Weapon(name, damage); }
    public void setDamage(int d) { this.damage = d; }
    public String toString() { return name + "(урон=" + damage + ")"; }
}

class Armor implements Prototype<Armor> {
    private final String name;
    private final int defense;
    public Armor(String name, int defense) { this.name = name; this.defense = defense; }
    public Armor copy() { return new Armor(name, defense); }
    public String toString() { return name + "(защита=" + defense + ")"; }
}

class Character implements Prototype<Character> {
    private String name;
    private int health;
    private int strength;
    private int agility;
    private int intellect;
    private Weapon weapon;
    private Armor armor;
    private List<Skill> skills;

    public Character(String name, int health, int strength, int agility, int intellect,
                     Weapon weapon, Armor armor, List<Skill> skills) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.agility = agility;
        this.intellect = intellect;
        this.weapon = weapon;
        this.armor = armor;
        this.skills = skills;
    }

    public Character copy() {
        List<Skill> clonedSkills = new ArrayList<>();
        for (Skill s : skills) clonedSkills.add(s.copy());
        return new Character(name, health, strength, agility, intellect,
                weapon.copy(), armor.copy(), clonedSkills);
    }

    public void setName(String name) { this.name = name; }
    public Weapon getWeapon() { return weapon; }

    public String toString() {
        return name + " [HP=" + health + ", STR=" + strength + ", AGI=" + agility + ", INT=" + intellect
                + ", " + weapon + ", " + armor + ", skills=" + skills + "]";
    }
}

