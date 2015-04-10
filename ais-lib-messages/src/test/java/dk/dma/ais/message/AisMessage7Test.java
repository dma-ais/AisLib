package dk.dma.ais.message;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

public class AisMessage7Test {

    @Test(expected = SixbitException.class)
    public void test() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse(
            "!AIVDM,1,1,,A,701uEO1:CovP00,4*6A"
        );

        AisMessage.getInstance(vdm);
    }

    @Test(expected = SixbitException.class)
    public void test2() throws AisMessageException, SixbitException, SentenceException {
        Vdm vdm = new Vdm();
        vdm.parse(
            "!AIVDM,1,1,,A,7000W50rgMN2BK@:h00,2*4E\n"
        );

        AisMessage.getInstance(vdm);
    }

}
