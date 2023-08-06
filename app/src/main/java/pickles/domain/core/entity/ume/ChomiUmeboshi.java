package pickles.domain.core.entity.ume;

import pickles.domain.Entity;
import pickles.domain.core.entity.ume.states.Dried;
import pickles.domain.core.entity.ume.states.Marinated;
/**
 * 調味梅干し。エンドプロダクトの一つ。
 */
public class ChomiUmeboshi implements Entity<ChomiUmeboshi>, Dried, Marinated {
    private final String id;

    public ChomiUmeboshi(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

}
