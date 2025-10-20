public class Book {
    private String title;
    private String author;
    private String publisher;
    private int copies;

    public Book(String title, String author, String publisher, int copies) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.copies = copies;
    }

    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getPublisher() {return publisher;}
    public int getCopies() {return copies;}

    public void addCopies(int count) {this.copies += count;}

    public boolean removeCopies() {
        if(copies > 0) {
            copies--;
            return true;
        }
        return false;
    }

    public void returnCopies() {
        copies++;
    }

    @Override
    public String toString() {
        return title + " (" + author + "), ISBN: " + publisher + ", Экземпляров: " + copies;
    }
}
