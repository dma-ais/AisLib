// Generated from SourceFilter.g4 by ANTLR 4.1
package dk.dma.ais.internal.parser.sourcefilter;
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
		T__7=1, T__6=2, T__5=3, T__4=4, T__3=5, T__2=6, T__1=7, T__0=8, EQUALS=9, 
		WS=10, AND=11, OR=12, INT=13, ID=14;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'id'", "'country'", "'region'", "','", "'type'", "')'", "'bs'", "'('", 
		"'='", "WS", "'&'", "'|'", "INT", "ID"
	};
	public static final String[] ruleNames = {
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "EQUALS", 
		"WS", "AND", "OR", "INT", "ID"
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 9: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\20W\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\6\13C\n\13\r\13\16\13D\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\16\6\16N\n\16\r\16\16\16O\3\17\3\17\6\17T\n\17"+
		"\r\17\16\17U\2\20\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23"+
		"\13\1\25\f\2\27\r\1\31\16\1\33\17\1\35\20\1\3\2\6\4\2\13\13\"\"\3\2\62"+
		";\4\2C\\c|\5\2\62;C\\c|Y\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2"+
		"\2\2\5\"\3\2\2\2\7*\3\2\2\2\t\61\3\2\2\2\13\63\3\2\2\2\r8\3\2\2\2\17:"+
		"\3\2\2\2\21=\3\2\2\2\23?\3\2\2\2\25B\3\2\2\2\27H\3\2\2\2\31J\3\2\2\2\33"+
		"M\3\2\2\2\35Q\3\2\2\2\37 \7k\2\2 !\7f\2\2!\4\3\2\2\2\"#\7e\2\2#$\7q\2"+
		"\2$%\7w\2\2%&\7p\2\2&\'\7v\2\2\'(\7t\2\2()\7{\2\2)\6\3\2\2\2*+\7t\2\2"+
		"+,\7g\2\2,-\7i\2\2-.\7k\2\2./\7q\2\2/\60\7p\2\2\60\b\3\2\2\2\61\62\7."+
		"\2\2\62\n\3\2\2\2\63\64\7v\2\2\64\65\7{\2\2\65\66\7r\2\2\66\67\7g\2\2"+
		"\67\f\3\2\2\289\7+\2\29\16\3\2\2\2:;\7d\2\2;<\7u\2\2<\20\3\2\2\2=>\7*"+
		"\2\2>\22\3\2\2\2?@\7?\2\2@\24\3\2\2\2AC\t\2\2\2BA\3\2\2\2CD\3\2\2\2DB"+
		"\3\2\2\2DE\3\2\2\2EF\3\2\2\2FG\b\13\2\2G\26\3\2\2\2HI\7(\2\2I\30\3\2\2"+
		"\2JK\7~\2\2K\32\3\2\2\2LN\t\3\2\2ML\3\2\2\2NO\3\2\2\2OM\3\2\2\2OP\3\2"+
		"\2\2P\34\3\2\2\2QS\t\4\2\2RT\t\5\2\2SR\3\2\2\2TU\3\2\2\2US\3\2\2\2UV\3"+
		"\2\2\2V\36\3\2\2\2\6\2DOU";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}