package pickles.domain;

public interface DomainObject<D> {
    boolean equivalentTo(D another);
}
