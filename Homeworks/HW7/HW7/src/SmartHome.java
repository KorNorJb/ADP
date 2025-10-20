import java.util.Stack;

public class SmartHome {
    public static void main(String[] args) {
        SmartHomeController controller = new SmartHomeController();

        Light light = new Light();
        Door door = new Door();
        Thermostat thermostat = new Thermostat();

        // создаём команды
        ICommand lightOn = new LightOnCommand(light);
        ICommand doorOpen = new DoorOpenCommand(door);
        ICommand tempUp = new IncreaseTemperatureCommand(thermostat);

        // выполняем
        controller.executeCommand(lightOn);
        controller.executeCommand(doorOpen);
        controller.executeCommand(tempUp);

        System.out.println("\n--- Отмена последних действий ---");
        controller.undoLastCommand();
        controller.undoLastCommand();
        controller.undoLastCommand();

        System.out.println("\n--- Попытка отмены без истории ---");
        controller.undoLastCommand();

    }
}

// Интерфейс
interface ICommand {
    void execute();
    void undo();
}

// Устройства умного дома
class Light{
    private boolean on = false;

    public void on() {
        on = true;
        System.out.println("Свет включен");
    }

    public void off() {
        on = false;
        System.out.println("Свет выключен");
    }
}
class Door{
    private boolean open = false;

    public void open() {
        open = true;
        System.out.println("Дверь открыта");
    }

    public void close() {
        open = false;
        System.out.println("Дверь закрыта");
    }
}
class Thermostat{
    private int temperature = 22;

    public void increase() {
        temperature++;
        System.out.println("Температура увеличена до " + temperature + "°C");
    }

    public void decrease() {
        temperature--;
        System.out.println("Температура уменьшена до " + temperature + "°C");
    }
}


class LightOnCommand implements ICommand {
    private final Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }
    public void execute() {
        light.on();
    }
    public void undo() {
        light.off();
    }
}

class LightOffCommand implements ICommand {
    private final Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.off();
    }

    public void undo() {
        light.on();
    }
}

class DoorOpenCommand implements ICommand {
    private final Door door;

    public DoorOpenCommand(Door door) {
        this.door = door;
    }

    public void execute() {
        door.open();
    }

    public void undo() {
        door.close();
    }
}

class DoorCloseCommand implements ICommand {
    private final Door door;

    public DoorCloseCommand(Door door) {
        this.door = door;
    }

    public void execute() {
        door.close();
    }

    public void undo() {
        door.open();
    }
}


class IncreaseTemperatureCommand implements ICommand {
    private final Thermostat thermostat;

    public IncreaseTemperatureCommand(Thermostat thermostat) {
        this.thermostat = thermostat;
    }

    public void execute() {
        thermostat.increase();
    }

    public void undo() {
        thermostat.decrease();
    }
}

class DecreaseTemperatureCommand implements ICommand {
    private final Thermostat thermostat;

    public DecreaseTemperatureCommand(Thermostat thermostat) {
        this.thermostat = thermostat;
    }

    public void execute() {
        thermostat.decrease();
    }

    public void undo() {
        thermostat.increase();
    }
}

//Invoker
class SmartHomeController{
    private final Stack<ICommand> history = new Stack<>();

    public void executeCommand(ICommand command){
        command.execute();
        history.push(command);
    }

    public void undoLastCommand(){
        if(history.isEmpty()){
            System.out.println("Нет команд для отмены!");
            return;
        }
        ICommand last = history.pop();
        last.undo();
    }
}