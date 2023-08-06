package pickles.domain.core.entity.ume;

import pickles.domain.Entity;
import pickles.domain.core.entity.ume.states.ToBeDriedTo;

/**
 * 塩漬けされた状態の梅(白梅漬け)
 */
public class Shiraumezuke implements Entity<Shiraumezuke>, ToBeDriedTo<Shiraboshi> {

    private final String id;

    public Shiraumezuke(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Shiraboshi dried() {
        return new Shiraboshi(this.id() + "d");
    }
}
