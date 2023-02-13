package broker;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
/**
 * gradleで叩きたければ
 * gradle run test --tests "RunnerBrokerTest"
 * でどうぞ
 */
public class RunnerBrokerTest {
    @Test
    void Runnableの登録と実行が可能であること() {
        // SetUp
        RunnerBroker sut = null; // 実装をここに。
        var callbackInvocationChecks = new ArrayList<String>();
        // Exercise
        sut
                .publish("event1") // まだ登録されていないので何もしない
                .subscribe("event1", () -> callbackInvocationChecks.add("callback1"))
                .subscribe("event2", () -> callbackInvocationChecks.add("callback2"))
                .subscribe("event2", () -> callbackInvocationChecks.add("callback3")) // 同じイベントを複数購読
                .subscribe("event3", () -> callbackInvocationChecks.add("callback4"))
                .publish("event1")
                .publish("event2")
                .publish("event3")
                .publish("event3") // 2回呼べば2回動いてほしい
                .publish("event4"); // 登録されていないので何もしない
        // Verify
        assertThat(callbackInvocationChecks)
                .containsExactlyInAnyOrder(
                        "callback1", // event1
                        "callback2", // event2の1つ目
                        "callback3", // event2の2つ目
                        "callback4", // 1回目のevent3
                        "callback4");// 1回目のevent3
    }
}
