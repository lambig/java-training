package pickles.application;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pickles.domain.service.CuringProcess;
import pickles.domain.service.DryingProcess;
import pickles.domain.service.HarvestingProcess;
import pickles.domain.service.MarinatingProcess;

import static java.util.function.Predicate.not;

public final class Pickling {
    private final HarvestingProcess harvestingProcess;
    private final CuringProcess curingProcess;
    private final DryingProcess dryingProcess;
    private final MarinatingProcess marinatingProcess;

    public Pickling(
            HarvestingProcess harvestingProcess,
            CuringProcess curingProcess,
            DryingProcess dryingProcess,
            MarinatingProcess marinatingProcess) {
        this.harvestingProcess = harvestingProcess;
        this.curingProcess = curingProcess;
        this.dryingProcess = dryingProcess;
        this.marinatingProcess = marinatingProcess;
    }

    public void doPickle() {
        AtomicInteger latch = new AtomicInteger(0);
        AtomicInteger completed = new AtomicInteger(0);
        AtomicInteger wentOnTheLine = new AtomicInteger(0);
        AtomicReference<Disposable> harvestingDisposable = new AtomicReference<>(null);

        System.out.println("処理開始");
        Disposable process = this.harvestingProcess.service()
                .doOnSubscribe(harvestingDisposable::set)
                .flatMap(umeSet -> Observable.fromIterable(umeSet))
                .doOnNext(umeboshi -> wentOnTheLine.incrementAndGet())
                .doOnNext(ume -> latch.incrementAndGet())
                .flatMap(row -> this.curingProcess.cure(row).toObservable()
                        .doOnError(error -> System.out.println(error.getMessage())), 5) // 5個まで同時に漬けられる
                .flatMap(shiraumezuke -> this.dryingProcess.dry(shiraumezuke).toObservable()
                        .doOnError(error -> System.out.println(error.getMessage())), 5)
                .flatMap(shiraboshi -> this.marinatingProcess.marinate(shiraboshi).toObservable()
                        .doOnError(error -> System.out.println(error.getMessage())), 5)
                .doOnError(error -> {
                    System.out.println(error.getMessage());
                    latch.decrementAndGet();
                })
                .doOnNext(umeboshi -> latch.decrementAndGet())
                .doOnNext(umeboshi -> completed.incrementAndGet())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        umeboshi -> System.out.println("完成: " + umeboshi.id()),
                        throwable -> {
                            System.out.println("なんか事故ったっぽいです 処理止めます" + throwable.getMessage());
                        },
                        () -> {
                            System.out.println("無が漬かりました 処理止めます");
                            throw new IllegalStateException("無が漬かりました");
                        });

        try {
            Thread.sleep(10000); // 10秒間ほっとく
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("時間終了、撤収準備");
        Optional.of(harvestingDisposable) // 収穫を先に止める
                .map(AtomicReference::get)
                .filter(not(Disposable::isDisposed))
                .ifPresent(Disposable::dispose);

        while (latch.get() > 0) { // 全部処理が終わったか確認
            try {
                System.out.println("残作業待ち: " + latch.get() + "件");
                System.out.println("ラインに乗った総数: " + wentOnTheLine.get() + "個");
                System.out.println("完成個数: " + completed.get() + "個");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("残作業: " + latch.get() + "件");
        System.out.println("ラインに乗った総数: " + wentOnTheLine.get() + "個");
        System.out.println("完成個数: " + completed.get() + "個");
        System.out.println("処理完了");
        process.dispose(); // ライン全体を止める
    }
}
