// Generated from SourceFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.sourcefilter;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SourceFilterParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__13=1, T__12=2, T__11=3, T__10=4, T__9=5, T__8=6, T__7=7, T__6=8, T__5=9, 
		T__4=10, T__3=11, T__2=12, T__1=13, T__0=14, EQUALS=15, NEQUALS=16, WS=17, 
		AND=18, OR=19, ID=20;
	public static final String[] tokenNames = {
		"<INVALID>", "'bs'", "'country'", "'id'", "'messagetype'", "'>='", "'<'", 
		"'>'", "'m.mmsi'", "'type'", "'<='", "'('", "'region'", "')'", "','", 
		"'='", "'!='", "WS", "'&'", "'|'", "ID"
	};
	public static final int
		RULE_prog = 0, RULE_expr = 1, RULE_equalityTest = 2, RULE_comparison = 3, 
		RULE_idList = 4;
	public static final String[] ruleNames = {
		"prog", "expr", "equalityTest", "comparison", "idList"
	};

	@Override
	public String getGrammarFileName() { return "SourceFilter.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SourceFilterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(SourceFilterParser.EOF, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10); expr(0);
			setState(11); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SourceIdContext extends ExprContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceIdContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceCountryContext extends ExprContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceCountryContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceCountry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParensContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceTypeContext extends ExprContext {
		public TerminalNode ID() { return getToken(SourceFilterParser.ID, 0); }
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceTypeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class M_mmsiContext extends ExprContext {
		public TerminalNode ID() { return getToken(SourceFilterParser.ID, 0); }
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public M_mmsiContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitM_mmsi(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceRegionContext extends ExprContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceRegionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceRegion(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrAndContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public OrAndContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitOrAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceBasestationContext extends ExprContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceBasestationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceBasestation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AisMessagetypeContext extends ExprContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public AisMessagetypeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitAisMessagetype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			switch (_input.LA(1)) {
			case 3:
				{
				_localctx = new SourceIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(14); match(3);
				setState(15); equalityTest();
				setState(16); idList();
				}
				break;
			case 1:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(18); match(1);
				setState(19); equalityTest();
				setState(20); idList();
				}
				break;
			case 2:
				{
				_localctx = new SourceCountryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(22); match(2);
				setState(23); equalityTest();
				setState(24); idList();
				}
				break;
			case 9:
				{
				_localctx = new SourceTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(26); match(9);
				setState(27); equalityTest();
				setState(28); match(ID);
				}
				break;
			case 12:
				{
				_localctx = new SourceRegionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(30); match(12);
				setState(31); equalityTest();
				setState(32); idList();
				}
				break;
			case 8:
				{
				_localctx = new M_mmsiContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(34); match(8);
				setState(35); comparison();
				setState(36); match(ID);
				}
				break;
			case 4:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(38); match(4);
				setState(39); equalityTest();
				setState(40); idList();
				}
				break;
			case 11:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(42); match(11);
				setState(43); expr(0);
				setState(44); match(13);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(53);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrAndContext(new ExprContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(48);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(49);
					((OrAndContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
						((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					consume();
					setState(50); expr(3);
					}
					} 
				}
				setState(55);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class EqualityTestContext extends ParserRuleContext {
		public EqualityTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityTest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitEqualityTest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualityTestContext equalityTest() throws RecognitionException {
		EqualityTestContext _localctx = new EqualityTestContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_equalityTest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			_la = _input.LA(1);
			if ( !(_la==EQUALS || _la==NEQUALS) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonContext extends ParserRuleContext {
		public ComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitComparison(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComparisonContext comparison() throws RecognitionException {
		ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_comparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 6) | (1L << 7) | (1L << 10) | (1L << EQUALS) | (1L << NEQUALS))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdListContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(SourceFilterParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SourceFilterParser.ID, i);
		}
		public IdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitIdList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdListContext idList() throws RecognitionException {
		IdListContext _localctx = new IdListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_idList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(60); match(ID);
			setState(65);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(61); match(14);
					setState(62); match(ID);
					}
					} 
				}
				setState(67);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\26G\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\61\n\3\3\3\3\3\3\3\7\3\66\n\3\f\3\16"+
		"\39\13\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\7\6B\n\6\f\6\16\6E\13\6\3\6\2\3\4"+
		"\7\2\4\6\b\n\2\5\3\2\24\25\3\2\21\22\5\2\7\t\f\f\21\22J\2\f\3\2\2\2\4"+
		"\60\3\2\2\2\6:\3\2\2\2\b<\3\2\2\2\n>\3\2\2\2\f\r\5\4\3\2\r\16\7\2\2\3"+
		"\16\3\3\2\2\2\17\20\b\3\1\2\20\21\7\5\2\2\21\22\5\6\4\2\22\23\5\n\6\2"+
		"\23\61\3\2\2\2\24\25\7\3\2\2\25\26\5\6\4\2\26\27\5\n\6\2\27\61\3\2\2\2"+
		"\30\31\7\4\2\2\31\32\5\6\4\2\32\33\5\n\6\2\33\61\3\2\2\2\34\35\7\13\2"+
		"\2\35\36\5\6\4\2\36\37\7\26\2\2\37\61\3\2\2\2 !\7\16\2\2!\"\5\6\4\2\""+
		"#\5\n\6\2#\61\3\2\2\2$%\7\n\2\2%&\5\b\5\2&\'\7\26\2\2\'\61\3\2\2\2()\7"+
		"\6\2\2)*\5\6\4\2*+\5\n\6\2+\61\3\2\2\2,-\7\r\2\2-.\5\4\3\2./\7\17\2\2"+
		"/\61\3\2\2\2\60\17\3\2\2\2\60\24\3\2\2\2\60\30\3\2\2\2\60\34\3\2\2\2\60"+
		" \3\2\2\2\60$\3\2\2\2\60(\3\2\2\2\60,\3\2\2\2\61\67\3\2\2\2\62\63\f\4"+
		"\2\2\63\64\t\2\2\2\64\66\5\4\3\5\65\62\3\2\2\2\669\3\2\2\2\67\65\3\2\2"+
		"\2\678\3\2\2\28\5\3\2\2\29\67\3\2\2\2:;\t\3\2\2;\7\3\2\2\2<=\t\4\2\2="+
		"\t\3\2\2\2>C\7\26\2\2?@\7\20\2\2@B\7\26\2\2A?\3\2\2\2BE\3\2\2\2CA\3\2"+
		"\2\2CD\3\2\2\2D\13\3\2\2\2EC\3\2\2\2\5\60\67C";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}