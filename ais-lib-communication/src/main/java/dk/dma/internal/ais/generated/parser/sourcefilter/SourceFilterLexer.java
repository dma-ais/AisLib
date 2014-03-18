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
		T__9=1, T__8=2, T__7=3, T__6=4, T__5=5, T__4=6, T__3=7, T__2=8, T__1=9, 
		T__0=10, EQUALS=11, NEQUALS=12, WS=13, AND=14, OR=15, ID=16;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'bs'", "'country'", "'id'", "'messagetype'", "'type'", "'mmsi'", "'('", 
		"'region'", "')'", "','", "'='", "'!='", "WS", "'&'", "'|'", "ID"
	};
	public static final String[] ruleNames = {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\22i\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3"+
		"\16\6\16[\n\16\r\16\16\16\\\3\16\3\16\3\17\3\17\3\20\3\20\3\21\6\21f\n"+
		"\21\r\21\16\21g\2\2\22\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22\3\2\4\4\2\13\13\"\"\5\2\62;C\\c|j\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\3#\3\2\2\2\5"+
		"&\3\2\2\2\7.\3\2\2\2\t\61\3\2\2\2\13=\3\2\2\2\rB\3\2\2\2\17G\3\2\2\2\21"+
		"I\3\2\2\2\23P\3\2\2\2\25R\3\2\2\2\27T\3\2\2\2\31V\3\2\2\2\33Z\3\2\2\2"+
		"\35`\3\2\2\2\37b\3\2\2\2!e\3\2\2\2#$\7d\2\2$%\7u\2\2%\4\3\2\2\2&\'\7e"+
		"\2\2\'(\7q\2\2()\7w\2\2)*\7p\2\2*+\7v\2\2+,\7t\2\2,-\7{\2\2-\6\3\2\2\2"+
		"./\7k\2\2/\60\7f\2\2\60\b\3\2\2\2\61\62\7o\2\2\62\63\7g\2\2\63\64\7u\2"+
		"\2\64\65\7u\2\2\65\66\7c\2\2\66\67\7i\2\2\678\7g\2\289\7v\2\29:\7{\2\2"+
		":;\7r\2\2;<\7g\2\2<\n\3\2\2\2=>\7v\2\2>?\7{\2\2?@\7r\2\2@A\7g\2\2A\f\3"+
		"\2\2\2BC\7o\2\2CD\7o\2\2DE\7u\2\2EF\7k\2\2F\16\3\2\2\2GH\7*\2\2H\20\3"+
		"\2\2\2IJ\7t\2\2JK\7g\2\2KL\7i\2\2LM\7k\2\2MN\7q\2\2NO\7p\2\2O\22\3\2\2"+
		"\2PQ\7+\2\2Q\24\3\2\2\2RS\7.\2\2S\26\3\2\2\2TU\7?\2\2U\30\3\2\2\2VW\7"+
		"#\2\2WX\7?\2\2X\32\3\2\2\2Y[\t\2\2\2ZY\3\2\2\2[\\\3\2\2\2\\Z\3\2\2\2\\"+
		"]\3\2\2\2]^\3\2\2\2^_\b\16\2\2_\34\3\2\2\2`a\7(\2\2a\36\3\2\2\2bc\7~\2"+
		"\2c \3\2\2\2df\t\3\2\2ed\3\2\2\2fg\3\2\2\2ge\3\2\2\2gh\3\2\2\2h\"\3\2"+
		"\2\2\5\2\\g\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}