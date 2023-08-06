package pickles.domain.core.entity.ume.states;

public interface ToBeMarinatedTo<E extends Marinated> {
    String id();

    E marinated();
}
