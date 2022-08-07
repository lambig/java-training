package base.project;

import java.util.function.Supplier;

public class Sample implements Supplier<Integer> {
    private final int value;
    public Sample(int value) {
        this.value = value;
    }
    public Integer get(){
        return this.value;
    }
}