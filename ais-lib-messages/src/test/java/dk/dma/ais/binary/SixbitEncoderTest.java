package dk.dma.ais.binary;

import dk.dma.ais.sentence.Vdm;
import org.junit.Test;

import static org.junit.Assert.*;

public class SixbitEncoderTest {

    @Test
    public void testEncodeWithPadding() throws Exception {
        final String encodedPayload = "63@ndh@l=v9P=dD";

        BinArray encodedBinArray = new BinArray();
        encodedBinArray.append(Integer.parseInt("000110", 2), 6); // '6'
        encodedBinArray.append(Integer.parseInt("000011", 2), 6); // '3'
        encodedBinArray.append(Integer.parseInt("010000", 2), 6); // '@'
        encodedBinArray.append(Integer.parseInt("110110", 2), 6); // 'n'
        encodedBinArray.append(Integer.parseInt("101100", 2), 6); // 'd'
        encodedBinArray.append(Integer.parseInt("110000", 2), 6); // 'h'
        encodedBinArray.append(Integer.parseInt("010000", 2), 6); // '@'
        encodedBinArray.append(Integer.parseInt("110100", 2), 6); // 'l'
        encodedBinArray.append(Integer.parseInt("001101", 2), 6); // '='
        encodedBinArray.append(Integer.parseInt("111110", 2), 6); // 'v'
        encodedBinArray.append(Integer.parseInt("001001", 2), 6); // '9'
        encodedBinArray.append(Integer.parseInt("100000", 2), 6); // 'P'
        encodedBinArray.append(Integer.parseInt("001101", 2), 6); // '='
        encodedBinArray.append(Integer.parseInt("101100", 2), 6); // 'd'
        encodedBinArray.append(Integer.parseInt("0101", 2), 4);   // 'D'  <-- requires 2 bit padding

        // Test that encoding of encodedBinArray leads to "63@ndh@l=v9P=dD"
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.append(encodedBinArray);
        String encodedString = encoder.encode();
        assertEquals(encodedPayload, encodedString);

        // Test that decoding of "63@ndh@l=v9P=dD" leads to encodedBinArray
        Vdm vdm = new Vdm();
        vdm.parse("!AIVDM,1,1,0,," + encodedPayload + ",2*61");
        BinArray decodedBinArray = vdm.getBinArray();
        assertEquals(decodedBinArray.toString(), encodedBinArray.toString());
    }

}