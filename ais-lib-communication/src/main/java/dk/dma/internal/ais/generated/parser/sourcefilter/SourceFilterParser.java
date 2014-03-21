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
		T__19=1, T__18=2, T__17=3, T__16=4, T__15=5, T__14=6, T__13=7, T__12=8, 
		T__11=9, T__10=10, T__9=11, T__8=12, T__7=13, T__6=14, T__5=15, T__4=16, 
		T__3=17, T__2=18, T__1=19, T__0=20, EQUALS=21, NEQUALS=22, WS=23, AND=24, 
		OR=25, INT=26, FLOAT=27, SIXBITCHAR=28, SIXBITSTRING=29;
	public static final String[] tokenNames = {
		"<INVALID>", "'s.type'", "'messagetype'", "'m.sog'", "'>='", "'..'", "'<'", 
		"'s.id'", "'s.region'", "'>'", "'@'", "'m.mmsi'", "'<='", "'IN'", "'in'", 
		"'m.id'", "'s.country'", "'('", "')'", "'s.bs'", "','", "'='", "'!='", 
		"WS", "'&'", "'|'", "INT", "FLOAT", "SIXBITCHAR", "SIXBITSTRING"
	};
	public static final int
		RULE_prog = 0, RULE_filterExpression = 1, RULE_equalityTest = 2, RULE_comparison = 3, 
		RULE_inListOrRange = 4, RULE_operator = 5, RULE_valueList = 6, RULE_valueRange = 7, 
		RULE_value = 8, RULE_valueSpec = 9;
	public static final String[] ruleNames = {
		"prog", "filterExpression", "equalityTest", "comparison", "inListOrRange", 
		"operator", "valueList", "valueRange", "value", "valueSpec"
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
		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class,0);
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
			setState(20); filterExpression(0);
			setState(21); match(EOF);
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

	public static class FilterExpressionContext extends ParserRuleContext {
		public FilterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filterExpression; }
	 
		public FilterExpressionContext() { }
		public void copyFrom(FilterExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SourceIdContext extends FilterExpressionContext {
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceIdContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceCountryContext extends FilterExpressionContext {
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceCountryContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceCountry(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensContext extends FilterExpressionContext {
		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class,0);
		}
		public ParensContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceTypeContext extends FilterExpressionContext {
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceTypeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceRegionContext extends FilterExpressionContext {
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceRegionContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceRegion(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdContext extends FilterExpressionContext {
		public ValueSpecContext valueSpec() {
			return getRuleContext(ValueSpecContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public MessageIdContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrAndContext extends FilterExpressionContext {
		public Token op;
		public List<FilterExpressionContext> filterExpression() {
			return getRuleContexts(FilterExpressionContext.class);
		}
		public FilterExpressionContext filterExpression(int i) {
			return getRuleContext(FilterExpressionContext.class,i);
		}
		public OrAndContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitOrAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageSpeedOverGroundContext extends FilterExpressionContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SourceFilterParser.FLOAT, 0); }
		public MessageSpeedOverGroundContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageSpeedOverGround(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceBasestationContext extends FilterExpressionContext {
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public SourceBasestationContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceBasestation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageMmsiContext extends FilterExpressionContext {
		public ValueSpecContext valueSpec() {
			return getRuleContext(ValueSpecContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public MessageMmsiContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageMmsi(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AisMessagetypeContext extends FilterExpressionContext {
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public AisMessagetypeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitAisMessagetype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterExpressionContext filterExpression() throws RecognitionException {
		return filterExpression(0);
	}

	private FilterExpressionContext filterExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FilterExpressionContext _localctx = new FilterExpressionContext(_ctx, _parentState);
		FilterExpressionContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_filterExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			switch (_input.LA(1)) {
			case 7:
				{
				_localctx = new SourceIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(24); match(7);
				setState(25); equalityTest();
				setState(26); valueList();
				}
				break;
			case 19:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28); match(19);
				setState(29); equalityTest();
				setState(30); valueList();
				}
				break;
			case 16:
				{
				_localctx = new SourceCountryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(32); match(16);
				setState(33); equalityTest();
				setState(34); valueList();
				}
				break;
			case 1:
				{
				_localctx = new SourceTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(36); match(1);
				setState(37); equalityTest();
				setState(38); valueList();
				}
				break;
			case 8:
				{
				_localctx = new SourceRegionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(40); match(8);
				setState(41); equalityTest();
				setState(42); valueList();
				}
				break;
			case 15:
				{
				_localctx = new MessageIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(44); match(15);
				setState(45); operator();
				setState(46); valueSpec();
				}
				break;
			case 11:
				{
				_localctx = new MessageMmsiContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48); match(11);
				setState(49); operator();
				setState(50); valueSpec();
				}
				break;
			case 3:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52); match(3);
				setState(53); comparison();
				setState(54);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==FLOAT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case 2:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56); match(2);
				setState(57); equalityTest();
				setState(58); valueList();
				}
				break;
			case 17:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(60); match(17);
				setState(61); filterExpression(0);
				setState(62); match(18);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(71);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrAndContext(new FilterExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_filterExpression);
					setState(66);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(67);
					((OrAndContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
						((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					consume();
					setState(68); filterExpression(3);
					}
					} 
				}
				setState(73);
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
			setState(74);
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
			setState(76);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 6) | (1L << 9) | (1L << 12) | (1L << EQUALS) | (1L << NEQUALS))) != 0)) ) {
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
			setState(78);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 10) | (1L << 13) | (1L << 14))) != 0)) ) {
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

	public static class OperatorContext extends ParserRuleContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_operator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			switch (_input.LA(1)) {
			case 4:
			case 6:
			case 9:
			case 12:
			case EQUALS:
			case NEQUALS:
				{
				setState(80); comparison();
				}
				break;
			case 10:
			case 13:
			case 14:
				{
				setState(81); inListOrRange();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class ValueListContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ValueListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitValueList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueListContext valueList() throws RecognitionException {
		ValueListContext _localctx = new ValueListContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_valueList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_la = _input.LA(1);
			if (_la==17) {
				{
				setState(84); match(17);
				}
			}

			setState(87); value();
			setState(92);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(88); match(20);
					setState(89); value();
					}
					} 
				}
				setState(94);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(96);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(95); match(18);
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

	public static class ValueRangeContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ValueRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitValueRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueRangeContext valueRange() throws RecognitionException {
		ValueRangeContext _localctx = new ValueRangeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_valueRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			_la = _input.LA(1);
			if (_la==17) {
				{
				setState(98); match(17);
				}
			}

			setState(101); value();
			setState(102); match(5);
			setState(103); value();
			setState(105);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(104); match(18);
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

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode SIXBITSTRING() { return getToken(SourceFilterParser.SIXBITSTRING, 0); }
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SourceFilterParser.FLOAT, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << FLOAT) | (1L << SIXBITSTRING))) != 0)) ) {
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

	public static class ValueSpecContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public ValueRangeContext valueRange() {
			return getRuleContext(ValueRangeContext.class,0);
		}
		public ValueSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitValueSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueSpecContext valueSpec() throws RecognitionException {
		ValueSpecContext _localctx = new ValueSpecContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_valueSpec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(109); value();
				}
				break;

			case 2:
				{
				setState(110); valueList();
				}
				break;

			case 3:
				{
				setState(111); valueRange();
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
		case 1: return filterExpression_sempred((FilterExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean filterExpression_sempred(FilterExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\37u\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3C\n\3\3\3\3\3\3\3\7\3H\n\3\f\3\16"+
		"\3K\13\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\5\7U\n\7\3\b\5\bX\n\b\3\b\3\b"+
		"\3\b\7\b]\n\b\f\b\16\b`\13\b\3\b\5\bc\n\b\3\t\5\tf\n\t\3\t\3\t\3\t\3\t"+
		"\5\tl\n\t\3\n\3\n\3\13\3\13\3\13\5\13s\n\13\3\13\2\3\4\f\2\4\6\b\n\f\16"+
		"\20\22\24\2\b\3\2\34\35\3\2\32\33\3\2\27\30\7\2\6\6\b\b\13\13\16\16\27"+
		"\30\4\2\f\f\17\20\4\2\34\35\37\37|\2\26\3\2\2\2\4B\3\2\2\2\6L\3\2\2\2"+
		"\bN\3\2\2\2\nP\3\2\2\2\fT\3\2\2\2\16W\3\2\2\2\20e\3\2\2\2\22m\3\2\2\2"+
		"\24r\3\2\2\2\26\27\5\4\3\2\27\30\7\2\2\3\30\3\3\2\2\2\31\32\b\3\1\2\32"+
		"\33\7\t\2\2\33\34\5\6\4\2\34\35\5\16\b\2\35C\3\2\2\2\36\37\7\25\2\2\37"+
		" \5\6\4\2 !\5\16\b\2!C\3\2\2\2\"#\7\22\2\2#$\5\6\4\2$%\5\16\b\2%C\3\2"+
		"\2\2&\'\7\3\2\2\'(\5\6\4\2()\5\16\b\2)C\3\2\2\2*+\7\n\2\2+,\5\6\4\2,-"+
		"\5\16\b\2-C\3\2\2\2./\7\21\2\2/\60\5\f\7\2\60\61\5\24\13\2\61C\3\2\2\2"+
		"\62\63\7\r\2\2\63\64\5\f\7\2\64\65\5\24\13\2\65C\3\2\2\2\66\67\7\5\2\2"+
		"\678\5\b\5\289\t\2\2\29C\3\2\2\2:;\7\4\2\2;<\5\6\4\2<=\5\16\b\2=C\3\2"+
		"\2\2>?\7\23\2\2?@\5\4\3\2@A\7\24\2\2AC\3\2\2\2B\31\3\2\2\2B\36\3\2\2\2"+
		"B\"\3\2\2\2B&\3\2\2\2B*\3\2\2\2B.\3\2\2\2B\62\3\2\2\2B\66\3\2\2\2B:\3"+
		"\2\2\2B>\3\2\2\2CI\3\2\2\2DE\f\4\2\2EF\t\3\2\2FH\5\4\3\5GD\3\2\2\2HK\3"+
		"\2\2\2IG\3\2\2\2IJ\3\2\2\2J\5\3\2\2\2KI\3\2\2\2LM\t\4\2\2M\7\3\2\2\2N"+
		"O\t\5\2\2O\t\3\2\2\2PQ\t\6\2\2Q\13\3\2\2\2RU\5\b\5\2SU\5\n\6\2TR\3\2\2"+
		"\2TS\3\2\2\2U\r\3\2\2\2VX\7\23\2\2WV\3\2\2\2WX\3\2\2\2XY\3\2\2\2Y^\5\22"+
		"\n\2Z[\7\26\2\2[]\5\22\n\2\\Z\3\2\2\2]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_"+
		"b\3\2\2\2`^\3\2\2\2ac\7\24\2\2ba\3\2\2\2bc\3\2\2\2c\17\3\2\2\2df\7\23"+
		"\2\2ed\3\2\2\2ef\3\2\2\2fg\3\2\2\2gh\5\22\n\2hi\7\7\2\2ik\5\22\n\2jl\7"+
		"\24\2\2kj\3\2\2\2kl\3\2\2\2l\21\3\2\2\2mn\t\7\2\2n\23\3\2\2\2os\5\22\n"+
		"\2ps\5\16\b\2qs\5\20\t\2ro\3\2\2\2rp\3\2\2\2rq\3\2\2\2s\25\3\2\2\2\13"+
		"BITW^bekr";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}