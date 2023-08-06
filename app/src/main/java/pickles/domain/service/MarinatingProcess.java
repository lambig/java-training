package pickles.domain.service;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import pickles.domain.core.entity.ume.states.Marinated;
import pickles.domain.core.entity.ume.states.ToBeMarinatedTo;

/**
 * 漬け工程をつかさどるドメインサービス。共通で2秒かかる。
 */
public class MarinatingProcess {
    public <E extends Marinated> Single<E> marinate(ToBeMarinatedTo<E> toBeMarinated) {
        return Single
                .<E>create(emitter -> {
                    System.out.println("本漬け開始: " + toBeMarinated.id());
                    Thread.sleep(2000);
                    emitter.onSuccess(toBeMarinated.marinated());
                })
                .subscribeOn(Schedulers.io())
                .doOnSuccess(m -> System.out.println("本漬け完了: " + m.id()));
    }
}
