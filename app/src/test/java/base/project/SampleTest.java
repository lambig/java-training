package base.project;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class SampleTest {

    @Nested
    class なんかテスト {
        @Test
        void test() {
            // Setup
            var sut = new Sample(1);

            // Exercise
            var actual = sut.get();

            // Verify
            assertThat(actual).isEqualTo(1);
        }
    }
}
