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
		T__17=1, T__16=2, T__15=3, T__14=4, T__13=5, T__12=6, T__11=7, T__10=8, 
		T__9=9, T__8=10, T__7=11, T__6=12, T__5=13, T__4=14, T__3=15, T__2=16, 
		T__1=17, T__0=18, EQUALS=19, NEQUALS=20, WS=21, AND=22, OR=23, ID=24;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'bs'", "'country'", "'id'", "'messagetype'", "'>='", "'..'", "'<'", "'>'", 
		"'@'", "'m.mmsi'", "'type'", "'<='", "'IN'", "'in'", "'('", "'region'", 
		"')'", "','", "'='", "'!='", "WS", "'&'", "'|'", "ID"
	};
	public static final String[] ruleNames = {
		"T__17", "T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", 
		"T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", 
		"T__0", "EQUALS", "NEQUALS", "WS", "AND", "OR", "ID"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\32\u0090\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3"+
		"\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f"+
		"\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\26"+
		"\6\26\u0082\n\26\r\26\16\26\u0083\3\26\3\26\3\27\3\27\3\30\3\30\3\31\6"+
		"\31\u008d\n\31\r\31\16\31\u008e\2\2\32\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\3\2\4\4\2\13\13\"\"\5\2\62;C\\c|\u0091\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\3\63\3"+
		"\2\2\2\5\66\3\2\2\2\7>\3\2\2\2\tA\3\2\2\2\13M\3\2\2\2\rP\3\2\2\2\17S\3"+
		"\2\2\2\21U\3\2\2\2\23W\3\2\2\2\25Y\3\2\2\2\27`\3\2\2\2\31e\3\2\2\2\33"+
		"h\3\2\2\2\35k\3\2\2\2\37n\3\2\2\2!p\3\2\2\2#w\3\2\2\2%y\3\2\2\2\'{\3\2"+
		"\2\2)}\3\2\2\2+\u0081\3\2\2\2-\u0087\3\2\2\2/\u0089\3\2\2\2\61\u008c\3"+
		"\2\2\2\63\64\7d\2\2\64\65\7u\2\2\65\4\3\2\2\2\66\67\7e\2\2\678\7q\2\2"+
		"89\7w\2\29:\7p\2\2:;\7v\2\2;<\7t\2\2<=\7{\2\2=\6\3\2\2\2>?\7k\2\2?@\7"+
		"f\2\2@\b\3\2\2\2AB\7o\2\2BC\7g\2\2CD\7u\2\2DE\7u\2\2EF\7c\2\2FG\7i\2\2"+
		"GH\7g\2\2HI\7v\2\2IJ\7{\2\2JK\7r\2\2KL\7g\2\2L\n\3\2\2\2MN\7@\2\2NO\7"+
		"?\2\2O\f\3\2\2\2PQ\7\60\2\2QR\7\60\2\2R\16\3\2\2\2ST\7>\2\2T\20\3\2\2"+
		"\2UV\7@\2\2V\22\3\2\2\2WX\7B\2\2X\24\3\2\2\2YZ\7o\2\2Z[\7\60\2\2[\\\7"+
		"o\2\2\\]\7o\2\2]^\7u\2\2^_\7k\2\2_\26\3\2\2\2`a\7v\2\2ab\7{\2\2bc\7r\2"+
		"\2cd\7g\2\2d\30\3\2\2\2ef\7>\2\2fg\7?\2\2g\32\3\2\2\2hi\7K\2\2ij\7P\2"+
		"\2j\34\3\2\2\2kl\7k\2\2lm\7p\2\2m\36\3\2\2\2no\7*\2\2o \3\2\2\2pq\7t\2"+
		"\2qr\7g\2\2rs\7i\2\2st\7k\2\2tu\7q\2\2uv\7p\2\2v\"\3\2\2\2wx\7+\2\2x$"+
		"\3\2\2\2yz\7.\2\2z&\3\2\2\2{|\7?\2\2|(\3\2\2\2}~\7#\2\2~\177\7?\2\2\177"+
		"*\3\2\2\2\u0080\u0082\t\2\2\2\u0081\u0080\3\2\2\2\u0082\u0083\3\2\2\2"+
		"\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0086"+
		"\b\26\2\2\u0086,\3\2\2\2\u0087\u0088\7(\2\2\u0088.\3\2\2\2\u0089\u008a"+
		"\7~\2\2\u008a\60\3\2\2\2\u008b\u008d\t\3\2\2\u008c\u008b\3\2\2\2\u008d"+
		"\u008e\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f\62\3\2\2"+
		"\2\5\2\u0083\u008e\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}