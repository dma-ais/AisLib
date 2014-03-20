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
		T__19=1, T__18=2, T__17=3, T__16=4, T__15=5, T__14=6, T__13=7, T__12=8, 
		T__11=9, T__10=10, T__9=11, T__8=12, T__7=13, T__6=14, T__5=15, T__4=16, 
		T__3=17, T__2=18, T__1=19, T__0=20, EQUALS=21, NEQUALS=22, WS=23, AND=24, 
		OR=25, INT=26, FLOAT=27, SIXBITCHAR=28, SIXBITSTRING=29;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'s.type'", "'messagetype'", "'m.sog'", "'>='", "'..'", "'<'", "'s.id'", 
		"'s.region'", "'>'", "'@'", "'m.mmsi'", "'<='", "'IN'", "'in'", "'m.id'", 
		"'s.country'", "'('", "')'", "'s.bs'", "','", "'='", "'!='", "WS", "'&'", 
		"'|'", "INT", "FLOAT", "SIXBITCHAR", "SIXBITSTRING"
	};
	public static final String[] ruleNames = {
		"T__19", "T__18", "T__17", "T__16", "T__15", "T__14", "T__13", "T__12", 
		"T__11", "T__10", "T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", 
		"T__2", "T__1", "T__0", "EQUALS", "NEQUALS", "WS", "AND", "OR", "INT", 
		"FLOAT", "SIXBITCHAR", "SIXBITSTRING"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\37\u00c7\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3"+
		"\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3"+
		"\30\6\30\u00a1\n\30\r\30\16\30\u00a2\3\30\3\30\3\31\3\31\3\32\3\32\3\33"+
		"\5\33\u00ac\n\33\3\33\6\33\u00af\n\33\r\33\16\33\u00b0\3\34\5\34\u00b4"+
		"\n\34\3\34\6\34\u00b7\n\34\r\34\16\34\u00b8\3\34\3\34\6\34\u00bd\n\34"+
		"\r\34\16\34\u00be\3\35\3\35\3\36\6\36\u00c4\n\36\r\36\16\36\u00c5\2\2"+
		"\37\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\37\3\2\5\4\2\13\13\"\"\3\2\62;\5\2\62;C\\c|\u00cd\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2"+
		"\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3"+
		"\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'"+
		"\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63"+
		"\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5"+
		"D\3\2\2\2\7P\3\2\2\2\tV\3\2\2\2\13Y\3\2\2\2\r\\\3\2\2\2\17^\3\2\2\2\21"+
		"c\3\2\2\2\23l\3\2\2\2\25n\3\2\2\2\27p\3\2\2\2\31w\3\2\2\2\33z\3\2\2\2"+
		"\35}\3\2\2\2\37\u0080\3\2\2\2!\u0085\3\2\2\2#\u008f\3\2\2\2%\u0091\3\2"+
		"\2\2\'\u0093\3\2\2\2)\u0098\3\2\2\2+\u009a\3\2\2\2-\u009c\3\2\2\2/\u00a0"+
		"\3\2\2\2\61\u00a6\3\2\2\2\63\u00a8\3\2\2\2\65\u00ab\3\2\2\2\67\u00b3\3"+
		"\2\2\29\u00c0\3\2\2\2;\u00c3\3\2\2\2=>\7u\2\2>?\7\60\2\2?@\7v\2\2@A\7"+
		"{\2\2AB\7r\2\2BC\7g\2\2C\4\3\2\2\2DE\7o\2\2EF\7g\2\2FG\7u\2\2GH\7u\2\2"+
		"HI\7c\2\2IJ\7i\2\2JK\7g\2\2KL\7v\2\2LM\7{\2\2MN\7r\2\2NO\7g\2\2O\6\3\2"+
		"\2\2PQ\7o\2\2QR\7\60\2\2RS\7u\2\2ST\7q\2\2TU\7i\2\2U\b\3\2\2\2VW\7@\2"+
		"\2WX\7?\2\2X\n\3\2\2\2YZ\7\60\2\2Z[\7\60\2\2[\f\3\2\2\2\\]\7>\2\2]\16"+
		"\3\2\2\2^_\7u\2\2_`\7\60\2\2`a\7k\2\2ab\7f\2\2b\20\3\2\2\2cd\7u\2\2de"+
		"\7\60\2\2ef\7t\2\2fg\7g\2\2gh\7i\2\2hi\7k\2\2ij\7q\2\2jk\7p\2\2k\22\3"+
		"\2\2\2lm\7@\2\2m\24\3\2\2\2no\7B\2\2o\26\3\2\2\2pq\7o\2\2qr\7\60\2\2r"+
		"s\7o\2\2st\7o\2\2tu\7u\2\2uv\7k\2\2v\30\3\2\2\2wx\7>\2\2xy\7?\2\2y\32"+
		"\3\2\2\2z{\7K\2\2{|\7P\2\2|\34\3\2\2\2}~\7k\2\2~\177\7p\2\2\177\36\3\2"+
		"\2\2\u0080\u0081\7o\2\2\u0081\u0082\7\60\2\2\u0082\u0083\7k\2\2\u0083"+
		"\u0084\7f\2\2\u0084 \3\2\2\2\u0085\u0086\7u\2\2\u0086\u0087\7\60\2\2\u0087"+
		"\u0088\7e\2\2\u0088\u0089\7q\2\2\u0089\u008a\7w\2\2\u008a\u008b\7p\2\2"+
		"\u008b\u008c\7v\2\2\u008c\u008d\7t\2\2\u008d\u008e\7{\2\2\u008e\"\3\2"+
		"\2\2\u008f\u0090\7*\2\2\u0090$\3\2\2\2\u0091\u0092\7+\2\2\u0092&\3\2\2"+
		"\2\u0093\u0094\7u\2\2\u0094\u0095\7\60\2\2\u0095\u0096\7d\2\2\u0096\u0097"+
		"\7u\2\2\u0097(\3\2\2\2\u0098\u0099\7.\2\2\u0099*\3\2\2\2\u009a\u009b\7"+
		"?\2\2\u009b,\3\2\2\2\u009c\u009d\7#\2\2\u009d\u009e\7?\2\2\u009e.\3\2"+
		"\2\2\u009f\u00a1\t\2\2\2\u00a0\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2"+
		"\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a5\b\30"+
		"\2\2\u00a5\60\3\2\2\2\u00a6\u00a7\7(\2\2\u00a7\62\3\2\2\2\u00a8\u00a9"+
		"\7~\2\2\u00a9\64\3\2\2\2\u00aa\u00ac\7/\2\2\u00ab\u00aa\3\2\2\2\u00ab"+
		"\u00ac\3\2\2\2\u00ac\u00ae\3\2\2\2\u00ad\u00af\t\3\2\2\u00ae\u00ad\3\2"+
		"\2\2\u00af\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1"+
		"\66\3\2\2\2\u00b2\u00b4\7/\2\2\u00b3\u00b2\3\2\2\2\u00b3\u00b4\3\2\2\2"+
		"\u00b4\u00b6\3\2\2\2\u00b5\u00b7\t\3\2\2\u00b6\u00b5\3\2\2\2\u00b7\u00b8"+
		"\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba"+
		"\u00bc\7\60\2\2\u00bb\u00bd\t\3\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00be\3"+
		"\2\2\2\u00be\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf8\3\2\2\2\u00c0\u00c1"+
		"\13\2\2\2\u00c1:\3\2\2\2\u00c2\u00c4\t\4\2\2\u00c3\u00c2\3\2\2\2\u00c4"+
		"\u00c5\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6<\3\2\2\2"+
		"\n\2\u00a2\u00ab\u00b0\u00b3\u00b8\u00be\u00c5\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}