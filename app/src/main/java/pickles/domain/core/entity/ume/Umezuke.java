package pickles.domain.core.entity.ume;

import pickles.domain.Entity;
import pickles.domain.core.entity.ume.states.ToBeDriedTo;
import pickles.domain.core.entity.ume.states.ToBeMarinatedTo;

/**
 * 塩漬けした状態の梅漬け。
 */
public class Umezuke implements Entity<Umeboshi>, ToBeMarinatedTo<ChomiUmezuke>, ToBeDriedTo<Shiraboshi> {
    private final String id;

    public Umezuke(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    @Override
    public Shiraboshi dried() {
        return new Shiraboshi(this.id() + "d");
    }

    @Override
    public ChomiUmezuke marinated() {
        return new ChomiUmezuke(this.id() + "m");
    }

}
