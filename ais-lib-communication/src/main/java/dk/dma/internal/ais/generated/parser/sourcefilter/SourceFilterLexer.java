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
		T__16=1, T__15=2, T__14=3, T__13=4, T__12=5, T__11=6, T__10=7, T__9=8, 
		T__8=9, T__7=10, T__6=11, T__5=12, T__4=13, T__3=14, T__2=15, T__1=16, 
		T__0=17, EQUALS=18, NEQUALS=19, WS=20, AND=21, OR=22, ID=23;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'bs'", "'country'", "'id'", "'messagetype'", "'>='", "'<'", "'>'", "'@'", 
		"'m.mmsi'", "'type'", "'<='", "'IN'", "'in'", "'('", "'region'", "')'", 
		"','", "'='", "'!='", "WS", "'&'", "'|'", "ID"
	};
	public static final String[] ruleNames = {
		"T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", "T__9", 
		"T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", 
		"EQUALS", "NEQUALS", "WS", "AND", "OR", "ID"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\31\u008b\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21"+
		"\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\25\6\25}\n\25\r\25\16\25~\3"+
		"\25\3\25\3\26\3\26\3\27\3\27\3\30\6\30\u0088\n\30\r\30\16\30\u0089\2\2"+
		"\31\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\3\2\4\4\2\13\13\"\"\5\2\62"+
		";C\\c|\u008c\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2"+
		"\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2"+
		"\2\2/\3\2\2\2\3\61\3\2\2\2\5\64\3\2\2\2\7<\3\2\2\2\t?\3\2\2\2\13K\3\2"+
		"\2\2\rN\3\2\2\2\17P\3\2\2\2\21R\3\2\2\2\23T\3\2\2\2\25[\3\2\2\2\27`\3"+
		"\2\2\2\31c\3\2\2\2\33f\3\2\2\2\35i\3\2\2\2\37k\3\2\2\2!r\3\2\2\2#t\3\2"+
		"\2\2%v\3\2\2\2\'x\3\2\2\2)|\3\2\2\2+\u0082\3\2\2\2-\u0084\3\2\2\2/\u0087"+
		"\3\2\2\2\61\62\7d\2\2\62\63\7u\2\2\63\4\3\2\2\2\64\65\7e\2\2\65\66\7q"+
		"\2\2\66\67\7w\2\2\678\7p\2\289\7v\2\29:\7t\2\2:;\7{\2\2;\6\3\2\2\2<=\7"+
		"k\2\2=>\7f\2\2>\b\3\2\2\2?@\7o\2\2@A\7g\2\2AB\7u\2\2BC\7u\2\2CD\7c\2\2"+
		"DE\7i\2\2EF\7g\2\2FG\7v\2\2GH\7{\2\2HI\7r\2\2IJ\7g\2\2J\n\3\2\2\2KL\7"+
		"@\2\2LM\7?\2\2M\f\3\2\2\2NO\7>\2\2O\16\3\2\2\2PQ\7@\2\2Q\20\3\2\2\2RS"+
		"\7B\2\2S\22\3\2\2\2TU\7o\2\2UV\7\60\2\2VW\7o\2\2WX\7o\2\2XY\7u\2\2YZ\7"+
		"k\2\2Z\24\3\2\2\2[\\\7v\2\2\\]\7{\2\2]^\7r\2\2^_\7g\2\2_\26\3\2\2\2`a"+
		"\7>\2\2ab\7?\2\2b\30\3\2\2\2cd\7K\2\2de\7P\2\2e\32\3\2\2\2fg\7k\2\2gh"+
		"\7p\2\2h\34\3\2\2\2ij\7*\2\2j\36\3\2\2\2kl\7t\2\2lm\7g\2\2mn\7i\2\2no"+
		"\7k\2\2op\7q\2\2pq\7p\2\2q \3\2\2\2rs\7+\2\2s\"\3\2\2\2tu\7.\2\2u$\3\2"+
		"\2\2vw\7?\2\2w&\3\2\2\2xy\7#\2\2yz\7?\2\2z(\3\2\2\2{}\t\2\2\2|{\3\2\2"+
		"\2}~\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177\u0080\3\2\2\2\u0080\u0081\b\25"+
		"\2\2\u0081*\3\2\2\2\u0082\u0083\7(\2\2\u0083,\3\2\2\2\u0084\u0085\7~\2"+
		"\2\u0085.\3\2\2\2\u0086\u0088\t\3\2\2\u0087\u0086\3\2\2\2\u0088\u0089"+
		"\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\60\3\2\2\2\5\2"+
		"~\u0089\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}