package broker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RunnerBrokerImpl implements RunnerBroker {
    private final Map<String, List<Runnable>> callbacks = new HashMap<>();

    @Override
    public RunnerBrokerImpl subscribe(String eventName, Runnable callback) {
        var events = Stream.concat(
                this.callbacks.getOrDefault(eventName, List.of()).stream(),
                Stream.of(callback))
                .toList();
        this.callbacks.put(eventName, events);
        return this;
    }

    @Override
    public RunnerBrokerImpl publish(String eventName) {
        this.callbacks.getOrDefault(eventName, List.of()).forEach(Runnable::run);
        return this;
    }

}
