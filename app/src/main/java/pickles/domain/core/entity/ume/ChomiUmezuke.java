package pickles.domain.core.entity.ume;

import pickles.domain.Entity;
import pickles.domain.core.entity.ume.states.Marinated;
import pickles.domain.core.entity.ume.states.ToBeDriedTo;

/**
 * 塩漬けしたあと、赤紫蘇漬けにした状態の調味梅漬け(ドブ漬け)。
 * エンドプロダクトの一つでもある。
 */
public class ChomiUmezuke implements Entity<ChomiUmezuke>, Marinated, ToBeDriedTo<ChomiUmeboshi> {
    private final String id;

    public ChomiUmezuke(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    @Override
    public ChomiUmeboshi dried() {
        return new ChomiUmeboshi(this.id() + "d");
    }

}
