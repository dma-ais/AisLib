// Generated from SourceFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.sourcefilter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SourceFilterLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__13=1, T__12=2, T__11=3, T__10=4, T__9=5, T__8=6, T__7=7, T__6=8, T__5=9, 
		T__4=10, T__3=11, T__2=12, T__1=13, T__0=14, EQUALS=15, NEQUALS=16, WS=17, 
		AND=18, OR=19, ID=20;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'bs'", "'country'", "'id'", "'messagetype'", "'>='", "'<'", "'>'", "'m.mmsi'", 
		"'type'", "'<='", "'('", "'region'", "')'", "','", "'='", "'!='", "WS", 
		"'&'", "'|'", "ID"
	};
	public static final String[] ruleNames = {
		"T__13", "T__12", "T__11", "T__10", "T__9", "T__8", "T__7", "T__6", "T__5", 
		"T__4", "T__3", "T__2", "T__1", "T__0", "EQUALS", "NEQUALS", "WS", "AND", 
		"OR", "ID"
	};


	public SourceFilterLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SourceFilter.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\26}\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n"+
		"\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\17\3\17"+
		"\3\20\3\20\3\21\3\21\3\21\3\22\6\22o\n\22\r\22\16\22p\3\22\3\22\3\23\3"+
		"\23\3\24\3\24\3\25\6\25z\n\25\r\25\16\25{\2\2\26\3\3\5\4\7\5\t\6\13\7"+
		"\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25"+
		")\26\3\2\4\4\2\13\13\"\"\5\2\62;C\\c|~\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
		"\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3"+
		"\2\2\2\3+\3\2\2\2\5.\3\2\2\2\7\66\3\2\2\2\t9\3\2\2\2\13E\3\2\2\2\rH\3"+
		"\2\2\2\17J\3\2\2\2\21L\3\2\2\2\23S\3\2\2\2\25X\3\2\2\2\27[\3\2\2\2\31"+
		"]\3\2\2\2\33d\3\2\2\2\35f\3\2\2\2\37h\3\2\2\2!j\3\2\2\2#n\3\2\2\2%t\3"+
		"\2\2\2\'v\3\2\2\2)y\3\2\2\2+,\7d\2\2,-\7u\2\2-\4\3\2\2\2./\7e\2\2/\60"+
		"\7q\2\2\60\61\7w\2\2\61\62\7p\2\2\62\63\7v\2\2\63\64\7t\2\2\64\65\7{\2"+
		"\2\65\6\3\2\2\2\66\67\7k\2\2\678\7f\2\28\b\3\2\2\29:\7o\2\2:;\7g\2\2;"+
		"<\7u\2\2<=\7u\2\2=>\7c\2\2>?\7i\2\2?@\7g\2\2@A\7v\2\2AB\7{\2\2BC\7r\2"+
		"\2CD\7g\2\2D\n\3\2\2\2EF\7@\2\2FG\7?\2\2G\f\3\2\2\2HI\7>\2\2I\16\3\2\2"+
		"\2JK\7@\2\2K\20\3\2\2\2LM\7o\2\2MN\7\60\2\2NO\7o\2\2OP\7o\2\2PQ\7u\2\2"+
		"QR\7k\2\2R\22\3\2\2\2ST\7v\2\2TU\7{\2\2UV\7r\2\2VW\7g\2\2W\24\3\2\2\2"+
		"XY\7>\2\2YZ\7?\2\2Z\26\3\2\2\2[\\\7*\2\2\\\30\3\2\2\2]^\7t\2\2^_\7g\2"+
		"\2_`\7i\2\2`a\7k\2\2ab\7q\2\2bc\7p\2\2c\32\3\2\2\2de\7+\2\2e\34\3\2\2"+
		"\2fg\7.\2\2g\36\3\2\2\2hi\7?\2\2i \3\2\2\2jk\7#\2\2kl\7?\2\2l\"\3\2\2"+
		"\2mo\t\2\2\2nm\3\2\2\2op\3\2\2\2pn\3\2\2\2pq\3\2\2\2qr\3\2\2\2rs\b\22"+
		"\2\2s$\3\2\2\2tu\7(\2\2u&\3\2\2\2vw\7~\2\2w(\3\2\2\2xz\t\3\2\2yx\3\2\2"+
		"\2z{\3\2\2\2{y\3\2\2\2{|\3\2\2\2|*\3\2\2\2\5\2p{\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}