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
        var result = new ArrayList<>(books);
        var actualAdditions = new ArrayList<Book>();
        for (Book candidate : toBeAdded) {
            var foundDuplicationInList = false;
            var foundDuplicationInAddition = false;
            for (Book alreadyInList : books) {
                if (sameBook(candidate, alreadyInList)) {
                    foundDuplicationInList = true;
                    break;
                }
            }
            if (!foundDuplicationInList) {
                for (Book actualAddition : actualAdditions) {
                    if (sameBook(candidate, actualAddition)) {
                        foundDuplicationInAddition = true;
                        break;
                    }
                }
                if (!foundDuplicationInAddition) {
                    actualAdditions.add(candidate);
                }
            }
            foundDuplicationInList = false;
            foundDuplicationInAddition = false;
        }
        result.addAll(actualAdditions);
        return result;
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
