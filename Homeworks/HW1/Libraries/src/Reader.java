public class Reader {

    private String name;
    private int id;

    public Reader(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {return name;}
    public int getId() {return id;}

    @Override
    public String toString() {
        return "Читатель: " + name + " (ID: " + id + ")";
    }
}
