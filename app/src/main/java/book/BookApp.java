package book;

import java.util.ArrayList;
import java.util.List;

public class BookApp {
    /**
     * 
     * @param books
     * @param toBeAdded
     * @return
     */
    public List<Book> シチュエーション1_本のリストに重複なく本を足したい(List<Book> books, Book... toBeAdded) {
        return BookCatalog.of(books).with(List.of(toBeAdded)).books();
    }

    /**
     * この世界ではタイトルと著者名が同じ本は同じ本とみなす
     * @param book1
     * @param book2
     * @return 同じであればtrue
     */
    private boolean sameBook(Book book1, Book book2) {
        return book1.title() == book2.title() && book1.author() == book2.author();
    }
}
