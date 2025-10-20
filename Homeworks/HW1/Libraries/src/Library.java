import java.util.*;

public class Library {
    private List<Book> books = new ArrayList<>();
    private List<Reader> readers = new ArrayList<>();
    private Map<Integer, List<Book>> borrowedBooks = new HashMap<>();


    public void addBook(Book book) {
        books.add(book);
    }
    public void removeBook(String publisher) {
        books.removeIf(book -> book.getPublisher().equals(publisher));
    }

    public void registerReader(Reader reader) {
        readers.add(reader);
    }
    public void removeReader(int id) {
        readers.removeIf(reader -> reader.getId() == id);
        borrowedBooks.remove(id);
    }


    public boolean borrowBook(int readerId, String isbn) {
        Reader reader = findReader(readerId);
        Book book = findBook(isbn);

        if (reader != null && book != null && book.removeCopies()) {
            borrowedBooks.putIfAbsent(readerId, new ArrayList<>());
            borrowedBooks.get(readerId).add(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(int readerId, String isbn) {
        List<Book> borrowed = borrowedBooks.get(readerId);
        if (borrowed != null) {
            for (Book b : borrowed) {
                if (b.getPublisher().equals(isbn)) {
                    b.returnCopies();
                    borrowed.remove(b);
                    return true;
                }
            }
        }
        return false;
    }

    // === Вспомогательные методы ===
    private Reader findReader(int id) {
        for (Reader r : readers) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    private Book findBook(String isbn) {
        for (Book b : books) {
            if (b.getPublisher().equals(isbn)) return b;
        }
        return null;
    }

    public void showBooks() {
        for (Book b : books) {
            System.out.println(b);
        }
    }

    public void showReaders() {
        for (Reader r : readers) {
            System.out.println(r);
        }
    }
}