package testability;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class EditedTime implements Supplier<String> {

    public String get() {
        var date = LocalDateTime.now();
        return date.getHour() > 11 ? "午前" : "午後";
    }

}
