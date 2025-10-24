import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Выбери паттерн:");
        System.out.println("1 - Команда");
        System.out.println("2 - Шаблонный метод");
        System.out.println("3 - Посредник");
        String choice = in.nextLine();

        switch (choice) {
            case "1":
                DemoCommand();
                break;
            case "2":
                DemoTemplateMethod();
                break;
            case "3":
                DemoMediator();
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }

    //КОМАНДА

    public interface ICommand {
        void Execute();
        void Undo();
    }

    // ---------- Устройства ----------
    public static class Light {
        public void On()  { System.out.println("Свет включен."); }
        public void Off() { System.out.println("Свет выключен."); }
    }

    public static class Television {
        public void On()  { System.out.println("Телевизор включен."); }
        public void Off() { System.out.println("Телевизор выключен."); }
    }

    public static class AirConditioner {
        private String mode = "cool";
        public void On()   { System.out.println("Кондиционер включен. Режим: " + mode); }
        public void Off()  { System.out.println("Кондиционер выключен."); }
        public void SetEcoMode() { mode = "eco"; System.out.println("Кондиционер: режим ЭКО."); }
        public void SetCoolMode() { mode = "cool"; System.out.println("Кондиционер: режим охлаждение."); }
    }

    public static class LightOnCommand implements ICommand {
        private final Light _light;
        public LightOnCommand(Light light) { _light = light; }
        public void Execute() { _light.On(); }
        public void Undo()    { _light.Off(); }
    }

    public static class LightOffCommand implements ICommand {
        private final Light _light;
        public LightOffCommand(Light light) { _light = light; }
        public void Execute() { _light.Off(); }
        public void Undo()    { _light.On(); }
    }

    public static class TelevisionOnCommand implements ICommand {
        private final Television _tv;
        public TelevisionOnCommand(Television tv) { _tv = tv; }
        public void Execute() { _tv.On(); }
        public void Undo()    { _tv.Off(); }
    }

    public static class TelevisionOffCommand implements ICommand {
        private final Television _tv;
        public TelevisionOffCommand(Television tv) { _tv = tv; }
        public void Execute() { _tv.Off(); }
        public void Undo()    { _tv.On(); }
    }

    public static class AirConditionerOnCommand implements ICommand {
        private final AirConditioner _ac;
        public AirConditionerOnCommand(AirConditioner ac) { _ac = ac; }
        public void Execute() { _ac.On(); }
        public void Undo()    { _ac.Off(); }
    }

    public static class AirConditionerOffCommand implements ICommand {
        private final AirConditioner _ac;
        public AirConditionerOffCommand(AirConditioner ac) { _ac = ac; }
        public void Execute() { _ac.Off(); }
        public void Undo()    { _ac.On(); }
    }

    public static class AirConditionerEcoModeCommand implements ICommand {
        private final AirConditioner _ac;
        private String prevMode = "cool";
        public AirConditionerEcoModeCommand(AirConditioner ac) { _ac = ac; }
        public void Execute() {
            prevMode = "cool";
            _ac.SetEcoMode();
        }
        public void Undo() {
            if ("cool".equals(prevMode)) _ac.SetCoolMode();
        }
    }

    public static class MacroCommand implements ICommand {
        private final List<ICommand> _commands;
        public MacroCommand(List<ICommand> commands) { _commands = commands; }
        public void Execute() { for (ICommand c : _commands) c.Execute(); }
        public void Undo()    { for (int i = _commands.size()-1; i >= 0; i--) _commands.get(i).Undo(); }
    }

    public static class RemoteControl {
        private ICommand _onCommand;
        private ICommand _offCommand;
        private final Deque<ICommand> _history = new ArrayDeque<>();

        public void SetCommands(ICommand onCommand, ICommand offCommand) {
            _onCommand = onCommand;
            _offCommand = offCommand;
        }

        public void PressOnButton() {
            if (_onCommand == null) {
                System.out.println("Ошибка");
                return;
            }
            _onCommand.Execute();
            _history.push(_onCommand);
        }

        public void PressOffButton() {
            if (_offCommand == null) {
                System.out.println("Ошибка");
                return;
            }
            _offCommand.Execute();
            _history.push(_offCommand);
        }

        public void PressUndoButton() {
            if (_history.isEmpty()) {
                System.out.println("Отменять нечего.");
                return;
            }
            ICommand last = _history.pop();
            last.Undo();
        }
    }
    private static void DemoCommand() {
        Light livingRoomLight = new Light();
        Television tv = new Television();
        AirConditioner ac = new AirConditioner();

        ICommand lightOn = new LightOnCommand(livingRoomLight);
        ICommand lightOff = new LightOffCommand(livingRoomLight);

        ICommand tvOn = new TelevisionOnCommand(tv);
        ICommand tvOff = new TelevisionOffCommand(tv);

        ICommand acOn = new AirConditionerOnCommand(ac);
        ICommand acOff = new AirConditionerOffCommand(ac);
        ICommand acEco = new AirConditionerEcoModeCommand(ac);

        ICommand eveningMacro = new MacroCommand(Arrays.asList(lightOn, tvOn, acOn, acEco));

        RemoteControl remote = new RemoteControl();

        System.out.println("Управление светом:");
        remote.SetCommands(lightOn, lightOff);
        remote.PressOnButton();
        remote.PressOffButton();
        remote.PressUndoButton();

        System.out.println("\nУправление телевизором:");
        remote.SetCommands(tvOn, tvOff);
        remote.PressOnButton();
        remote.PressOffButton();

        System.out.println("\nУправление кондиционером:");
        remote.SetCommands(acOn, acOff);
        remote.PressOnButton();
        // смена режима как отдельная команда
        acEco.Execute();
        remote.PressOffButton();
        remote.PressUndoButton();

        System.out.println("\nМакрокоманда ВЕЧЕР:");
        eveningMacro.Execute();
        eveningMacro.Undo();

        System.out.println("\nКнопка Undo без истории:");
        new RemoteControl().PressUndoButton();
    }

    // ШАБЛОННЫЙ МЕТОД

    public static abstract class Beverage {
        // Шаблонный метод
        public final void PrepareRecipe() {
            BoilWater();
            Brew();
            PourInCup();
            if (WantCondiments()) {
                AddCondiments();
            } else {
                System.out.println("Топпингов не будет");
            }
        }

        private void BoilWater() { System.out.println("Кипячение"); }
        private void PourInCup() { System.out.println("Наливание в чашку"); }

// Абстрактные шаги
protected abstract void Brew();
        protected abstract void AddCondiments();

        // Хук
        protected boolean WantCondiments() { return true; }
    }

    public static class Tea extends Beverage {
        protected void Brew() { System.out.println("Заваривание чая"); }
        protected void AddCondiments() { System.out.println("Добавление лимона"); }
    }

    public static class Coffee extends Beverage {
        private final boolean withSugarAndMilk;
        public Coffee() { this(true); }
        public Coffee(boolean withSugarAndMilk) { this.withSugarAndMilk = withSugarAndMilk; }
        protected void Brew() { System.out.println("Заваривание кофе"); }
        protected void AddCondiments() { System.out.println("Добавление сахара и молока"); }
        protected boolean WantCondiments() { return withSugarAndMilk; }
    }

    public static class HotChocolate extends Beverage {
        protected void Brew() { System.out.println("Растапливание какао"); }
        protected void AddCondiments() { System.out.println("Добавление взбитых сливок"); }
    }

    private static void DemoTemplateMethod() {
        System.out.println("Приготовление чая:");
        Beverage tea = new Tea();
        tea.PrepareRecipe();

        System.out.println();
        System.out.println("Приготовление кофе (без сахара/молока):");
        Beverage coffeeNoSugar = new Coffee(false);
        coffeeNoSugar.PrepareRecipe();

        System.out.println();
        System.out.println("Приготовление горячего шоколада:");
        Beverage choco = new HotChocolate();
        choco.PrepareRecipe();
    }

    // ====================== ПАТТЕРН ПОСРЕДНИК ======================

    public interface IMediator {
        void SendMessage(String message, Colleague colleague);
        void SendPrivate(String message, Colleague from, Colleague to);
        void RegisterColleague(Colleague colleague);
        void UnregisterColleague(Colleague colleague);
    }

    public static abstract class Colleague {
        protected IMediator _mediator;
        public Colleague(IMediator mediator) { _mediator = mediator; }
        public abstract void ReceiveMessage(String message);
    }

    public static class ChatMediator implements IMediator {
        private final List<Colleague> _colleagues = new ArrayList<>();

        public void RegisterColleague(Colleague colleague) {
            if (!_colleagues.contains(colleague)) {
                _colleagues.add(colleague);
            }
        }

        public void UnregisterColleague(Colleague colleague) {
            _colleagues.remove(colleague);
        }

        public void SendMessage(String message, Colleague sender) {
            if (!_colleagues.contains(sender)) {
                System.out.println("Ошибка");
                return;
            }
            for (Colleague c : _colleagues) {
                if (c != sender) c.ReceiveMessage(message);
            }
        }

        public void SendPrivate(String message, Colleague from, Colleague to) {
            if (!_colleagues.contains(from)) {
                System.out.println("Ошибка");
                return;
            }
            if (!_colleagues.contains(to)) {
                System.out.println("Ошибка");
                return;
            }
            to.ReceiveMessage("(ЛС) " + message);
        }
    }

    public static class User extends Colleague {
        private final String _name;
        public User(IMediator mediator, String name) {
            super(mediator);
            _name = name;
        }

        public void Send(String message) {
            System.out.println(_name + " отправляет сообщение: " + message);
            _mediator.SendMessage(message, this);
        }

        public void SendPrivate(String message, User to) {
            System.out.println(_name + " отправляет ЛС для " + to._name + ": " + message);
            _mediator.SendPrivate(message, this, to);
        }

        public void ReceiveMessage(String message) {
            System.out.println(_name + " получил сообщение: " + message);
        }

        @Override
        public String toString() { return _name; }
    }

    private static void DemoMediator() {
        ChatMediator chatMediator = new ChatMediator();

        User user1 = new User(chatMediator, "Дима");
        User user2 = new User(chatMediator, "Рой");
        User user3 = new User(chatMediator, "Максим");

        // Регистрация
        chatMediator.RegisterColleague(user1);
        chatMediator.RegisterColleague(user2);
        chatMediator.RegisterColleague(user3);

        user1.Send("Привет всем!");
        user2.Send("Привет, Дима!");
        user3.Send("Всем привет!");

        System.out.println("\nЛичные сообщения:");
        user1.SendPrivate("Рой, давай после созвона.", user2);

        System.out.println("\nПроверка ошибок (отправитель не зарегистрирован):");
        User rogue = new User(chatMediator, "Ариф");
        rogue.Send("Я не в списке, но я попробую...");

        System.out.println("\nОтключаем Максима и пишем ему ЛС:");
        chatMediator.UnregisterColleague(user3);
        user2.SendPrivate("Максим, ты где?", user3);
    }
}


