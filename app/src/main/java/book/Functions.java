package book;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class Functions {
    private Functions() {

    }
    
    @SafeVarargs
    public static <E> List<E> concatenated(List<E>... lists) {
        return Stream.of(lists)
                .map(List::stream)
                .reduce(Stream.empty(), (soFar, current) -> Stream.concat(soFar, current))
                .toList();
    }

    public static Consumer<Object> DO_NOTHING = anything -> {
    };

}
