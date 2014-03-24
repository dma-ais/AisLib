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
		T__28=1, T__27=2, T__26=3, T__25=4, T__24=5, T__23=6, T__22=7, T__21=8, 
		T__20=9, T__19=10, T__18=11, T__17=12, T__16=13, T__15=14, T__14=15, T__13=16, 
		T__12=17, T__11=18, T__10=19, T__9=20, T__8=21, T__7=22, T__6=23, T__5=24, 
		T__4=25, T__3=26, T__2=27, T__1=28, T__0=29, AND=30, OR=31, RANGE=32, 
		INT=33, FLOAT=34, WORD=35, STRING=36, WS=37;
	public static final String[] tokenNames = {
		"<INVALID>", "'s.type'", "'m.lat'", "'m.name'", "'!='", "'='", "'m.mmsi'", 
		"'<='", "'m.hdg'", "'s.country'", "'('", "','", "'m.cog'", "'messagetype'", 
		"'m.draught'", "'m.sog'", "'>='", "'<'", "'s.id'", "'s.region'", "'>'", 
		"'@'", "'m.cs'", "'m.lon'", "'IN'", "'in'", "'m.imo'", "'m.id'", "')'", 
		"'s.bs'", "'&'", "'|'", "'..'", "INT", "FLOAT", "WORD", "STRING", "WS"
	};
	public static final int
		RULE_prog = 0, RULE_filterExpression = 1, RULE_equalityTest = 2, RULE_value = 3, 
		RULE_valueList = 4, RULE_compareTo = 5, RULE_inListOrRange = 6, RULE_complies = 7, 
		RULE_intList = 8, RULE_stringList = 9, RULE_intRange = 10, RULE_numberRange = 11, 
		RULE_number = 12, RULE_string = 13;
	public static final String[] ruleNames = {
		"prog", "filterExpression", "equalityTest", "value", "valueList", "compareTo", 
		"inListOrRange", "complies", "intList", "stringList", "intRange", "numberRange", 
		"number", "string"
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
			setState(28); filterExpression(0);
			setState(29); match(EOF);
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
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public MessageNameContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageTrueHeadingContext extends FilterExpressionContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public MessageTrueHeadingContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageTrueHeading(this);
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
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public MessageLatitudeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLatitude(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageImoInRangeContext extends FilterExpressionContext {
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageImoInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageImoInRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNameInListContext extends FilterExpressionContext {
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageNameInListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageNameInList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdInRangeContext extends FilterExpressionContext {
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageIdInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageIdInRange(this);
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
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public MessageCourseOverGroundContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageCourseOverGround(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageSpeedOverGroundInRangeContext extends FilterExpressionContext {
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageSpeedOverGroundInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageSpeedOverGroundInRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrAndContext extends FilterExpressionContext {
		public Token op;
		public TerminalNode AND(int i) {
			return getToken(SourceFilterParser.AND, i);
		}
		public List<FilterExpressionContext> filterExpression() {
			return getRuleContexts(FilterExpressionContext.class);
		}
		public FilterExpressionContext filterExpression(int i) {
			return getRuleContext(FilterExpressionContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(SourceFilterParser.AND); }
		public List<TerminalNode> OR() { return getTokens(SourceFilterParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(SourceFilterParser.OR, i);
		}
		public OrAndContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitOrAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageMmsiInListContext extends FilterExpressionContext {
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageMmsiInListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageMmsiInList(this);
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
	public static class MessageLongitudeInRangeContext extends FilterExpressionContext {
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageLongitudeInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLongitudeInRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageCallsignInListContext extends FilterExpressionContext {
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageCallsignInListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageCallsignInList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageCourseOverGroundInRangeContext extends FilterExpressionContext {
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageCourseOverGroundInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageCourseOverGroundInRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public MessageIdContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageCallsignContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public MessageCallsignContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageCallsign(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageSpeedOverGroundContext extends FilterExpressionContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public MessageSpeedOverGroundContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageSpeedOverGround(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageLatitudeInRangeContext extends FilterExpressionContext {
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageLatitudeInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLatitudeInRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageMmsiContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public MessageMmsiContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageMmsi(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageLongitudeContext extends FilterExpressionContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public MessageLongitudeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLongitude(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageDraughtContext extends FilterExpressionContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
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
	public static class MessageImoInListContext extends FilterExpressionContext {
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageImoInListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageImoInList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdInListContext extends FilterExpressionContext {
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageIdInListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageIdInList(this);
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
	public static class MessageTrueHeadingInRangeContext extends FilterExpressionContext {
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageTrueHeadingInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageTrueHeadingInRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageImoContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public MessageImoContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageImo(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageMmsiInRangeContext extends FilterExpressionContext {
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageMmsiInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageMmsiInRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageDraughtInRangeContext extends FilterExpressionContext {
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageDraughtInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageDraughtInRange(this);
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
			setState(160);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				_localctx = new SourceIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(32); match(18);
				setState(33); equalityTest();
				setState(34); valueList();
				}
				break;

			case 2:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(36); match(29);
				setState(37); equalityTest();
				setState(38); valueList();
				}
				break;

			case 3:
				{
				_localctx = new SourceCountryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(40); match(9);
				setState(41); equalityTest();
				setState(42); valueList();
				}
				break;

			case 4:
				{
				_localctx = new SourceTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(44); match(1);
				setState(45); equalityTest();
				setState(46); valueList();
				}
				break;

			case 5:
				{
				_localctx = new SourceRegionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48); match(19);
				setState(49); equalityTest();
				setState(50); valueList();
				}
				break;

			case 6:
				{
				_localctx = new MessageIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52); match(27);
				setState(53); compareTo();
				setState(54); match(INT);
				}
				break;

			case 7:
				{
				_localctx = new MessageIdInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56); match(27);
				setState(57); inListOrRange();
				setState(58); intRange();
				}
				break;

			case 8:
				{
				_localctx = new MessageIdInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(60); match(27);
				setState(61); inListOrRange();
				setState(62); intList();
				}
				break;

			case 9:
				{
				_localctx = new MessageMmsiContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64); match(6);
				setState(65); compareTo();
				setState(66); match(INT);
				}
				break;

			case 10:
				{
				_localctx = new MessageMmsiInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(68); match(6);
				setState(69); inListOrRange();
				setState(70); intRange();
				}
				break;

			case 11:
				{
				_localctx = new MessageMmsiInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(72); match(6);
				setState(73); inListOrRange();
				setState(74); intList();
				}
				break;

			case 12:
				{
				_localctx = new MessageImoContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(76); match(26);
				setState(77); compareTo();
				setState(78); match(INT);
				}
				break;

			case 13:
				{
				_localctx = new MessageImoInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(80); match(26);
				setState(81); inListOrRange();
				setState(82); intRange();
				}
				break;

			case 14:
				{
				_localctx = new MessageImoInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84); match(26);
				setState(85); inListOrRange();
				setState(86); intList();
				}
				break;

			case 15:
				{
				_localctx = new MessageNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(88); match(3);
				setState(89); compareTo();
				setState(90); string();
				}
				break;

			case 16:
				{
				_localctx = new MessageNameInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(92); match(3);
				setState(93); inListOrRange();
				setState(94); stringList();
				}
				break;

			case 17:
				{
				_localctx = new MessageCallsignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(96); match(22);
				setState(97); compareTo();
				setState(98); string();
				}
				break;

			case 18:
				{
				_localctx = new MessageCallsignInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100); match(22);
				setState(101); inListOrRange();
				setState(102); stringList();
				}
				break;

			case 19:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(104); match(15);
				setState(105); compareTo();
				setState(106); number();
				}
				break;

			case 20:
				{
				_localctx = new MessageSpeedOverGroundInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(108); match(15);
				setState(109); inListOrRange();
				setState(110); numberRange();
				}
				break;

			case 21:
				{
				_localctx = new MessageCourseOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(112); match(12);
				setState(113); compareTo();
				setState(114); number();
				}
				break;

			case 22:
				{
				_localctx = new MessageCourseOverGroundInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(116); match(12);
				setState(117); inListOrRange();
				setState(118); numberRange();
				}
				break;

			case 23:
				{
				_localctx = new MessageLatitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(120); match(2);
				setState(121); compareTo();
				setState(122); number();
				}
				break;

			case 24:
				{
				_localctx = new MessageLatitudeInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(124); match(2);
				setState(125); inListOrRange();
				setState(126); numberRange();
				}
				break;

			case 25:
				{
				_localctx = new MessageLongitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(128); match(23);
				setState(129); compareTo();
				setState(130); number();
				}
				break;

			case 26:
				{
				_localctx = new MessageLongitudeInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(132); match(23);
				setState(133); inListOrRange();
				setState(134); numberRange();
				}
				break;

			case 27:
				{
				_localctx = new MessageTrueHeadingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(136); match(8);
				setState(137); compareTo();
				setState(138); number();
				}
				break;

			case 28:
				{
				_localctx = new MessageTrueHeadingInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(140); match(8);
				setState(141); inListOrRange();
				setState(142); numberRange();
				}
				break;

			case 29:
				{
				_localctx = new MessageDraughtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(144); match(14);
				setState(145); compareTo();
				setState(146); number();
				}
				break;

			case 30:
				{
				_localctx = new MessageDraughtInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(148); match(14);
				setState(149); inListOrRange();
				setState(150); numberRange();
				}
				break;

			case 31:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(152); match(13);
				setState(153); equalityTest();
				setState(154); valueList();
				}
				break;

			case 32:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(156); match(10);
				setState(157); filterExpression(0);
				setState(158); match(28);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(171);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrAndContext(new FilterExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_filterExpression);
					setState(162);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(165); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(163);
							((OrAndContext)_localctx).op = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==AND || _la==OR) ) {
								((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
							}
							consume();
							setState(164); filterExpression(0);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(167); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					} while ( _alt!=2 && _alt!=-1 );
					}
					} 
				}
				setState(173);
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
			setState(174);
			_la = _input.LA(1);
			if ( !(_la==4 || _la==5) ) {
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

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(SourceFilterParser.WORD, 0); }
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
		enterRule(_localctx, 6, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << FLOAT) | (1L << WORD))) != 0)) ) {
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
		enterRule(_localctx, 8, RULE_valueList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(178); match(10);
				}
			}

			setState(181); value();
			setState(186);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(182); match(11);
					setState(183); value();
					}
					} 
				}
				setState(188);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(190);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(189); match(28);
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

	public static class CompareToContext extends ParserRuleContext {
		public CompareToContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compareTo; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitCompareTo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompareToContext compareTo() throws RecognitionException {
		CompareToContext _localctx = new CompareToContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_compareTo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 5) | (1L << 7) | (1L << 16) | (1L << 17) | (1L << 20))) != 0)) ) {
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
		enterRule(_localctx, 12, RULE_inListOrRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 21) | (1L << 24) | (1L << 25))) != 0)) ) {
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

	public static class CompliesContext extends ParserRuleContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public CompliesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complies; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitComplies(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompliesContext complies() throws RecognitionException {
		CompliesContext _localctx = new CompliesContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_complies);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			switch (_input.LA(1)) {
			case 4:
			case 5:
			case 7:
			case 16:
			case 17:
			case 20:
				{
				setState(196); compareTo();
				}
				break;
			case 21:
			case 24:
			case 25:
				{
				setState(197); inListOrRange();
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

	public static class IntListContext extends ParserRuleContext {
		public TerminalNode INT(int i) {
			return getToken(SourceFilterParser.INT, i);
		}
		public List<TerminalNode> INT() { return getTokens(SourceFilterParser.INT); }
		public IntListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitIntList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntListContext intList() throws RecognitionException {
		IntListContext _localctx = new IntListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_intList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(200); match(10);
				}
			}

			setState(203); match(INT);
			setState(208);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(204); match(11);
					setState(205); match(INT);
					}
					} 
				}
				setState(210);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(212);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(211); match(28);
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

	public static class StringListContext extends ParserRuleContext {
		public StringContext string(int i) {
			return getRuleContext(StringContext.class,i);
		}
		public List<StringContext> string() {
			return getRuleContexts(StringContext.class);
		}
		public StringListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitStringList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringListContext stringList() throws RecognitionException {
		StringListContext _localctx = new StringListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_stringList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(214); match(10);
				}
			}

			setState(217); string();
			setState(222);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(218); match(11);
					setState(219); string();
					}
					} 
				}
				setState(224);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(226);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(225); match(28);
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

	public static class IntRangeContext extends ParserRuleContext {
		public TerminalNode INT(int i) {
			return getToken(SourceFilterParser.INT, i);
		}
		public TerminalNode RANGE() { return getToken(SourceFilterParser.RANGE, 0); }
		public List<TerminalNode> INT() { return getTokens(SourceFilterParser.INT); }
		public IntRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitIntRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntRangeContext intRange() throws RecognitionException {
		IntRangeContext _localctx = new IntRangeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_intRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(228); match(10);
				}
			}

			setState(231); match(INT);
			setState(232); match(RANGE);
			setState(233); match(INT);
			setState(235);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(234); match(28);
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

	public static class NumberRangeContext extends ParserRuleContext {
		public List<NumberContext> number() {
			return getRuleContexts(NumberContext.class);
		}
		public TerminalNode RANGE() { return getToken(SourceFilterParser.RANGE, 0); }
		public NumberContext number(int i) {
			return getRuleContext(NumberContext.class,i);
		}
		public NumberRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitNumberRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberRangeContext numberRange() throws RecognitionException {
		NumberRangeContext _localctx = new NumberRangeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_numberRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(237); match(10);
				}
			}

			setState(240); number();
			setState(241); match(RANGE);
			setState(242); number();
			setState(244);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(243); match(28);
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

	public static class NumberContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SourceFilterParser.FLOAT, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==FLOAT) ) {
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

	public static class StringContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode WORD() { return getToken(SourceFilterParser.WORD, 0); }
		public TerminalNode STRING() { return getToken(SourceFilterParser.STRING, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
				{
				setState(248); number();
				}
				break;
			case WORD:
				{
				setState(249); match(WORD);
				}
				break;
			case STRING:
				{
				setState(250); match(STRING);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\'\u0100\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3\u00a3\n\3\3\3\3\3\3\3\6\3\u00a8\n\3\r\3\16\3\u00a9\7\3\u00ac"+
		"\n\3\f\3\16\3\u00af\13\3\3\4\3\4\3\5\3\5\3\6\5\6\u00b6\n\6\3\6\3\6\3\6"+
		"\7\6\u00bb\n\6\f\6\16\6\u00be\13\6\3\6\5\6\u00c1\n\6\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\5\t\u00c9\n\t\3\n\5\n\u00cc\n\n\3\n\3\n\3\n\7\n\u00d1\n\n\f\n\16"+
		"\n\u00d4\13\n\3\n\5\n\u00d7\n\n\3\13\5\13\u00da\n\13\3\13\3\13\3\13\7"+
		"\13\u00df\n\13\f\13\16\13\u00e2\13\13\3\13\5\13\u00e5\n\13\3\f\5\f\u00e8"+
		"\n\f\3\f\3\f\3\f\3\f\5\f\u00ee\n\f\3\r\5\r\u00f1\n\r\3\r\3\r\3\r\3\r\5"+
		"\r\u00f7\n\r\3\16\3\16\3\17\3\17\3\17\5\17\u00fe\n\17\3\17\2\3\4\20\2"+
		"\4\6\b\n\f\16\20\22\24\26\30\32\34\2\b\3\2 !\3\2\6\7\3\2#%\6\2\6\7\t\t"+
		"\22\23\26\26\4\2\27\27\32\33\3\2#$\u0122\2\36\3\2\2\2\4\u00a2\3\2\2\2"+
		"\6\u00b0\3\2\2\2\b\u00b2\3\2\2\2\n\u00b5\3\2\2\2\f\u00c2\3\2\2\2\16\u00c4"+
		"\3\2\2\2\20\u00c8\3\2\2\2\22\u00cb\3\2\2\2\24\u00d9\3\2\2\2\26\u00e7\3"+
		"\2\2\2\30\u00f0\3\2\2\2\32\u00f8\3\2\2\2\34\u00fd\3\2\2\2\36\37\5\4\3"+
		"\2\37 \7\2\2\3 \3\3\2\2\2!\"\b\3\1\2\"#\7\24\2\2#$\5\6\4\2$%\5\n\6\2%"+
		"\u00a3\3\2\2\2&\'\7\37\2\2\'(\5\6\4\2()\5\n\6\2)\u00a3\3\2\2\2*+\7\13"+
		"\2\2+,\5\6\4\2,-\5\n\6\2-\u00a3\3\2\2\2./\7\3\2\2/\60\5\6\4\2\60\61\5"+
		"\n\6\2\61\u00a3\3\2\2\2\62\63\7\25\2\2\63\64\5\6\4\2\64\65\5\n\6\2\65"+
		"\u00a3\3\2\2\2\66\67\7\35\2\2\678\5\f\7\289\7#\2\29\u00a3\3\2\2\2:;\7"+
		"\35\2\2;<\5\16\b\2<=\5\26\f\2=\u00a3\3\2\2\2>?\7\35\2\2?@\5\16\b\2@A\5"+
		"\22\n\2A\u00a3\3\2\2\2BC\7\b\2\2CD\5\f\7\2DE\7#\2\2E\u00a3\3\2\2\2FG\7"+
		"\b\2\2GH\5\16\b\2HI\5\26\f\2I\u00a3\3\2\2\2JK\7\b\2\2KL\5\16\b\2LM\5\22"+
		"\n\2M\u00a3\3\2\2\2NO\7\34\2\2OP\5\f\7\2PQ\7#\2\2Q\u00a3\3\2\2\2RS\7\34"+
		"\2\2ST\5\16\b\2TU\5\26\f\2U\u00a3\3\2\2\2VW\7\34\2\2WX\5\16\b\2XY\5\22"+
		"\n\2Y\u00a3\3\2\2\2Z[\7\5\2\2[\\\5\f\7\2\\]\5\34\17\2]\u00a3\3\2\2\2^"+
		"_\7\5\2\2_`\5\16\b\2`a\5\24\13\2a\u00a3\3\2\2\2bc\7\30\2\2cd\5\f\7\2d"+
		"e\5\34\17\2e\u00a3\3\2\2\2fg\7\30\2\2gh\5\16\b\2hi\5\24\13\2i\u00a3\3"+
		"\2\2\2jk\7\21\2\2kl\5\f\7\2lm\5\32\16\2m\u00a3\3\2\2\2no\7\21\2\2op\5"+
		"\16\b\2pq\5\30\r\2q\u00a3\3\2\2\2rs\7\16\2\2st\5\f\7\2tu\5\32\16\2u\u00a3"+
		"\3\2\2\2vw\7\16\2\2wx\5\16\b\2xy\5\30\r\2y\u00a3\3\2\2\2z{\7\4\2\2{|\5"+
		"\f\7\2|}\5\32\16\2}\u00a3\3\2\2\2~\177\7\4\2\2\177\u0080\5\16\b\2\u0080"+
		"\u0081\5\30\r\2\u0081\u00a3\3\2\2\2\u0082\u0083\7\31\2\2\u0083\u0084\5"+
		"\f\7\2\u0084\u0085\5\32\16\2\u0085\u00a3\3\2\2\2\u0086\u0087\7\31\2\2"+
		"\u0087\u0088\5\16\b\2\u0088\u0089\5\30\r\2\u0089\u00a3\3\2\2\2\u008a\u008b"+
		"\7\n\2\2\u008b\u008c\5\f\7\2\u008c\u008d\5\32\16\2\u008d\u00a3\3\2\2\2"+
		"\u008e\u008f\7\n\2\2\u008f\u0090\5\16\b\2\u0090\u0091\5\30\r\2\u0091\u00a3"+
		"\3\2\2\2\u0092\u0093\7\20\2\2\u0093\u0094\5\f\7\2\u0094\u0095\5\32\16"+
		"\2\u0095\u00a3\3\2\2\2\u0096\u0097\7\20\2\2\u0097\u0098\5\16\b\2\u0098"+
		"\u0099\5\30\r\2\u0099\u00a3\3\2\2\2\u009a\u009b\7\17\2\2\u009b\u009c\5"+
		"\6\4\2\u009c\u009d\5\n\6\2\u009d\u00a3\3\2\2\2\u009e\u009f\7\f\2\2\u009f"+
		"\u00a0\5\4\3\2\u00a0\u00a1\7\36\2\2\u00a1\u00a3\3\2\2\2\u00a2!\3\2\2\2"+
		"\u00a2&\3\2\2\2\u00a2*\3\2\2\2\u00a2.\3\2\2\2\u00a2\62\3\2\2\2\u00a2\66"+
		"\3\2\2\2\u00a2:\3\2\2\2\u00a2>\3\2\2\2\u00a2B\3\2\2\2\u00a2F\3\2\2\2\u00a2"+
		"J\3\2\2\2\u00a2N\3\2\2\2\u00a2R\3\2\2\2\u00a2V\3\2\2\2\u00a2Z\3\2\2\2"+
		"\u00a2^\3\2\2\2\u00a2b\3\2\2\2\u00a2f\3\2\2\2\u00a2j\3\2\2\2\u00a2n\3"+
		"\2\2\2\u00a2r\3\2\2\2\u00a2v\3\2\2\2\u00a2z\3\2\2\2\u00a2~\3\2\2\2\u00a2"+
		"\u0082\3\2\2\2\u00a2\u0086\3\2\2\2\u00a2\u008a\3\2\2\2\u00a2\u008e\3\2"+
		"\2\2\u00a2\u0092\3\2\2\2\u00a2\u0096\3\2\2\2\u00a2\u009a\3\2\2\2\u00a2"+
		"\u009e\3\2\2\2\u00a3\u00ad\3\2\2\2\u00a4\u00a7\f\4\2\2\u00a5\u00a6\t\2"+
		"\2\2\u00a6\u00a8\5\4\3\2\u00a7\u00a5\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9"+
		"\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ac\3\2\2\2\u00ab\u00a4\3\2"+
		"\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae"+
		"\5\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0\u00b1\t\3\2\2\u00b1\7\3\2\2\2\u00b2"+
		"\u00b3\t\4\2\2\u00b3\t\3\2\2\2\u00b4\u00b6\7\f\2\2\u00b5\u00b4\3\2\2\2"+
		"\u00b5\u00b6\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00bc\5\b\5\2\u00b8\u00b9"+
		"\7\r\2\2\u00b9\u00bb\5\b\5\2\u00ba\u00b8\3\2\2\2\u00bb\u00be\3\2\2\2\u00bc"+
		"\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be\u00bc\3\2"+
		"\2\2\u00bf\u00c1\7\36\2\2\u00c0\u00bf\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1"+
		"\13\3\2\2\2\u00c2\u00c3\t\5\2\2\u00c3\r\3\2\2\2\u00c4\u00c5\t\6\2\2\u00c5"+
		"\17\3\2\2\2\u00c6\u00c9\5\f\7\2\u00c7\u00c9\5\16\b\2\u00c8\u00c6\3\2\2"+
		"\2\u00c8\u00c7\3\2\2\2\u00c9\21\3\2\2\2\u00ca\u00cc\7\f\2\2\u00cb\u00ca"+
		"\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00d2\7#\2\2\u00ce"+
		"\u00cf\7\r\2\2\u00cf\u00d1\7#\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00d4\3\2"+
		"\2\2\u00d2\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4"+
		"\u00d2\3\2\2\2\u00d5\u00d7\7\36\2\2\u00d6\u00d5\3\2\2\2\u00d6\u00d7\3"+
		"\2\2\2\u00d7\23\3\2\2\2\u00d8\u00da\7\f\2\2\u00d9\u00d8\3\2\2\2\u00d9"+
		"\u00da\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00e0\5\34\17\2\u00dc\u00dd\7"+
		"\r\2\2\u00dd\u00df\5\34\17\2\u00de\u00dc\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0"+
		"\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2\u00e0\3\2"+
		"\2\2\u00e3\u00e5\7\36\2\2\u00e4\u00e3\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5"+
		"\25\3\2\2\2\u00e6\u00e8\7\f\2\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2"+
		"\2\u00e8\u00e9\3\2\2\2\u00e9\u00ea\7#\2\2\u00ea\u00eb\7\"\2\2\u00eb\u00ed"+
		"\7#\2\2\u00ec\u00ee\7\36\2\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee"+
		"\27\3\2\2\2\u00ef\u00f1\7\f\2\2\u00f0\u00ef\3\2\2\2\u00f0\u00f1\3\2\2"+
		"\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\5\32\16\2\u00f3\u00f4\7\"\2\2\u00f4"+
		"\u00f6\5\32\16\2\u00f5\u00f7\7\36\2\2\u00f6\u00f5\3\2\2\2\u00f6\u00f7"+
		"\3\2\2\2\u00f7\31\3\2\2\2\u00f8\u00f9\t\7\2\2\u00f9\33\3\2\2\2\u00fa\u00fe"+
		"\5\32\16\2\u00fb\u00fe\7%\2\2\u00fc\u00fe\7&\2\2\u00fd\u00fa\3\2\2\2\u00fd"+
		"\u00fb\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe\35\3\2\2\2\24\u00a2\u00a9\u00ad"+
		"\u00b5\u00bc\u00c0\u00c8\u00cb\u00d2\u00d6\u00d9\u00e0\u00e4\u00e7\u00ed"+
		"\u00f0\u00f6\u00fd";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}