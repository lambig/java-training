package book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import static book.Functions.concatenated;
import static book.Functions.DO_NOTHING;

/**
 * 本のカタログを表すクラス。
 * 収録されている本には重複がないことが約束されている。
 * ただし、本の重複判定基準は本による。
 */
public class BookCatalog {

    private static final BiConsumer<List<Book>, Book> ACCEPT_IF_UNKNOWN //
            = (listed, book) -> listed
                    .stream()
                    .filter(book::isSameAs)
                    .findFirst()
                    .ifPresentOrElse(
                            DO_NOTHING,
                            () -> listed.add(book));

    private static final Collector<Book, ?, BookCatalog> TO_DISTINCT_CATALOG //
            = Collector.of(
                    ArrayList::new,
                    ACCEPT_IF_UNKNOWN,
                    Functions::concatenated,
                    BookCatalog::new);

    private final List<Book> books;

    /**
     * 本のリストからBookCatalogを生成する。
     * 
     * @param books Catalogに記録する本のリスト
     * @return BookCatalog
     */
    public static BookCatalog of(List<Book> books) {
        return books.stream().collect(TO_DISTINCT_CATALOG);
    }

    private BookCatalog(List<Book> books) {
        this.books = books;
    }

    /**
     * 現在収録されている本に加え、引数のリストの本も含んだ新しいBookCatalogを返却する。
     * 
     * @param books Catalogに加える本のリスト
     * @return 新しいBookCatalog
     */
    public BookCatalog with(List<Book> books) {
        return BookCatalog.of(concatenated(this.books, books));
    }

    /**
     * 収録されている本のリストを返却する。
     * 
     * @return 本のリスト
     */
    public List<Book> books() {
        return new ArrayList<>(this.books);
    }


    public BookCatalog byAuthor(String author){
        return BookCatalog.of(this.books.stream().filter(book->book.author() == author).toList());
    }
}