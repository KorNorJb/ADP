public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Book book1 = new Book("Война и мир", "Л. Толстой", "12345", 3);
        Book book2 = new Book("Преступление и наказание", "Ф. Достоевский", "67890", 2);

        library.addBook(book1);
        library.addBook(book2);

        // Регистрация читателей
        Reader reader1 = new Reader("Игнатовский Дмитрий", 1);
        Reader reader2 = new Reader("Говоров Ариф", 2);

        library.registerReader(reader1);
        library.registerReader(reader2);

        // Список книг и читателей
        System.out.println("📚 Список книг:");
        library.showBooks();

        System.out.println("\n👥 Список читателей:");
        library.showReaders();

        // Выдаём книгу
        System.out.println("\nВыдаём книгу Дмитрию...");
        if (library.borrowBook(1, "12345")) {
            System.out.println("Книга успешно выдана!");
        } else {
            System.out.println("Не удалось выдать книгу.");
        }

        // Возврат книги
        System.out.println("\nВозврат книги...");
        if (library.returnBook(1, "12345")) {
            System.out.println("Книга возвращена.");
        } else {
            System.out.println("Возврат не удался.");
        }

        // Список книг после операций
        System.out.println("\n📚 Список книг после операций:");
        library.showBooks();
    }
}
