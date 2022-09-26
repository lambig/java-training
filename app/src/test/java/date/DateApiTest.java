package date;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
public class DateApiTest {
    @Nested
    class actualNowのテスト {
        @Test
        public void 出力確認() {
            var sut = new DateApi();
            var dateString = sut.actualCurrentDate();
            assertThat(dateString)
            .as("とりあえず出力させてみる")
            .isEqualTo("???");
        }
    }
}
