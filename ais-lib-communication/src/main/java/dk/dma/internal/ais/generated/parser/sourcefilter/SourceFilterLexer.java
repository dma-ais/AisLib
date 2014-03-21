// Generated from SourceFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.sourcefilter;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SourceFilterLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__26=1, T__25=2, T__24=3, T__23=4, T__22=5, T__21=6, T__20=7, T__19=8, 
		T__18=9, T__17=10, T__16=11, T__15=12, T__14=13, T__13=14, T__12=15, T__11=16, 
		T__10=17, T__9=18, T__8=19, T__7=20, T__6=21, T__5=22, T__4=23, T__3=24, 
		T__2=25, T__1=26, T__0=27, EQUALS=28, NEQUALS=29, WS=30, AND=31, OR=32, 
		INT=33, FLOAT=34, SIXBITCHAR=35, SIXBITSTRING=36;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'s.type'", "'m.lat'", "'m.name'", "'..'", "'m.mmsi'", "'<='", "'m.hdg'", 
		"'s.country'", "'('", "','", "'m.cog'", "'messagetype'", "'m.draught'", 
		"'m.sog'", "'>='", "'<'", "'s.id'", "'s.region'", "'>'", "'@'", "'m.lon'", 
		"'IN'", "'in'", "'m.imo'", "'m.id'", "')'", "'s.bs'", "'='", "'!='", "WS", 
		"'&'", "'|'", "INT", "FLOAT", "SIXBITCHAR", "SIXBITSTRING"
	};
	public static final String[] ruleNames = {
		"T__26", "T__25", "T__24", "T__23", "T__22", "T__21", "T__20", "T__19", 
		"T__18", "T__17", "T__16", "T__15", "T__14", "T__13", "T__12", "T__11", 
		"T__10", "T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", 
		"T__1", "T__0", "EQUALS", "NEQUALS", "WS", "AND", "OR", "INT", "FLOAT", 
		"SIXBITCHAR", "SIXBITSTRING"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2&\u0104\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21"+
		"\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\37\6\37\u00de"+
		"\n\37\r\37\16\37\u00df\3\37\3\37\3 \3 \3!\3!\3\"\5\"\u00e9\n\"\3\"\6\""+
		"\u00ec\n\"\r\"\16\"\u00ed\3#\5#\u00f1\n#\3#\6#\u00f4\n#\r#\16#\u00f5\3"+
		"#\3#\6#\u00fa\n#\r#\16#\u00fb\3$\3$\3%\6%\u0101\n%\r%\16%\u0102\2\2&\3"+
		"\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37"+
		"\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37="+
		" ?!A\"C#E$G%I&\3\2\5\4\2\13\13\"\"\3\2\62;\5\2\62;C\\c|\u010a\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3"+
		"\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3"+
		"\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2"+
		"=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3"+
		"\2\2\2\3K\3\2\2\2\5R\3\2\2\2\7X\3\2\2\2\t_\3\2\2\2\13b\3\2\2\2\ri\3\2"+
		"\2\2\17l\3\2\2\2\21r\3\2\2\2\23|\3\2\2\2\25~\3\2\2\2\27\u0080\3\2\2\2"+
		"\31\u0086\3\2\2\2\33\u0092\3\2\2\2\35\u009c\3\2\2\2\37\u00a2\3\2\2\2!"+
		"\u00a5\3\2\2\2#\u00a7\3\2\2\2%\u00ac\3\2\2\2\'\u00b5\3\2\2\2)\u00b7\3"+
		"\2\2\2+\u00b9\3\2\2\2-\u00bf\3\2\2\2/\u00c2\3\2\2\2\61\u00c5\3\2\2\2\63"+
		"\u00cb\3\2\2\2\65\u00d0\3\2\2\2\67\u00d2\3\2\2\29\u00d7\3\2\2\2;\u00d9"+
		"\3\2\2\2=\u00dd\3\2\2\2?\u00e3\3\2\2\2A\u00e5\3\2\2\2C\u00e8\3\2\2\2E"+
		"\u00f0\3\2\2\2G\u00fd\3\2\2\2I\u0100\3\2\2\2KL\7u\2\2LM\7\60\2\2MN\7v"+
		"\2\2NO\7{\2\2OP\7r\2\2PQ\7g\2\2Q\4\3\2\2\2RS\7o\2\2ST\7\60\2\2TU\7n\2"+
		"\2UV\7c\2\2VW\7v\2\2W\6\3\2\2\2XY\7o\2\2YZ\7\60\2\2Z[\7p\2\2[\\\7c\2\2"+
		"\\]\7o\2\2]^\7g\2\2^\b\3\2\2\2_`\7\60\2\2`a\7\60\2\2a\n\3\2\2\2bc\7o\2"+
		"\2cd\7\60\2\2de\7o\2\2ef\7o\2\2fg\7u\2\2gh\7k\2\2h\f\3\2\2\2ij\7>\2\2"+
		"jk\7?\2\2k\16\3\2\2\2lm\7o\2\2mn\7\60\2\2no\7j\2\2op\7f\2\2pq\7i\2\2q"+
		"\20\3\2\2\2rs\7u\2\2st\7\60\2\2tu\7e\2\2uv\7q\2\2vw\7w\2\2wx\7p\2\2xy"+
		"\7v\2\2yz\7t\2\2z{\7{\2\2{\22\3\2\2\2|}\7*\2\2}\24\3\2\2\2~\177\7.\2\2"+
		"\177\26\3\2\2\2\u0080\u0081\7o\2\2\u0081\u0082\7\60\2\2\u0082\u0083\7"+
		"e\2\2\u0083\u0084\7q\2\2\u0084\u0085\7i\2\2\u0085\30\3\2\2\2\u0086\u0087"+
		"\7o\2\2\u0087\u0088\7g\2\2\u0088\u0089\7u\2\2\u0089\u008a\7u\2\2\u008a"+
		"\u008b\7c\2\2\u008b\u008c\7i\2\2\u008c\u008d\7g\2\2\u008d\u008e\7v\2\2"+
		"\u008e\u008f\7{\2\2\u008f\u0090\7r\2\2\u0090\u0091\7g\2\2\u0091\32\3\2"+
		"\2\2\u0092\u0093\7o\2\2\u0093\u0094\7\60\2\2\u0094\u0095\7f\2\2\u0095"+
		"\u0096\7t\2\2\u0096\u0097\7c\2\2\u0097\u0098\7w\2\2\u0098\u0099\7i\2\2"+
		"\u0099\u009a\7j\2\2\u009a\u009b\7v\2\2\u009b\34\3\2\2\2\u009c\u009d\7"+
		"o\2\2\u009d\u009e\7\60\2\2\u009e\u009f\7u\2\2\u009f\u00a0\7q\2\2\u00a0"+
		"\u00a1\7i\2\2\u00a1\36\3\2\2\2\u00a2\u00a3\7@\2\2\u00a3\u00a4\7?\2\2\u00a4"+
		" \3\2\2\2\u00a5\u00a6\7>\2\2\u00a6\"\3\2\2\2\u00a7\u00a8\7u\2\2\u00a8"+
		"\u00a9\7\60\2\2\u00a9\u00aa\7k\2\2\u00aa\u00ab\7f\2\2\u00ab$\3\2\2\2\u00ac"+
		"\u00ad\7u\2\2\u00ad\u00ae\7\60\2\2\u00ae\u00af\7t\2\2\u00af\u00b0\7g\2"+
		"\2\u00b0\u00b1\7i\2\2\u00b1\u00b2\7k\2\2\u00b2\u00b3\7q\2\2\u00b3\u00b4"+
		"\7p\2\2\u00b4&\3\2\2\2\u00b5\u00b6\7@\2\2\u00b6(\3\2\2\2\u00b7\u00b8\7"+
		"B\2\2\u00b8*\3\2\2\2\u00b9\u00ba\7o\2\2\u00ba\u00bb\7\60\2\2\u00bb\u00bc"+
		"\7n\2\2\u00bc\u00bd\7q\2\2\u00bd\u00be\7p\2\2\u00be,\3\2\2\2\u00bf\u00c0"+
		"\7K\2\2\u00c0\u00c1\7P\2\2\u00c1.\3\2\2\2\u00c2\u00c3\7k\2\2\u00c3\u00c4"+
		"\7p\2\2\u00c4\60\3\2\2\2\u00c5\u00c6\7o\2\2\u00c6\u00c7\7\60\2\2\u00c7"+
		"\u00c8\7k\2\2\u00c8\u00c9\7o\2\2\u00c9\u00ca\7q\2\2\u00ca\62\3\2\2\2\u00cb"+
		"\u00cc\7o\2\2\u00cc\u00cd\7\60\2\2\u00cd\u00ce\7k\2\2\u00ce\u00cf\7f\2"+
		"\2\u00cf\64\3\2\2\2\u00d0\u00d1\7+\2\2\u00d1\66\3\2\2\2\u00d2\u00d3\7"+
		"u\2\2\u00d3\u00d4\7\60\2\2\u00d4\u00d5\7d\2\2\u00d5\u00d6\7u\2\2\u00d6"+
		"8\3\2\2\2\u00d7\u00d8\7?\2\2\u00d8:\3\2\2\2\u00d9\u00da\7#\2\2\u00da\u00db"+
		"\7?\2\2\u00db<\3\2\2\2\u00dc\u00de\t\2\2\2\u00dd\u00dc\3\2\2\2\u00de\u00df"+
		"\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1"+
		"\u00e2\b\37\2\2\u00e2>\3\2\2\2\u00e3\u00e4\7(\2\2\u00e4@\3\2\2\2\u00e5"+
		"\u00e6\7~\2\2\u00e6B\3\2\2\2\u00e7\u00e9\7/\2\2\u00e8\u00e7\3\2\2\2\u00e8"+
		"\u00e9\3\2\2\2\u00e9\u00eb\3\2\2\2\u00ea\u00ec\t\3\2\2\u00eb\u00ea\3\2"+
		"\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee"+
		"D\3\2\2\2\u00ef\u00f1\7/\2\2\u00f0\u00ef\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1"+
		"\u00f3\3\2\2\2\u00f2\u00f4\t\3\2\2\u00f3\u00f2\3\2\2\2\u00f4\u00f5\3\2"+
		"\2\2\u00f5\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7"+
		"\u00f9\7\60\2\2\u00f8\u00fa\t\3\2\2\u00f9\u00f8\3\2\2\2\u00fa\u00fb\3"+
		"\2\2\2\u00fb\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fcF\3\2\2\2\u00fd\u00fe"+
		"\13\2\2\2\u00feH\3\2\2\2\u00ff\u0101\t\4\2\2\u0100\u00ff\3\2\2\2\u0101"+
		"\u0102\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103J\3\2\2\2"+
		"\n\2\u00df\u00e8\u00ed\u00f0\u00f5\u00fb\u0102\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}