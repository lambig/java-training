package pickles.domain.generic.random;

import java.security.SecureRandom;
import java.util.Random;

public class SecureRandomIntSupplier implements RandomIntSupplier {
    private final Random random = new SecureRandom();

    public Integer upTo(int max) {
        return this.random.nextInt(max);
    }

}
