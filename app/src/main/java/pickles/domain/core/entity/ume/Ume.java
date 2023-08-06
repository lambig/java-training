package pickles.domain.core.entity.ume;

import pickles.domain.Entity;

/**
 * 生の梅。梅をID管理することにしたのでEntityだが、個数ベースでモデリングするならUmesはValueで良さそう。
 */
public class Ume implements Entity<Ume> {
    private final String id;

    public Ume(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

}
