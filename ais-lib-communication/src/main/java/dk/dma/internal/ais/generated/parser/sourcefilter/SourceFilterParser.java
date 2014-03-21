// Generated from SourceFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.sourcefilter;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SourceFilterParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__26=1, T__25=2, T__24=3, T__23=4, T__22=5, T__21=6, T__20=7, T__19=8, 
		T__18=9, T__17=10, T__16=11, T__15=12, T__14=13, T__13=14, T__12=15, T__11=16, 
		T__10=17, T__9=18, T__8=19, T__7=20, T__6=21, T__5=22, T__4=23, T__3=24, 
		T__2=25, T__1=26, T__0=27, EQUALS=28, NEQUALS=29, WS=30, AND=31, OR=32, 
		INT=33, FLOAT=34, SIXBITCHAR=35, SIXBITSTRING=36;
	public static final String[] tokenNames = {
		"<INVALID>", "'s.type'", "'m.lat'", "'m.name'", "'..'", "'m.mmsi'", "'<='", 
		"'m.hdg'", "'s.country'", "'('", "','", "'m.cog'", "'messagetype'", "'m.draught'", 
		"'m.sog'", "'>='", "'<'", "'s.id'", "'s.region'", "'>'", "'@'", "'m.lon'", 
		"'IN'", "'in'", "'m.imo'", "'m.id'", "')'", "'s.bs'", "'='", "'!='", "WS", 
		"'&'", "'|'", "INT", "FLOAT", "SIXBITCHAR", "SIXBITSTRING"
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
	public static class MessageNameContext extends FilterExpressionContext {
		public ValueSpecContext valueSpec() {
			return getRuleContext(ValueSpecContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public MessageNameContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageName(this);
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
	public static class MessageLatitudeContext extends FilterExpressionContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SourceFilterParser.FLOAT, 0); }
		public MessageLatitudeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLatitude(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageHeadingContext extends FilterExpressionContext {
		public ValueSpecContext valueSpec() {
			return getRuleContext(ValueSpecContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public MessageHeadingContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageHeading(this);
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
	public static class MessageCourseOverGroundContext extends FilterExpressionContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SourceFilterParser.FLOAT, 0); }
		public MessageCourseOverGroundContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageCourseOverGround(this);
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
	public static class MessageLongitudeContext extends FilterExpressionContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SourceFilterParser.FLOAT, 0); }
		public MessageLongitudeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLongitude(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageDraughtContext extends FilterExpressionContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SourceFilterParser.FLOAT, 0); }
		public MessageDraughtContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageDraught(this);
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
	public static class MessageImoContext extends FilterExpressionContext {
		public ValueSpecContext valueSpec() {
			return getRuleContext(ValueSpecContext.class,0);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public MessageImoContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageImo(this);
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
			setState(92);
			switch (_input.LA(1)) {
			case 17:
				{
				_localctx = new SourceIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(24); match(17);
				setState(25); equalityTest();
				setState(26); valueList();
				}
				break;
			case 27:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28); match(27);
				setState(29); equalityTest();
				setState(30); valueList();
				}
				break;
			case 8:
				{
				_localctx = new SourceCountryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(32); match(8);
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
			case 18:
				{
				_localctx = new SourceRegionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(40); match(18);
				setState(41); equalityTest();
				setState(42); valueList();
				}
				break;
			case 25:
				{
				_localctx = new MessageIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(44); match(25);
				setState(45); operator();
				setState(46); valueSpec();
				}
				break;
			case 5:
				{
				_localctx = new MessageMmsiContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48); match(5);
				setState(49); operator();
				setState(50); valueSpec();
				}
				break;
			case 24:
				{
				_localctx = new MessageImoContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52); match(24);
				setState(53); operator();
				setState(54); valueSpec();
				}
				break;
			case 3:
				{
				_localctx = new MessageNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56); match(3);
				setState(57); operator();
				setState(58); valueSpec();
				}
				break;
			case 14:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(60); match(14);
				setState(61); comparison();
				setState(62);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==FLOAT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case 11:
				{
				_localctx = new MessageCourseOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64); match(11);
				setState(65); comparison();
				setState(66);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==FLOAT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case 7:
				{
				_localctx = new MessageHeadingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(68); match(7);
				setState(69); operator();
				setState(70); valueSpec();
				}
				break;
			case 21:
				{
				_localctx = new MessageLongitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(72); match(21);
				setState(73); comparison();
				setState(74);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==FLOAT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case 2:
				{
				_localctx = new MessageLatitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(76); match(2);
				setState(77); comparison();
				setState(78);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==FLOAT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case 13:
				{
				_localctx = new MessageDraughtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(80); match(13);
				setState(81); comparison();
				setState(82);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==FLOAT) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case 12:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84); match(12);
				setState(85); equalityTest();
				setState(86); valueList();
				}
				break;
			case 9:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(88); match(9);
				setState(89); filterExpression(0);
				setState(90); match(26);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(99);
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
					setState(94);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(95);
					((OrAndContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
						((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					consume();
					setState(96); filterExpression(3);
					}
					} 
				}
				setState(101);
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
			setState(102);
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
			setState(104);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 6) | (1L << 15) | (1L << 16) | (1L << 19) | (1L << EQUALS) | (1L << NEQUALS))) != 0)) ) {
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
			setState(106);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 20) | (1L << 22) | (1L << 23))) != 0)) ) {
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
			setState(110);
			switch (_input.LA(1)) {
			case 6:
			case 15:
			case 16:
			case 19:
			case EQUALS:
			case NEQUALS:
				{
				setState(108); comparison();
				}
				break;
			case 20:
			case 22:
			case 23:
				{
				setState(109); inListOrRange();
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
			setState(113);
			_la = _input.LA(1);
			if (_la==9) {
				{
				setState(112); match(9);
				}
			}

			setState(115); value();
			setState(120);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(116); match(10);
					setState(117); value();
					}
					} 
				}
				setState(122);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(124);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(123); match(26);
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
			setState(127);
			_la = _input.LA(1);
			if (_la==9) {
				{
				setState(126); match(9);
				}
			}

			setState(129); value();
			setState(130); match(4);
			setState(131); value();
			setState(133);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(132); match(26);
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
			setState(135);
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
			setState(140);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(137); value();
				}
				break;

			case 2:
				{
				setState(138); valueList();
				}
				break;

			case 3:
				{
				setState(139); valueRange();
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3&\u0091\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3_\n\3\3\3\3\3\3\3\7\3d\n\3\f\3\16\3g\13\3\3\4\3\4\3\5"+
		"\3\5\3\6\3\6\3\7\3\7\5\7q\n\7\3\b\5\bt\n\b\3\b\3\b\3\b\7\by\n\b\f\b\16"+
		"\b|\13\b\3\b\5\b\177\n\b\3\t\5\t\u0082\n\t\3\t\3\t\3\t\3\t\5\t\u0088\n"+
		"\t\3\n\3\n\3\13\3\13\3\13\5\13\u008f\n\13\3\13\2\3\4\f\2\4\6\b\n\f\16"+
		"\20\22\24\2\b\3\2#$\3\2!\"\3\2\36\37\6\2\b\b\21\22\25\25\36\37\4\2\26"+
		"\26\30\31\4\2#$&&\u009f\2\26\3\2\2\2\4^\3\2\2\2\6h\3\2\2\2\bj\3\2\2\2"+
		"\nl\3\2\2\2\fp\3\2\2\2\16s\3\2\2\2\20\u0081\3\2\2\2\22\u0089\3\2\2\2\24"+
		"\u008e\3\2\2\2\26\27\5\4\3\2\27\30\7\2\2\3\30\3\3\2\2\2\31\32\b\3\1\2"+
		"\32\33\7\23\2\2\33\34\5\6\4\2\34\35\5\16\b\2\35_\3\2\2\2\36\37\7\35\2"+
		"\2\37 \5\6\4\2 !\5\16\b\2!_\3\2\2\2\"#\7\n\2\2#$\5\6\4\2$%\5\16\b\2%_"+
		"\3\2\2\2&\'\7\3\2\2\'(\5\6\4\2()\5\16\b\2)_\3\2\2\2*+\7\24\2\2+,\5\6\4"+
		"\2,-\5\16\b\2-_\3\2\2\2./\7\33\2\2/\60\5\f\7\2\60\61\5\24\13\2\61_\3\2"+
		"\2\2\62\63\7\7\2\2\63\64\5\f\7\2\64\65\5\24\13\2\65_\3\2\2\2\66\67\7\32"+
		"\2\2\678\5\f\7\289\5\24\13\29_\3\2\2\2:;\7\5\2\2;<\5\f\7\2<=\5\24\13\2"+
		"=_\3\2\2\2>?\7\20\2\2?@\5\b\5\2@A\t\2\2\2A_\3\2\2\2BC\7\r\2\2CD\5\b\5"+
		"\2DE\t\2\2\2E_\3\2\2\2FG\7\t\2\2GH\5\f\7\2HI\5\24\13\2I_\3\2\2\2JK\7\27"+
		"\2\2KL\5\b\5\2LM\t\2\2\2M_\3\2\2\2NO\7\4\2\2OP\5\b\5\2PQ\t\2\2\2Q_\3\2"+
		"\2\2RS\7\17\2\2ST\5\b\5\2TU\t\2\2\2U_\3\2\2\2VW\7\16\2\2WX\5\6\4\2XY\5"+
		"\16\b\2Y_\3\2\2\2Z[\7\13\2\2[\\\5\4\3\2\\]\7\34\2\2]_\3\2\2\2^\31\3\2"+
		"\2\2^\36\3\2\2\2^\"\3\2\2\2^&\3\2\2\2^*\3\2\2\2^.\3\2\2\2^\62\3\2\2\2"+
		"^\66\3\2\2\2^:\3\2\2\2^>\3\2\2\2^B\3\2\2\2^F\3\2\2\2^J\3\2\2\2^N\3\2\2"+
		"\2^R\3\2\2\2^V\3\2\2\2^Z\3\2\2\2_e\3\2\2\2`a\f\4\2\2ab\t\3\2\2bd\5\4\3"+
		"\5c`\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2f\5\3\2\2\2ge\3\2\2\2hi\t\4"+
		"\2\2i\7\3\2\2\2jk\t\5\2\2k\t\3\2\2\2lm\t\6\2\2m\13\3\2\2\2nq\5\b\5\2o"+
		"q\5\n\6\2pn\3\2\2\2po\3\2\2\2q\r\3\2\2\2rt\7\13\2\2sr\3\2\2\2st\3\2\2"+
		"\2tu\3\2\2\2uz\5\22\n\2vw\7\f\2\2wy\5\22\n\2xv\3\2\2\2y|\3\2\2\2zx\3\2"+
		"\2\2z{\3\2\2\2{~\3\2\2\2|z\3\2\2\2}\177\7\34\2\2~}\3\2\2\2~\177\3\2\2"+
		"\2\177\17\3\2\2\2\u0080\u0082\7\13\2\2\u0081\u0080\3\2\2\2\u0081\u0082"+
		"\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0084\5\22\n\2\u0084\u0085\7\6\2\2"+
		"\u0085\u0087\5\22\n\2\u0086\u0088\7\34\2\2\u0087\u0086\3\2\2\2\u0087\u0088"+
		"\3\2\2\2\u0088\21\3\2\2\2\u0089\u008a\t\7\2\2\u008a\23\3\2\2\2\u008b\u008f"+
		"\5\22\n\2\u008c\u008f\5\16\b\2\u008d\u008f\5\20\t\2\u008e\u008b\3\2\2"+
		"\2\u008e\u008c\3\2\2\2\u008e\u008d\3\2\2\2\u008f\25\3\2\2\2\13^epsz~\u0081"+
		"\u0087\u008e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}