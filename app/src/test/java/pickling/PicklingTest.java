package pickling;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pickles.application.Pickling;
import pickles.domain.generic.random.SecureRandomIntSupplier;
import pickles.domain.service.CuringProcess;
import pickles.domain.service.DryingProcess;
import pickles.domain.service.HarvestingProcess;
import pickles.domain.service.MarinatingProcess;

public class PicklingTest {
    @Nested
    class doPickleのテスト {
        @Test
        void 動作確認(){
            //SetUp
            var sut = new Pickling(new HarvestingProcess(new SecureRandomIntSupplier(), new SecureRandomIntSupplier()),new CuringProcess(), new DryingProcess(), new MarinatingProcess());

            //Exercise
            sut.doPickle();
            //Verify
        }
    }
}
