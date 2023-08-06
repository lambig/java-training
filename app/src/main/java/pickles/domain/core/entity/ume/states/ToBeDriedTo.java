package pickles.domain.core.entity.ume.states;

public interface ToBeDriedTo<E extends Dried> {
    E dried();

    String id();
}
