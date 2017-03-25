package dk.dma.ais.packet;

import dk.dma.ais.sentence.SentenceException;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public class AisPacketParserTest {
    private static final String VSI_SENTENCE = "\\c:1490398110,C:1133,s:P-Helmcken*35\\$AIVSI,P-Helmcken,6,232830,1133,-79,34*0C";
    private static final String VDM_SENTENCE = "\\c:1490398110,C:1133,s:P-Helmcken*35\\!AIVDM,1,1,6,B,15NCn?0P00o:pljKRG57Hgvt26qh,0*64";

    @Test
    public void givenAVsiSentenceWithNoCorrespondingVdm_whenReadLine_thenNullIsReturned() throws SentenceException {
        AisPacketParser packetParser = new AisPacketParser();

        AisPacket aisPacket = packetParser.readLine(VSI_SENTENCE);

        assertThat(aisPacket, is(nullValue()));
    }

    @Test
    public void givenAVsiSentence_whenReadLine_thenAisPacketContainingVsiDetailsIsReturned() throws SentenceException {
        AisPacketParser packetParser = new AisPacketParser();

        packetParser.readLine(VDM_SENTENCE);
        AisPacket aisPacket = packetParser.readLine(VSI_SENTENCE);

        assertThat(aisPacket.isVsi(), is(true));
        assertThat(aisPacket.isValidMessage(), is(true));
        assertThat(aisPacket.getVsi(), is(not(nullValue())));
        assertThat(aisPacket.getVsi().getCommentBlock(), is(not(nullValue())));
        assertThat(aisPacket.getVsi().getCommentBlock().getString("C"), is(equalTo("1133")));
        assertThat(aisPacket.getVsi().getCommentBlock().getString("s"), is(equalTo("P-Helmcken")));
        assertThat(aisPacket.getVsi().getSignalStrength(), is(equalTo(-79)));
        assertThat(aisPacket.tryGetPositionTime().getLatitude(), is(closeTo(48.124193, 0.000001)));
        assertThat(aisPacket.tryGetPositionTime().getLongitude(), is(closeTo(-123.450625, 0.00001)));
        assertThat(aisPacket.getVsi().getTimestamp(), is(equalTo(new Date(1490398110000L))));
    }
}
