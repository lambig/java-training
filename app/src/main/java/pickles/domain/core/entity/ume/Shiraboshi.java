package pickles.domain.core.entity.ume;

import pickles.domain.Entity;
import pickles.domain.core.entity.ume.states.Dried;
import pickles.domain.core.entity.ume.states.ToBeMarinatedTo;

/**
 * 塩漬けしたあと、土用干しした状態の梅漬け(白干し)。
 */
public class Shiraboshi implements Entity<Shiraboshi>, Dried, ToBeMarinatedTo<Umeboshi> {
    private final String id;

    public Shiraboshi(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    @Override
    public Umeboshi marinated() {
        return new Umeboshi(this.id() + "m");
    }

}
