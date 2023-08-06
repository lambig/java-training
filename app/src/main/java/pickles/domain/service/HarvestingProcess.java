package pickles.domain.service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.reactivex.Observable;
import pickles.domain.core.entity.ume.Ume;
import pickles.domain.generic.random.RandomIntSupplier;

/**
 * 梅の収穫をつかさどるサービス
 */
public class HarvestingProcess {
    private final RandomIntSupplier durationSupplier;
    private final RandomIntSupplier batchSizeSupplier;

    public HarvestingProcess(RandomIntSupplier durationSupplier, RandomIntSupplier batchSizeSupplier) {
        this.durationSupplier = durationSupplier;
        this.batchSizeSupplier = batchSizeSupplier;
    }

    /**
     * emitterが生きている限り0.3秒から3秒に一回、1個から6個の梅を吐き出し続けるobserverを返す。
     */
    public Observable<Set<Ume>> service() {
        return Observable
                .<Set<Ume>>create(emitter -> {
                    System.out.println("収穫待機開始");
                    while (!emitter.isDisposed()) {
                        System.out.println("収穫開始");
                        Thread.sleep(this.durationSupplier.upTo(2700) + 300);
                        emitter.onNext(
                                IntStream.range(0, this.batchSizeSupplier.upTo(5) + 1)
                                        .mapToObj(unused -> UUID.randomUUID().toString())
                                        .map(Ume::new)
                                        .collect(Collectors.toSet()));
                    }
                    System.out.println("収穫中止");
                })
                .doOnNext(set -> System.out.println("収穫: " + set.size() + "個"));
    }
}
