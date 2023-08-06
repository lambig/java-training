package pickles.domain.service;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import pickles.domain.core.entity.ume.Shiraumezuke;
import pickles.domain.core.entity.ume.Ume;

/**
 * 塩漬け工程。1秒かかる。
 */
public class CuringProcess {
    public Single<Shiraumezuke> cure(Ume row) {
        return Single
                .<Shiraumezuke>create(emitter -> {
                    System.out.println("塩漬け開始:" + row.id());
                    Thread.sleep(1000);
                    emitter.onSuccess(new Shiraumezuke(row.id() + "c"));
                })
                .subscribeOn(Schedulers.io()) // IO用のスレッドで実行するという指定。これ呼んでおかないと親スレッドがブロックされて並列に漬けられない
                .doOnSuccess(s -> System.out.println("塩漬け完了:" + s.id()));
    }
}
