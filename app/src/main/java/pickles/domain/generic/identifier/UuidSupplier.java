package pickles.domain.generic.identifier;

import java.util.UUID;

public class UuidSupplier implements IdSupplier {

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
