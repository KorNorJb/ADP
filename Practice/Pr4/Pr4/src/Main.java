import java.util.Scanner;

// -------------------- ВХОД В ПРОГРАММУ --------------------
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Выберите тип документа:");
        System.out.println("1 - Report");
        System.out.println("2 - Resume");
        System.out.println("3 - Letter");
        System.out.println("4 - Invoice");
        String choice = in.nextLine();

        DocumentCreator creator = null;
        switch (choice) {
            case "1":
                creator = new ReportCreator();
                break;
            case "2":
                creator = new ResumeCreator();
                break;
            case "3":
                creator = new LetterCreator();
                break;
            case "4":
                creator = new InvoiceCreator();
                break;
            default:
                System.out.println("Неизвестный тип.");
                return;
        }

        Document doc = creator.CreateDocument();
        doc.Open();
    }
}

interface Document {
    void Open();
}

class Report implements Document {
    public void Open() {
        System.out.println("Открыт документ: Report.");
    }
}

class Resume implements Document {
    public void Open() {
        System.out.println("Открыт документ: Resume.");
    }
}

class Letter implements Document {
    public void Open() {
        System.out.println("Открыт документ: Letter.");
    }
}

class Invoice implements Document {
    public void Open() {
        System.out.println("Открыт документ: Invoice.");
    }
}

abstract class DocumentCreator {
    public abstract Document CreateDocument();
}

class ReportCreator extends DocumentCreator {
    public Document CreateDocument() {
        return new Report();
    }
}

class ResumeCreator extends DocumentCreator {
    public Document CreateDocument() {
        return new Resume();
    }
}

class LetterCreator extends DocumentCreator {
    public Document CreateDocument() {
        return new Letter();
    }
}

// Фабрика для нового типа
class InvoiceCreator extends DocumentCreator {
    public Document CreateDocument() {
        return new Invoice();
    }
}
