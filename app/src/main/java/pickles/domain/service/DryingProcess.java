package pickles.domain.service;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import pickles.domain.core.entity.ume.states.ToBeDriedTo;
import pickles.domain.core.entity.ume.states.Dried;

/**
 * 干し工程をつかさどるドメインサービス。共通で1500ミリ秒かかる。
 */
public class DryingProcess {
    public <E extends Dried> Single<E> dry(ToBeDriedTo<E> toBeDried) {
        return Single
                .<E>create(emmiter -> {
                    System.out.println("干し開始: " + toBeDried.id());
                    Thread.sleep(1500);
                    emmiter.onSuccess(toBeDried.dried());
                })
                .subscribeOn(Schedulers.io())
                .doOnSuccess(d -> System.out.println("干し完了" + d.id()));
    }
}
