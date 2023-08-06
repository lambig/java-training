package pickles.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.management.RuntimeErrorException;

import io.github.lambig.patterns.Patterns;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import pickles.domain.service.CuringProcess;
import pickles.domain.service.DryingProcess;
import pickles.domain.service.HarvestingProcess;
import pickles.domain.service.MarinatingProcess;

import static io.github.lambig.patterns.Patterns.*;
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
        List<String> trashBin = new ArrayList<>();
        Consumer<InterruptedException> c = t -> trashBin.add(t.getMessage());
        Consumer<? extends Throwable> rethrow = t -> {
            throw new RuntimeException(t);
        };
        RxJavaPlugins.setErrorHandler(t -> {
            throw new RuntimeException(t);
        });

        AtomicInteger latch = new AtomicInteger(0);
        AtomicReference<Disposable> harvestingDisposable = new AtomicReference<>(null);
        this.harvestingProcess.service()
                .doOnSubscribe(harvestingDisposable::set)
                .flatMap(umeSet -> Observable.fromIterable(umeSet))
                .doOnNext(ume -> latch.incrementAndGet())
                .doOnTerminate(() -> latch.decrementAndGet())
                .flatMap(row -> this.curingProcess.cure(row).toObservable(), 5) // 5個まで同時に漬けられる
                .flatMap(shiraumezuke -> this.dryingProcess.dry(shiraumezuke).toObservable(), 5)
                .flatMap(shiraboshi -> this.marinatingProcess.marinate(shiraboshi).toObservable(), 5)
                .subscribe(
                        umeboshi -> System.out.println("完成: " + umeboshi.id()),
                        throwable -> {
                            System.out.println("なんか事故ったっぽいです 処理止めます" + throwable.getMessage());
                        },
                        () -> {
                            System.out.println("無が漬かりました 処理止めます");
                            throw new IllegalStateException("無が漬かりました");
                        },
                        disposable -> {
                            new Thread(() -> { // 別スレ立てないとobserverの購読までブロックされて処理が始まらない
                                System.out.println("処理開始");
                                try {
                                    Thread.sleep(10000); // 10秒間ほっとく
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println("時間終了、撤収準備");
                                Optional.of(harvestingDisposable) // 収穫を先に止める
                                        .map(AtomicReference::get)
                                        .filter(not(Disposable::isDisposed))
                                        .ifPresent(Disposable::dispose);

                                while (latch.get() > 0) { // 全部処理が終わったか確認
                                    try {
                                        System.out.println("残作業待ち、残り: " + latch.get() + "件");
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                disposable.dispose(); // ライン全体を止める
                                System.out.println("処理完了");
                            }).start();
                        });
    }
}
