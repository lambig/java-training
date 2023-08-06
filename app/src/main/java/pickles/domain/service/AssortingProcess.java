package pickles.domain.service;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import pickles.domain.core.aggregate.UmeAssort;
import pickles.domain.core.entity.ume.ChomiUmeboshi;
import pickles.domain.core.entity.ume.ChomiUmezuke;
import pickles.domain.core.entity.ume.Umeboshi;
import pickles.domain.generic.identifier.IdSupplier;

/**
 * 箱詰め作業。0.02秒かかる。
 */
public class AssortingProcess {
    private final IdSupplier idSupplier;

    AssortingProcess(IdSupplier idSupplier) {
        this.idSupplier = idSupplier;
    }

    Single<UmeAssort> assort(Umeboshi umeboshi, ChomiUmezuke chomiUmezuke, ChomiUmeboshi chomiUmeboshi) {
        return Single
                .<UmeAssort>create(emitter -> {
                    System.out.println("箱詰め開始");
                    Thread.sleep(20);
                    emitter.onSuccess(new UmeAssort(this.idSupplier.get(), umeboshi, chomiUmezuke, chomiUmeboshi));
                })
                .doOnSuccess(a -> System.out.println("箱詰め完了" + a.id()))
                .subscribeOn(Schedulers.io());
    }
}
