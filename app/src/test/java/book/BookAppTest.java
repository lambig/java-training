package book;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class BookAppTest {
    @Nested
    class シチュエーション1のテスト {
        @Test
        public void 追加する本が空の場合_入力したリストをそのまま返すこと() {
            // SetUp
            var sut = new BookApp();
            var books = List.of(new Book("タイトル1", "著者1"), new Book("タイトル2", "著者2"));
            // Exercise
            var actual = sut.シチュエーション1_本のリストに重複なく本を足したい(books);
            // Test
            assertThat(actual)
                    .anyMatch(book -> book.title() == "タイトル1" && book.author() == "著者1")
                    .anyMatch(book -> book.title() == "タイトル2" && book.author() == "著者2")
                    .hasSize(2);
        }

        @Test
        public void 追加する本と初期状態の本に重複がない場合_入力したリストに追加する本をそのまま追加すること() {
            // SetUp
            var sut = new BookApp();
            var books = List.of(new Book("タイトル1", "著者1"), new Book("タイトル2", "著者2"));
            // Exercise
            var actual = sut.シチュエーション1_本のリストに重複なく本を足したい(books, new Book("タイトル3", "著者2"));
            // Test
            System.out.println(actual);
            assertThat(actual)
                    .anyMatch(book -> book.title() == "タイトル1" && book.author() == "著者1")
                    .anyMatch(book -> book.title() == "タイトル2" && book.author() == "著者2")
                    .anyMatch(book -> book.title() == "タイトル3" && book.author() == "著者2")
                    .hasSize(3);
        }

        @Test
        public void 追加する本と初期状態の本に重複がある場合_入力したリストに追加する本を重複を省いて追加すること() {
            // SetUp
            var sut = new BookApp();
            var books = List.of(new Book("タイトル1", "著者1"), new Book("タイトル2", "著者2"));
            // Exercise
            var actual = sut.シチュエーション1_本のリストに重複なく本を足したい(books, new Book("タイトル2", "著者2"), new Book("タイトル3", "著者3"));
            // Test
            System.out.println(actual);
            assertThat(actual)
                    .anyMatch(book -> book.title() == "タイトル1" && book.author() == "著者1")
                    .anyMatch(book -> book.title() == "タイトル2" && book.author() == "著者2")
                    .anyMatch(book -> book.title() == "タイトル3" && book.author() == "著者3")
                    .hasSize(3);
        }

        @Test
        public void 重複を省いた結果何も残らなかった場合_入力したリストをそのまま出力すること() {
            // SetUp
            var sut = new BookApp();
            var books = List.of(new Book("タイトル1", "著者1"), new Book("タイトル2", "著者2"));
            // Exercise
            var actual = sut.シチュエーション1_本のリストに重複なく本を足したい(books, new Book("タイトル2", "著者2"), new Book("タイトル1", "著者1"));
            // Test
            System.out.println(actual);
            assertThat(actual)
                    .anyMatch(book -> book.title() == "タイトル1" && book.author() == "著者1")
                    .anyMatch(book -> book.title() == "タイトル2" && book.author() == "著者2")
                    .hasSize(2);
        }

        @Test
        public void 追加する本の中で重複がある場合_入力したリストに追加する本を重複を省いて追加すること() {
            // SetUp
            var sut = new BookApp();
            var books = List.of(new Book("タイトル1", "著者1"), new Book("タイトル2", "著者2"));
            // Exercise
            var actual = sut.シチュエーション1_本のリストに重複なく本を足したい(books, new Book("タイトル3", "著者3"), new Book("タイトル3", "著者3"));
            // Test
            System.out.println(actual);
            assertThat(actual)
                    .anyMatch(book -> book.title() == "タイトル1" && book.author() == "著者1")
                    .anyMatch(book -> book.title() == "タイトル2" && book.author() == "著者2")
                    .anyMatch(book -> book.title() == "タイトル3" && book.author() == "著者3")
                    .hasSize(3);
        }
    }
}
