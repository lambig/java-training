package pickles.domain;

import java.util.Objects;

public interface Entity<E extends Entity<? extends E>> extends DomainObject<E> {
    String id();

    default boolean equivalentTo(E another) {
        return Objects.equals(this.id(), another.id());
    }
}
