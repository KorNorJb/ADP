import java.util.*;

// ==================== ВХОД В ПРОГРАММУ ====================
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Выберите задачу:");
        System.out.println("1 - Команда ");
        System.out.println("2 - Шаблонный метод ");
        System.out.println("3 - Посредник ");
        String choice = in.nextLine();

        switch (choice) {
            case "1": SmartHomeDemo.run(); break;
            case "2": ReportDemo.run(); break;
            case "3": ChatDemo.run(); break;
            default: System.out.println("Неверный выбор.");
        }
    }
}

// КОМАНДА
interface ICommand {
    void execute();
    void undo();
}

// Устройства
class Light {
    public void on() { System.out.println("Свет включен."); }
    public void off() { System.out.println("Свет выключен."); }
}
class AirConditioner {
    public void on() { System.out.println("Кондиционер включен."); }
    public void off() { System.out.println("Кондиционер выключен."); }
}
class TV {
    public void on() { System.out.println("Телевизор включен."); }
    public void off() { System.out.println("Телевизор выключен."); }
}

// Команды
class LightOnCommand implements ICommand {
    private final Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}
class LightOffCommand implements ICommand {
    private final Light light;
    public LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.off(); }
    public void undo() { light.on(); }
}
class ACOnCommand implements ICommand {
    private final AirConditioner ac;
    public ACOnCommand(AirConditioner ac) { this.ac = ac; }
    public void execute() { ac.on(); }
    public void undo() { ac.off(); }
}
class ACOffCommand implements ICommand {
    private final AirConditioner ac;
    public ACOffCommand(AirConditioner ac) { this.ac = ac; }
    public void execute() { ac.off(); }
    public void undo() { ac.on(); }
}
class TVOnCommand implements ICommand {
    private final TV tv;
    public TVOnCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.on(); }
    public void undo() { tv.off(); }
}
class TVOffCommand implements ICommand {
    private final TV tv;
    public TVOffCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.off(); }
    public void undo() { tv.on(); }
}

class MacroCommand implements ICommand {
    private final List<ICommand> commands;
    public MacroCommand(List<ICommand> commands) { this.commands = commands; }
    public void execute() { for (ICommand c : commands) c.execute(); }
    public void undo() { for (int i = commands.size() - 1; i >= 0; i--) commands.get(i).undo(); }
}

// Пульт управления
class RemoteControl {
    private final Map<Integer, ICommand> slots = new HashMap<>();
    private final Deque<ICommand> history = new ArrayDeque<>();
    private final Deque<ICommand> undone = new ArrayDeque<>();

    public void setCommand(int slot, ICommand command) { slots.put(slot, command); }

    public void pressButton(int slot) {
        ICommand cmd = slots.get(slot);
        if (cmd == null) {
            System.out.println("Кнопка не назначена.");
            return;
        }
        cmd.execute();
        history.push(cmd);
        undone.clear();
    }

    public void undo() {
        if (history.isEmpty()) { System.out.println("Нечего отменять."); return; }
        ICommand cmd = history.pop();
        cmd.undo();
        undone.push(cmd);
    }

    public void redo() {
        if (undone.isEmpty()) { System.out.println("Нечего повторять."); return; }
        ICommand cmd = undone.pop();
        cmd.execute();
        history.push(cmd);
    }
}

class SmartHomeDemo {
    public static void run() {
        System.out.println("=== Smart Home ===");
        Light light = new Light();
        AirConditioner ac = new AirConditioner();
        TV tv = new TV();

        RemoteControl remote = new RemoteControl();
        remote.setCommand(1, new LightOnCommand(light));
        remote.setCommand(2, new LightOffCommand(light));
        remote.setCommand(3, new ACOnCommand(ac));
        remote.setCommand(4, new ACOffCommand(ac));
        remote.setCommand(5, new TVOnCommand(tv));
        remote.setCommand(6, new TVOffCommand(tv));

        remote.pressButton(1);
        remote.pressButton(3);
        remote.pressButton(5);
        remote.undo();
        remote.redo();

        System.out.println("Макрокоманда вечер");
        ICommand macro = new MacroCommand(Arrays.asList(new LightOnCommand(light), new TVOnCommand(tv), new ACOnCommand(ac)));
        macro.execute();
        macro.undo();
    }
}

//ШАБЛОННЫЙ МЕТОД
abstract class ReportGenerator {
    public final void generateReport() {
        gatherData();
        formatData();
        createHeader();
        createBody();
        createFooter();
        if (customerWantsSave()) saveToFile();
    }

    protected abstract void gatherData();
    protected abstract void formatData();
    protected abstract void createHeader();
    protected abstract void createBody();
    protected abstract void createFooter();
    protected boolean customerWantsSave() { return true; }
    protected void saveToFile() { System.out.println("Отчет сохранен."); }
}

class PdfReport extends ReportGenerator {
    protected void gatherData() { System.out.println("PDF: Сбор данных."); }
    protected void formatData() { System.out.println("PDF: Форматирование данных."); }
    protected void createHeader() { System.out.println("PDF: Заголовок."); }
    protected void createBody() { System.out.println("PDF: Основная часть."); }
    protected void createFooter() { System.out.println("PDF: Подвал."); }
}

class ExcelReport extends ReportGenerator {
    protected void gatherData() { System.out.println("Excel: Сбор данных."); }
    protected void formatData() { System.out.println("Excel: Форматирование таблиц."); }
    protected void createHeader() { System.out.println("Excel: Заголовок."); }
    protected void createBody() { System.out.println("Excel: Основная часть."); }
    protected void createFooter() { System.out.println("Excel: Подвал."); }
    protected void saveToFile() { System.out.println("Excel: Сохранение в .xlsx."); }
}

class HtmlReport extends ReportGenerator {
    protected void gatherData() { System.out.println("HTML: Сбор данных."); }
    protected void formatData() { System.out.println("HTML: Форматирование HTML."); }
    protected void createHeader() { System.out.println("HTML: Заголовок."); }
    protected void createBody() { System.out.println("HTML: Основная часть."); }
    protected void createFooter() { System.out.println("HTML: Подвал."); }
}

class ReportDemo {
    public static void run() {
        System.out.println("=== Template Method: Reports ===");
        ReportGenerator pdf = new PdfReport();
        ReportGenerator excel = new ExcelReport();
        ReportGenerator html = new HtmlReport();
        pdf.generateReport();
        excel.generateReport();
        html.generateReport();
    }
}

// ПОСРЕДНИК
interface IMediator {
    void sendMessage(String message, User sender, String channel);
    void addUser(User user, String channel);
}

class ChatMediator implements IMediator {
    private final Map<String, List<User>> channels = new HashMap<>();

    public void sendMessage(String message, User sender, String channel) {
        List<User> users = channels.get(channel);
        if (users == null || !users.contains(sender)) {
            System.out.println(sender.getName() + " не состоит в канале " + channel);
            return;
        }
        for (User u : users) {
            if (u != sender) u.receive(message, sender.getName(), channel);
        }
    }

    public void addUser(User user, String channel) {
        channels.computeIfAbsent(channel, k -> new ArrayList<>()).add(user);
        user.setMediator(this);
        System.out.println(user.getName() + " присоединился к каналу " + channel);
    }
}

class User {
    private IMediator mediator;
    private final String name;

    public User(String name) { this.name = name; }
    public void setMediator(IMediator mediator) { this.mediator = mediator; }
    public String getName() { return name; }

    public void send(String message, String channel) {
        if (mediator != null) mediator.sendMessage(message, this, channel);
    }

    public void receive(String message, String from, String channel) {
        System.out.println(name + " получил в " + channel + " от " + from + ": " + message);
    }
}

class ChatDemo {
    public static void run() {
        System.out.println("=== Mediator Chat ===");
        ChatMediator mediator = new ChatMediator();

        User dima = new User("Дима");
        User roy = new User("Рой");
        User arif = new User("Ариф");

        mediator.addUser(dima, "general");
        mediator.addUser(roy, "general");
        mediator.addUser(arif, "tech");

        dima.send("Привет всем!", "general");
        roy.send("Есть кто в тех-канале?", "tech");
        arif.send("Проверка доступа.", "general");
        dima.send("Отправляю не туда", "tech"); // ошибка
    }
}

