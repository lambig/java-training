package book;

public class Book {
    private final String title;
    private final String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String title() {
        return this.title;
    }

    public String author() {
        return this.author;
    }
}
