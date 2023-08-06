package pickles.domain.core.aggregate;

import pickles.domain.AggregateRoot;
import pickles.domain.core.entity.ume.ChomiUmeboshi;
import pickles.domain.core.entity.ume.ChomiUmezuke;
import pickles.domain.core.entity.ume.Umeboshi;

public class UmeAssort implements AggregateRoot<UmeAssort> {
    private final String id;
    private final Umeboshi umeboshi;
    private final ChomiUmezuke chomiUmezuke;
    private final ChomiUmeboshi chomiUmeboshi;

    public UmeAssort(String id, Umeboshi umeboshi, ChomiUmezuke chomiUmezuke, ChomiUmeboshi chomiUmeboshi) {
        this.id = id;
        this.umeboshi = umeboshi;
        this.chomiUmezuke = chomiUmezuke;
        this.chomiUmeboshi = chomiUmeboshi;
    }

    @Override
    public String id() {
        return this.id;
    }

    public String check() {
        return this.umeboshi.id() + this.chomiUmezuke.id() + this.chomiUmeboshi.id();
    }
}
