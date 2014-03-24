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
		T__29=1, T__28=2, T__27=3, T__26=4, T__25=5, T__24=6, T__23=7, T__22=8, 
		T__21=9, T__20=10, T__19=11, T__18=12, T__17=13, T__16=14, T__15=15, T__14=16, 
		T__13=17, T__12=18, T__11=19, T__10=20, T__9=21, T__8=22, T__7=23, T__6=24, 
		T__5=25, T__4=26, T__3=27, T__2=28, T__1=29, T__0=30, AND=31, OR=32, RANGE=33, 
		INT=34, FLOAT=35, WORD=36, STRING=37, WS=38;
	public static final String[] tokenNames = {
		"<INVALID>", "'s.type'", "'m.lat'", "'m.name'", "'!='", "'='", "'m.mmsi'", 
		"'<='", "'m.hdg'", "'s.country'", "'('", "','", "'m.cog'", "'messagetype'", 
		"'m.draught'", "'m.sog'", "'>='", "'<'", "'s.id'", "'s.region'", "'>'", 
		"'@'", "'m.cs'", "'m.lon'", "'m.type'", "'IN'", "'in'", "'m.imo'", "'m.id'", 
		"')'", "'s.bs'", "'&'", "'|'", "'..'", "INT", "FLOAT", "WORD", "STRING", 
		"WS"
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
	public static class MessageShiptypeInListContext extends FilterExpressionContext {
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageShiptypeInListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptypeInList(this);
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
	public static class MessageShiptypeInRangeContext extends FilterExpressionContext {
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageShiptypeInRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptypeInRange(this);
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
	public static class MessageShiptypeLabelContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public MessageShiptypeLabelContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptypeLabel(this);
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
	public static class MessageShiptypeInLabelListContext extends FilterExpressionContext {
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public InListOrRangeContext inListOrRange() {
			return getRuleContext(InListOrRangeContext.class,0);
		}
		public MessageShiptypeInLabelListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptypeInLabelList(this);
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
	public static class MessageShiptypeContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public MessageShiptypeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptype(this);
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
			setState(180);
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
				setState(36); match(30);
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
				setState(52); match(28);
				setState(53); compareTo();
				setState(54); match(INT);
				}
				break;

			case 7:
				{
				_localctx = new MessageIdInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56); match(28);
				setState(57); inListOrRange();
				setState(58); intRange();
				}
				break;

			case 8:
				{
				_localctx = new MessageIdInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(60); match(28);
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
				setState(76); match(27);
				setState(77); compareTo();
				setState(78); match(INT);
				}
				break;

			case 13:
				{
				_localctx = new MessageImoInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(80); match(27);
				setState(81); inListOrRange();
				setState(82); intRange();
				}
				break;

			case 14:
				{
				_localctx = new MessageImoInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84); match(27);
				setState(85); inListOrRange();
				setState(86); intList();
				}
				break;

			case 15:
				{
				_localctx = new MessageShiptypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(88); match(24);
				setState(89); compareTo();
				setState(90); match(INT);
				}
				break;

			case 16:
				{
				_localctx = new MessageShiptypeLabelContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(92); match(24);
				setState(93); compareTo();
				setState(94); string();
				}
				break;

			case 17:
				{
				_localctx = new MessageShiptypeInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(96); match(24);
				setState(97); inListOrRange();
				setState(98); intRange();
				}
				break;

			case 18:
				{
				_localctx = new MessageShiptypeInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100); match(24);
				setState(101); inListOrRange();
				setState(102); intList();
				}
				break;

			case 19:
				{
				_localctx = new MessageShiptypeInLabelListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(104); match(24);
				setState(105); inListOrRange();
				setState(106); stringList();
				}
				break;

			case 20:
				{
				_localctx = new MessageNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(108); match(3);
				setState(109); compareTo();
				setState(110); string();
				}
				break;

			case 21:
				{
				_localctx = new MessageNameInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(112); match(3);
				setState(113); inListOrRange();
				setState(114); stringList();
				}
				break;

			case 22:
				{
				_localctx = new MessageCallsignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(116); match(22);
				setState(117); compareTo();
				setState(118); string();
				}
				break;

			case 23:
				{
				_localctx = new MessageCallsignInListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(120); match(22);
				setState(121); inListOrRange();
				setState(122); stringList();
				}
				break;

			case 24:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(124); match(15);
				setState(125); compareTo();
				setState(126); number();
				}
				break;

			case 25:
				{
				_localctx = new MessageSpeedOverGroundInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(128); match(15);
				setState(129); inListOrRange();
				setState(130); numberRange();
				}
				break;

			case 26:
				{
				_localctx = new MessageCourseOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(132); match(12);
				setState(133); compareTo();
				setState(134); number();
				}
				break;

			case 27:
				{
				_localctx = new MessageCourseOverGroundInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(136); match(12);
				setState(137); inListOrRange();
				setState(138); numberRange();
				}
				break;

			case 28:
				{
				_localctx = new MessageLatitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(140); match(2);
				setState(141); compareTo();
				setState(142); number();
				}
				break;

			case 29:
				{
				_localctx = new MessageLatitudeInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(144); match(2);
				setState(145); inListOrRange();
				setState(146); numberRange();
				}
				break;

			case 30:
				{
				_localctx = new MessageLongitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(148); match(23);
				setState(149); compareTo();
				setState(150); number();
				}
				break;

			case 31:
				{
				_localctx = new MessageLongitudeInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(152); match(23);
				setState(153); inListOrRange();
				setState(154); numberRange();
				}
				break;

			case 32:
				{
				_localctx = new MessageTrueHeadingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(156); match(8);
				setState(157); compareTo();
				setState(158); number();
				}
				break;

			case 33:
				{
				_localctx = new MessageTrueHeadingInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(160); match(8);
				setState(161); inListOrRange();
				setState(162); numberRange();
				}
				break;

			case 34:
				{
				_localctx = new MessageDraughtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(164); match(14);
				setState(165); compareTo();
				setState(166); number();
				}
				break;

			case 35:
				{
				_localctx = new MessageDraughtInRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(168); match(14);
				setState(169); inListOrRange();
				setState(170); numberRange();
				}
				break;

			case 36:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(172); match(13);
				setState(173); equalityTest();
				setState(174); valueList();
				}
				break;

			case 37:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(176); match(10);
				setState(177); filterExpression(0);
				setState(178); match(29);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(191);
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
					setState(182);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(185); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(183);
							((OrAndContext)_localctx).op = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==AND || _la==OR) ) {
								((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
							}
							consume();
							setState(184); filterExpression(0);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(187); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					} while ( _alt!=2 && _alt!=-1 );
					}
					} 
				}
				setState(193);
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
			setState(194);
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
			setState(196);
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
			setState(199);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(198); match(10);
				}
			}

			setState(201); value();
			setState(206);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(202); match(11);
					setState(203); value();
					}
					} 
				}
				setState(208);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(210);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(209); match(29);
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
			setState(212);
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
			setState(214);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 21) | (1L << 25) | (1L << 26))) != 0)) ) {
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
			setState(218);
			switch (_input.LA(1)) {
			case 4:
			case 5:
			case 7:
			case 16:
			case 17:
			case 20:
				{
				setState(216); compareTo();
				}
				break;
			case 21:
			case 25:
			case 26:
				{
				setState(217); inListOrRange();
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
			setState(221);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(220); match(10);
				}
			}

			setState(223); match(INT);
			setState(228);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(224); match(11);
					setState(225); match(INT);
					}
					} 
				}
				setState(230);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(232);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(231); match(29);
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
			setState(235);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(234); match(10);
				}
			}

			setState(237); string();
			setState(242);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(238); match(11);
					setState(239); string();
					}
					} 
				}
				setState(244);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(246);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(245); match(29);
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
			setState(249);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(248); match(10);
				}
			}

			setState(251); match(INT);
			setState(252); match(RANGE);
			setState(253); match(INT);
			setState(255);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(254); match(29);
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
			setState(258);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(257); match(10);
				}
			}

			setState(260); number();
			setState(261); match(RANGE);
			setState(262); number();
			setState(264);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(263); match(29);
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
			setState(266);
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
			setState(271);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
				{
				setState(268); number();
				}
				break;
			case WORD:
				{
				setState(269); match(WORD);
				}
				break;
			case STRING:
				{
				setState(270); match(STRING);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3(\u0114\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u00b7\n\3\3\3\3\3\3\3\6\3\u00bc\n\3\r\3\16\3\u00bd"+
		"\7\3\u00c0\n\3\f\3\16\3\u00c3\13\3\3\4\3\4\3\5\3\5\3\6\5\6\u00ca\n\6\3"+
		"\6\3\6\3\6\7\6\u00cf\n\6\f\6\16\6\u00d2\13\6\3\6\5\6\u00d5\n\6\3\7\3\7"+
		"\3\b\3\b\3\t\3\t\5\t\u00dd\n\t\3\n\5\n\u00e0\n\n\3\n\3\n\3\n\7\n\u00e5"+
		"\n\n\f\n\16\n\u00e8\13\n\3\n\5\n\u00eb\n\n\3\13\5\13\u00ee\n\13\3\13\3"+
		"\13\3\13\7\13\u00f3\n\13\f\13\16\13\u00f6\13\13\3\13\5\13\u00f9\n\13\3"+
		"\f\5\f\u00fc\n\f\3\f\3\f\3\f\3\f\5\f\u0102\n\f\3\r\5\r\u0105\n\r\3\r\3"+
		"\r\3\r\3\r\5\r\u010b\n\r\3\16\3\16\3\17\3\17\3\17\5\17\u0112\n\17\3\17"+
		"\2\3\4\20\2\4\6\b\n\f\16\20\22\24\26\30\32\34\2\b\3\2!\"\3\2\6\7\3\2$"+
		"&\6\2\6\7\t\t\22\23\26\26\4\2\27\27\33\34\3\2$%\u013b\2\36\3\2\2\2\4\u00b6"+
		"\3\2\2\2\6\u00c4\3\2\2\2\b\u00c6\3\2\2\2\n\u00c9\3\2\2\2\f\u00d6\3\2\2"+
		"\2\16\u00d8\3\2\2\2\20\u00dc\3\2\2\2\22\u00df\3\2\2\2\24\u00ed\3\2\2\2"+
		"\26\u00fb\3\2\2\2\30\u0104\3\2\2\2\32\u010c\3\2\2\2\34\u0111\3\2\2\2\36"+
		"\37\5\4\3\2\37 \7\2\2\3 \3\3\2\2\2!\"\b\3\1\2\"#\7\24\2\2#$\5\6\4\2$%"+
		"\5\n\6\2%\u00b7\3\2\2\2&\'\7 \2\2\'(\5\6\4\2()\5\n\6\2)\u00b7\3\2\2\2"+
		"*+\7\13\2\2+,\5\6\4\2,-\5\n\6\2-\u00b7\3\2\2\2./\7\3\2\2/\60\5\6\4\2\60"+
		"\61\5\n\6\2\61\u00b7\3\2\2\2\62\63\7\25\2\2\63\64\5\6\4\2\64\65\5\n\6"+
		"\2\65\u00b7\3\2\2\2\66\67\7\36\2\2\678\5\f\7\289\7$\2\29\u00b7\3\2\2\2"+
		":;\7\36\2\2;<\5\16\b\2<=\5\26\f\2=\u00b7\3\2\2\2>?\7\36\2\2?@\5\16\b\2"+
		"@A\5\22\n\2A\u00b7\3\2\2\2BC\7\b\2\2CD\5\f\7\2DE\7$\2\2E\u00b7\3\2\2\2"+
		"FG\7\b\2\2GH\5\16\b\2HI\5\26\f\2I\u00b7\3\2\2\2JK\7\b\2\2KL\5\16\b\2L"+
		"M\5\22\n\2M\u00b7\3\2\2\2NO\7\35\2\2OP\5\f\7\2PQ\7$\2\2Q\u00b7\3\2\2\2"+
		"RS\7\35\2\2ST\5\16\b\2TU\5\26\f\2U\u00b7\3\2\2\2VW\7\35\2\2WX\5\16\b\2"+
		"XY\5\22\n\2Y\u00b7\3\2\2\2Z[\7\32\2\2[\\\5\f\7\2\\]\7$\2\2]\u00b7\3\2"+
		"\2\2^_\7\32\2\2_`\5\f\7\2`a\5\34\17\2a\u00b7\3\2\2\2bc\7\32\2\2cd\5\16"+
		"\b\2de\5\26\f\2e\u00b7\3\2\2\2fg\7\32\2\2gh\5\16\b\2hi\5\22\n\2i\u00b7"+
		"\3\2\2\2jk\7\32\2\2kl\5\16\b\2lm\5\24\13\2m\u00b7\3\2\2\2no\7\5\2\2op"+
		"\5\f\7\2pq\5\34\17\2q\u00b7\3\2\2\2rs\7\5\2\2st\5\16\b\2tu\5\24\13\2u"+
		"\u00b7\3\2\2\2vw\7\30\2\2wx\5\f\7\2xy\5\34\17\2y\u00b7\3\2\2\2z{\7\30"+
		"\2\2{|\5\16\b\2|}\5\24\13\2}\u00b7\3\2\2\2~\177\7\21\2\2\177\u0080\5\f"+
		"\7\2\u0080\u0081\5\32\16\2\u0081\u00b7\3\2\2\2\u0082\u0083\7\21\2\2\u0083"+
		"\u0084\5\16\b\2\u0084\u0085\5\30\r\2\u0085\u00b7\3\2\2\2\u0086\u0087\7"+
		"\16\2\2\u0087\u0088\5\f\7\2\u0088\u0089\5\32\16\2\u0089\u00b7\3\2\2\2"+
		"\u008a\u008b\7\16\2\2\u008b\u008c\5\16\b\2\u008c\u008d\5\30\r\2\u008d"+
		"\u00b7\3\2\2\2\u008e\u008f\7\4\2\2\u008f\u0090\5\f\7\2\u0090\u0091\5\32"+
		"\16\2\u0091\u00b7\3\2\2\2\u0092\u0093\7\4\2\2\u0093\u0094\5\16\b\2\u0094"+
		"\u0095\5\30\r\2\u0095\u00b7\3\2\2\2\u0096\u0097\7\31\2\2\u0097\u0098\5"+
		"\f\7\2\u0098\u0099\5\32\16\2\u0099\u00b7\3\2\2\2\u009a\u009b\7\31\2\2"+
		"\u009b\u009c\5\16\b\2\u009c\u009d\5\30\r\2\u009d\u00b7\3\2\2\2\u009e\u009f"+
		"\7\n\2\2\u009f\u00a0\5\f\7\2\u00a0\u00a1\5\32\16\2\u00a1\u00b7\3\2\2\2"+
		"\u00a2\u00a3\7\n\2\2\u00a3\u00a4\5\16\b\2\u00a4\u00a5\5\30\r\2\u00a5\u00b7"+
		"\3\2\2\2\u00a6\u00a7\7\20\2\2\u00a7\u00a8\5\f\7\2\u00a8\u00a9\5\32\16"+
		"\2\u00a9\u00b7\3\2\2\2\u00aa\u00ab\7\20\2\2\u00ab\u00ac\5\16\b\2\u00ac"+
		"\u00ad\5\30\r\2\u00ad\u00b7\3\2\2\2\u00ae\u00af\7\17\2\2\u00af\u00b0\5"+
		"\6\4\2\u00b0\u00b1\5\n\6\2\u00b1\u00b7\3\2\2\2\u00b2\u00b3\7\f\2\2\u00b3"+
		"\u00b4\5\4\3\2\u00b4\u00b5\7\37\2\2\u00b5\u00b7\3\2\2\2\u00b6!\3\2\2\2"+
		"\u00b6&\3\2\2\2\u00b6*\3\2\2\2\u00b6.\3\2\2\2\u00b6\62\3\2\2\2\u00b6\66"+
		"\3\2\2\2\u00b6:\3\2\2\2\u00b6>\3\2\2\2\u00b6B\3\2\2\2\u00b6F\3\2\2\2\u00b6"+
		"J\3\2\2\2\u00b6N\3\2\2\2\u00b6R\3\2\2\2\u00b6V\3\2\2\2\u00b6Z\3\2\2\2"+
		"\u00b6^\3\2\2\2\u00b6b\3\2\2\2\u00b6f\3\2\2\2\u00b6j\3\2\2\2\u00b6n\3"+
		"\2\2\2\u00b6r\3\2\2\2\u00b6v\3\2\2\2\u00b6z\3\2\2\2\u00b6~\3\2\2\2\u00b6"+
		"\u0082\3\2\2\2\u00b6\u0086\3\2\2\2\u00b6\u008a\3\2\2\2\u00b6\u008e\3\2"+
		"\2\2\u00b6\u0092\3\2\2\2\u00b6\u0096\3\2\2\2\u00b6\u009a\3\2\2\2\u00b6"+
		"\u009e\3\2\2\2\u00b6\u00a2\3\2\2\2\u00b6\u00a6\3\2\2\2\u00b6\u00aa\3\2"+
		"\2\2\u00b6\u00ae\3\2\2\2\u00b6\u00b2\3\2\2\2\u00b7\u00c1\3\2\2\2\u00b8"+
		"\u00bb\f\4\2\2\u00b9\u00ba\t\2\2\2\u00ba\u00bc\5\4\3\2\u00bb\u00b9\3\2"+
		"\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be"+
		"\u00c0\3\2\2\2\u00bf\u00b8\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1\u00bf\3\2"+
		"\2\2\u00c1\u00c2\3\2\2\2\u00c2\5\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c4\u00c5"+
		"\t\3\2\2\u00c5\7\3\2\2\2\u00c6\u00c7\t\4\2\2\u00c7\t\3\2\2\2\u00c8\u00ca"+
		"\7\f\2\2\u00c9\u00c8\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb"+
		"\u00d0\5\b\5\2\u00cc\u00cd\7\r\2\2\u00cd\u00cf\5\b\5\2\u00ce\u00cc\3\2"+
		"\2\2\u00cf\u00d2\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1"+
		"\u00d4\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d3\u00d5\7\37\2\2\u00d4\u00d3\3"+
		"\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\13\3\2\2\2\u00d6\u00d7\t\5\2\2\u00d7"+
		"\r\3\2\2\2\u00d8\u00d9\t\6\2\2\u00d9\17\3\2\2\2\u00da\u00dd\5\f\7\2\u00db"+
		"\u00dd\5\16\b\2\u00dc\u00da\3\2\2\2\u00dc\u00db\3\2\2\2\u00dd\21\3\2\2"+
		"\2\u00de\u00e0\7\f\2\2\u00df\u00de\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0\u00e1"+
		"\3\2\2\2\u00e1\u00e6\7$\2\2\u00e2\u00e3\7\r\2\2\u00e3\u00e5\7$\2\2\u00e4"+
		"\u00e2\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2"+
		"\2\2\u00e7\u00ea\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00eb\7\37\2\2\u00ea"+
		"\u00e9\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\23\3\2\2\2\u00ec\u00ee\7\f\2"+
		"\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f4"+
		"\5\34\17\2\u00f0\u00f1\7\r\2\2\u00f1\u00f3\5\34\17\2\u00f2\u00f0\3\2\2"+
		"\2\u00f3\u00f6\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f8"+
		"\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f7\u00f9\7\37\2\2\u00f8\u00f7\3\2\2\2"+
		"\u00f8\u00f9\3\2\2\2\u00f9\25\3\2\2\2\u00fa\u00fc\7\f\2\2\u00fb\u00fa"+
		"\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00fe\7$\2\2\u00fe"+
		"\u00ff\7#\2\2\u00ff\u0101\7$\2\2\u0100\u0102\7\37\2\2\u0101\u0100\3\2"+
		"\2\2\u0101\u0102\3\2\2\2\u0102\27\3\2\2\2\u0103\u0105\7\f\2\2\u0104\u0103"+
		"\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0107\5\32\16\2"+
		"\u0107\u0108\7#\2\2\u0108\u010a\5\32\16\2\u0109\u010b\7\37\2\2\u010a\u0109"+
		"\3\2\2\2\u010a\u010b\3\2\2\2\u010b\31\3\2\2\2\u010c\u010d\t\7\2\2\u010d"+
		"\33\3\2\2\2\u010e\u0112\5\32\16\2\u010f\u0112\7&\2\2\u0110\u0112\7\'\2"+
		"\2\u0111\u010e\3\2\2\2\u0111\u010f\3\2\2\2\u0111\u0110\3\2\2\2\u0112\35"+
		"\3\2\2\2\24\u00b6\u00bd\u00c1\u00c9\u00d0\u00d4\u00dc\u00df\u00e6\u00ea"+
		"\u00ed\u00f4\u00f8\u00fb\u0101\u0104\u010a\u0111";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}