package pickles.domain.core.entity.ume;

import pickles.domain.Entity;
import pickles.domain.core.entity.ume.states.Dried;
import pickles.domain.core.entity.ume.states.Marinated;

/**
 * 白干しを漬けた梅干し。エンドプロダクトの一つ。
 */
public class Umeboshi implements Entity<Umeboshi>, Dried, Marinated {
    private final String id;

    public Umeboshi(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

}
