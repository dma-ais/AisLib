// Generated from SourceFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.sourcefilter;
import org.antlr.v4.runtime.FailedPredicateException;
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
		T__17=1, T__16=2, T__15=3, T__14=4, T__13=5, T__12=6, T__11=7, T__10=8, 
		T__9=9, T__8=10, T__7=11, T__6=12, T__5=13, T__4=14, T__3=15, T__2=16, 
		T__1=17, T__0=18, EQUALS=19, NEQUALS=20, WS=21, AND=22, OR=23, ID=24;
	public static final String[] tokenNames = {
		"<INVALID>", "'bs'", "'country'", "'id'", "'messagetype'", "'>='", "'..'", 
		"'<'", "'>'", "'@'", "'m.mmsi'", "'type'", "'<='", "'IN'", "'in'", "'('", 
		"'region'", "')'", "','", "'='", "'!='", "WS", "'&'", "'|'", "ID"
	};
	public static final int
		RULE_prog = 0, RULE_expr = 1, RULE_equalityTest = 2, RULE_comparison = 3, 
		RULE_inListOrRange = 4, RULE_idList = 5, RULE_idRange = 6;
	public static final String[] ruleNames = {
		"prog", "expr", "equalityTest", "comparison", "inListOrRange", "idList", 
		"idRange"
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
			setState(14); expr(0);
			setState(15); match(EOF);
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
	public static class ComparesToIntContext extends ExprContext {
		public TerminalNode ID() { return getToken(SourceFilterParser.ID, 0); }
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public ComparesToIntContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitComparesToInt(this);
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
	public static class InIntListContext extends ExprContext {
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public InIntListContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitInIntList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class InIntRangeContext extends ExprContext {
		public IdRangeContext idRange() {
			return getRuleContext(IdRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public InIntRangeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitInIntRange(this);
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
			setState(58);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				_localctx = new SourceIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(18); match(3);
				setState(19); equalityTest();
				setState(20); idList();
				}
				break;

			case 2:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(22); match(1);
				setState(23); equalityTest();
				setState(24); idList();
				}
				break;

			case 3:
				{
				_localctx = new SourceCountryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(26); match(2);
				setState(27); equalityTest();
				setState(28); idList();
				}
				break;

			case 4:
				{
				_localctx = new SourceTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(30); match(11);
				setState(31); equalityTest();
				setState(32); match(ID);
				}
				break;

			case 5:
				{
				_localctx = new SourceRegionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(34); match(16);
				setState(35); equalityTest();
				setState(36); idList();
				}
				break;

			case 6:
				{
				_localctx = new ComparesToIntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(38); match(10);
				setState(39); comparison();
				setState(40); match(ID);
				}
				break;

			case 7:
				{
				_localctx = new InIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(42); match(10);
				setState(43); inListOrRange();
				setState(44); idList();
				}
				break;

			case 8:
				{
				_localctx = new InIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(46); match(10);
				setState(47); inListOrRange();
				setState(48); idRange();
				}
				break;

			case 9:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(50); match(4);
				setState(51); equalityTest();
				setState(52); idList();
				}
				break;

			case 10:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(54); match(15);
				setState(55); expr(0);
				setState(56); match(17);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(65);
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
					setState(60);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(61);
					((OrAndContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
						((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					consume();
					setState(62); expr(3);
					}
					} 
				}
				setState(67);
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
			setState(68);
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
			setState(70);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 8) | (1L << 12) | (1L << EQUALS) | (1L << NEQUALS))) != 0)) ) {
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

	public static class InListOrRangeContext extends ParserRuleContext {
		public InListOrRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inListOrRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitInListOrRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InListOrRangeContext inListOrRange() throws RecognitionException {
		InListOrRangeContext _localctx = new InListOrRangeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_inListOrRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 9) | (1L << 13) | (1L << 14))) != 0)) ) {
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
		enterRule(_localctx, 10, RULE_idList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			_la = _input.LA(1);
			if (_la==15) {
				{
				setState(74); match(15);
				}
			}

			setState(77); match(ID);
			setState(82);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(78); match(18);
					setState(79); match(ID);
					}
					} 
				}
				setState(84);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(86);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(85); match(17);
				}
				break;
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

	public static class IdRangeContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(SourceFilterParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SourceFilterParser.ID, i);
		}
		public IdRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitIdRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdRangeContext idRange() throws RecognitionException {
		IdRangeContext _localctx = new IdRangeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_idRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			_la = _input.LA(1);
			if (_la==15) {
				{
				setState(88); match(15);
				}
			}

			setState(91); match(ID);
			setState(92); match(6);
			setState(93); match(ID);
			setState(95);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(94); match(17);
				}
				break;
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\32d\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\2\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3=\n\3\3\3\3\3\3\3\7\3B\n\3\f\3\16\3E\13\3\3\4\3\4\3\5\3\5"+
		"\3\6\3\6\3\7\5\7N\n\7\3\7\3\7\3\7\7\7S\n\7\f\7\16\7V\13\7\3\7\5\7Y\n\7"+
		"\3\b\5\b\\\n\b\3\b\3\b\3\b\3\b\5\bb\n\b\3\b\2\3\4\t\2\4\6\b\n\f\16\2\6"+
		"\3\2\30\31\3\2\25\26\6\2\7\7\t\n\16\16\25\26\4\2\13\13\17\20k\2\20\3\2"+
		"\2\2\4<\3\2\2\2\6F\3\2\2\2\bH\3\2\2\2\nJ\3\2\2\2\fM\3\2\2\2\16[\3\2\2"+
		"\2\20\21\5\4\3\2\21\22\7\2\2\3\22\3\3\2\2\2\23\24\b\3\1\2\24\25\7\5\2"+
		"\2\25\26\5\6\4\2\26\27\5\f\7\2\27=\3\2\2\2\30\31\7\3\2\2\31\32\5\6\4\2"+
		"\32\33\5\f\7\2\33=\3\2\2\2\34\35\7\4\2\2\35\36\5\6\4\2\36\37\5\f\7\2\37"+
		"=\3\2\2\2 !\7\r\2\2!\"\5\6\4\2\"#\7\32\2\2#=\3\2\2\2$%\7\22\2\2%&\5\6"+
		"\4\2&\'\5\f\7\2\'=\3\2\2\2()\7\f\2\2)*\5\b\5\2*+\7\32\2\2+=\3\2\2\2,-"+
		"\7\f\2\2-.\5\n\6\2./\5\f\7\2/=\3\2\2\2\60\61\7\f\2\2\61\62\5\n\6\2\62"+
		"\63\5\16\b\2\63=\3\2\2\2\64\65\7\6\2\2\65\66\5\6\4\2\66\67\5\f\7\2\67"+
		"=\3\2\2\289\7\21\2\29:\5\4\3\2:;\7\23\2\2;=\3\2\2\2<\23\3\2\2\2<\30\3"+
		"\2\2\2<\34\3\2\2\2< \3\2\2\2<$\3\2\2\2<(\3\2\2\2<,\3\2\2\2<\60\3\2\2\2"+
		"<\64\3\2\2\2<8\3\2\2\2=C\3\2\2\2>?\f\4\2\2?@\t\2\2\2@B\5\4\3\5A>\3\2\2"+
		"\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\5\3\2\2\2EC\3\2\2\2FG\t\3\2\2G\7\3\2"+
		"\2\2HI\t\4\2\2I\t\3\2\2\2JK\t\5\2\2K\13\3\2\2\2LN\7\21\2\2ML\3\2\2\2M"+
		"N\3\2\2\2NO\3\2\2\2OT\7\32\2\2PQ\7\24\2\2QS\7\32\2\2RP\3\2\2\2SV\3\2\2"+
		"\2TR\3\2\2\2TU\3\2\2\2UX\3\2\2\2VT\3\2\2\2WY\7\23\2\2XW\3\2\2\2XY\3\2"+
		"\2\2Y\r\3\2\2\2Z\\\7\21\2\2[Z\3\2\2\2[\\\3\2\2\2\\]\3\2\2\2]^\7\32\2\2"+
		"^_\7\b\2\2_a\7\32\2\2`b\7\23\2\2a`\3\2\2\2ab\3\2\2\2b\17\3\2\2\2\t<CM"+
		"TX[a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}