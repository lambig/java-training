package pickles.domain;

public interface AggregateRoot<A extends AggregateRoot<A>> extends Entity<A> {

}
