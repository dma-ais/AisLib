package dk.dma.ais.packet;

import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.SentenceLine;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class VsiTest {
    private static final String VSI_SENTENCE = "\\c:1490398110,C:1133,s:M-Kingsburg*5D\\$AIVSI,M-Kingsburg,4,232830,1133,-111,8*66";

    private SentenceLine sentenceLine;
    private Vsi vsi;

    @Before
    public void setUp() throws Exception {
        sentenceLine = new SentenceLine(VSI_SENTENCE);
        vsi = new Vsi();
    }

    @Test
    public void whenParse_thenCommentBlockIsParsed() throws SentenceException {
        vsi.parse(sentenceLine);

        assertThat(vsi.getCommentBlock(), is(not(nullValue())));
        assertThat(vsi.getCommentBlock().getString("C"), is(equalTo("1133")));
        assertThat(vsi.getCommentBlock().getString("s"), is(equalTo("M-Kingsburg")));
    }

    @Test
    public void whenParse_thenSignalStrengthIsParsed() throws SentenceException {
        vsi.parse(sentenceLine);

        assertThat(vsi.getSignalStrength(), is(equalTo(-111)));
    }

    @Test
    public void givenASentenceWithAnInvalidSignalStrength_whenParse_thenDefaultValueIsUsed() throws SentenceException {
        vsi.parse(new SentenceLine("\\c:1497228032,C:1206,s:M-Chebucto*2F\\$AIVSI,M-Chebucto,4,,,,*31"));

        assertThat(vsi.getSignalStrength(), is(equalTo(Integer.MIN_VALUE)));
    }
}
