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
		T__30=1, T__29=2, T__28=3, T__27=4, T__26=5, T__25=6, T__24=7, T__23=8, 
		T__22=9, T__21=10, T__20=11, T__19=12, T__18=13, T__17=14, T__16=15, T__15=16, 
		T__14=17, T__13=18, T__12=19, T__11=20, T__10=21, T__9=22, T__8=23, T__7=24, 
		T__6=25, T__5=26, T__4=27, T__3=28, T__2=29, T__1=30, T__0=31, AND=32, 
		OR=33, RANGE=34, INT=35, FLOAT=36, WORD=37, STRING=38, WS=39;
	public static final String[] tokenNames = {
		"<INVALID>", "'s.type'", "'m.lat'", "'m.name'", "'!='", "'='", "'m.mmsi'", 
		"'<='", "'m.hdg'", "'s.country'", "'('", "','", "'m.cog'", "'messagetype'", 
		"'m.draught'", "'m.sog'", "'m.navstat'", "'>='", "'<'", "'s.id'", "'s.region'", 
		"'>'", "'@'", "'m.cs'", "'m.lon'", "'m.type'", "'IN'", "'in'", "'m.imo'", 
		"'m.id'", "')'", "'s.bs'", "'&'", "'|'", "'..'", "INT", "FLOAT", "WORD", 
		"STRING", "WS"
	};
	public static final int
		RULE_prog = 0, RULE_filterExpression = 1, RULE_compareTo = 2, RULE_in = 3, 
		RULE_intList = 4, RULE_stringList = 5, RULE_intRange = 6, RULE_numberRange = 7, 
		RULE_number = 8, RULE_string = 9;
	public static final String[] ruleNames = {
		"prog", "filterExpression", "compareTo", "in", "intList", "stringList", 
		"intRange", "numberRange", "number", "string"
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
	public static class SourceTypeInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public SourceTypeInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceTypeInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageCallsignInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public MessageCallsignInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageCallsignInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceIdInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public SourceIdInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceIdInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNavigationalStatusContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public MessageNavigationalStatusContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageNavigationalStatus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceBasestationContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(SourceFilterParser.INT, 0); }
		public SourceBasestationContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceBasestation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageDraughtInNumberRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public MessageDraughtInNumberRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageDraughtInNumberRange(this);
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
	public static class MessageSpeedOverGroundInNumberRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public MessageSpeedOverGroundInNumberRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageSpeedOverGroundInNumberRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNavigationalStatusInIntRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public MessageNavigationalStatusInIntRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageNavigationalStatusInIntRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageTrueHeadingInNumberRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public MessageTrueHeadingInNumberRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageTrueHeadingInNumberRange(this);
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
	public static class SourceBasestationInIntListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public SourceBasestationInIntListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceBasestationInIntList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNameInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public MessageNameInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageNameInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AisMessagetypeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public AisMessagetypeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitAisMessagetype(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageShiptypeInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public MessageShiptypeInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptypeInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageShiptypeInIntRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public MessageShiptypeInIntRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptypeInIntRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdInIntRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public MessageIdInIntRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageIdInIntRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNavigationalStatusInIntListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageNavigationalStatusInIntListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageNavigationalStatusInIntList(this);
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
	public static class MessageImoInIntListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageImoInIntListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageImoInIntList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageLatitudeInNumberRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public MessageLatitudeInNumberRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLatitudeInNumberRange(this);
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
	public static class MessageLongitudeInNumberRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public MessageLongitudeInNumberRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageLongitudeInNumberRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNavigationalStatusInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public MessageNavigationalStatusInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageNavigationalStatusInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageCourseOverGroundInNumberRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public MessageCourseOverGroundInNumberRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageCourseOverGroundInNumberRange(this);
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
	public static class MessageMmsiInIntRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public MessageMmsiInIntRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageMmsiInIntRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceRegionInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public SourceRegionInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceRegionInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageShiptypeInIntListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageShiptypeInIntListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptypeInIntList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceCountryInStringListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public SourceCountryInStringListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceCountryInStringList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdInIntListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageIdInIntListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageIdInIntList(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageImoInIntRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public MessageImoInIntRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageImoInIntRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceBasestationInIntRangeContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public SourceBasestationInIntRangeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceBasestationInIntRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageShiptypeContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public MessageShiptypeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageShiptype(this);
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
	public static class MessageMmsiInIntListContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageMmsiInIntListContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitMessageMmsiInIntList(this);
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
			setState(192);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				_localctx = new SourceIdInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(24); match(19);
				setState(25); in();
				setState(26); stringList();
				}
				break;

			case 2:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28); match(31);
				setState(29); compareTo();
				setState(30); match(INT);
				}
				break;

			case 3:
				{
				_localctx = new SourceBasestationInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(32); match(31);
				setState(33); in();
				setState(34); intRange();
				}
				break;

			case 4:
				{
				_localctx = new SourceBasestationInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(36); match(31);
				setState(37); in();
				setState(38); intList();
				}
				break;

			case 5:
				{
				_localctx = new SourceCountryInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(40); match(9);
				setState(41); in();
				setState(42); stringList();
				}
				break;

			case 6:
				{
				_localctx = new SourceTypeInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(44); match(1);
				setState(45); in();
				setState(46); stringList();
				}
				break;

			case 7:
				{
				_localctx = new SourceRegionInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(48); match(20);
				setState(49); in();
				setState(50); stringList();
				}
				break;

			case 8:
				{
				_localctx = new MessageIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52); match(29);
				setState(53); compareTo();
				setState(54); match(INT);
				}
				break;

			case 9:
				{
				_localctx = new MessageIdInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(56); match(29);
				setState(57); in();
				setState(58); intRange();
				}
				break;

			case 10:
				{
				_localctx = new MessageIdInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(60); match(29);
				setState(61); in();
				setState(62); intList();
				}
				break;

			case 11:
				{
				_localctx = new MessageMmsiContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64); match(6);
				setState(65); compareTo();
				setState(66); match(INT);
				}
				break;

			case 12:
				{
				_localctx = new MessageMmsiInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(68); match(6);
				setState(69); in();
				setState(70); intRange();
				}
				break;

			case 13:
				{
				_localctx = new MessageMmsiInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(72); match(6);
				setState(73); in();
				setState(74); intList();
				}
				break;

			case 14:
				{
				_localctx = new MessageImoContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(76); match(28);
				setState(77); compareTo();
				setState(78); match(INT);
				}
				break;

			case 15:
				{
				_localctx = new MessageImoInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(80); match(28);
				setState(81); in();
				setState(82); intRange();
				}
				break;

			case 16:
				{
				_localctx = new MessageImoInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84); match(28);
				setState(85); in();
				setState(86); intList();
				}
				break;

			case 17:
				{
				_localctx = new MessageShiptypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(88); match(25);
				setState(89); compareTo();
				setState(90); string();
				}
				break;

			case 18:
				{
				_localctx = new MessageShiptypeInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(92); match(25);
				setState(93); in();
				setState(94); intRange();
				}
				break;

			case 19:
				{
				_localctx = new MessageShiptypeInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(96); match(25);
				setState(97); in();
				setState(98); intList();
				}
				break;

			case 20:
				{
				_localctx = new MessageShiptypeInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100); match(25);
				setState(101); in();
				setState(102); stringList();
				}
				break;

			case 21:
				{
				_localctx = new MessageNavigationalStatusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(104); match(16);
				setState(105); compareTo();
				setState(106); string();
				}
				break;

			case 22:
				{
				_localctx = new MessageNavigationalStatusInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(108); match(16);
				setState(109); in();
				setState(110); intRange();
				}
				break;

			case 23:
				{
				_localctx = new MessageNavigationalStatusInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(112); match(16);
				setState(113); in();
				setState(114); intList();
				}
				break;

			case 24:
				{
				_localctx = new MessageNavigationalStatusInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(116); match(16);
				setState(117); in();
				setState(118); stringList();
				}
				break;

			case 25:
				{
				_localctx = new MessageNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(120); match(3);
				setState(121); compareTo();
				setState(122); string();
				}
				break;

			case 26:
				{
				_localctx = new MessageNameInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(124); match(3);
				setState(125); in();
				setState(126); stringList();
				}
				break;

			case 27:
				{
				_localctx = new MessageCallsignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(128); match(23);
				setState(129); compareTo();
				setState(130); string();
				}
				break;

			case 28:
				{
				_localctx = new MessageCallsignInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(132); match(23);
				setState(133); in();
				setState(134); stringList();
				}
				break;

			case 29:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(136); match(15);
				setState(137); compareTo();
				setState(138); number();
				}
				break;

			case 30:
				{
				_localctx = new MessageSpeedOverGroundInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(140); match(15);
				setState(141); in();
				setState(142); numberRange();
				}
				break;

			case 31:
				{
				_localctx = new MessageCourseOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(144); match(12);
				setState(145); compareTo();
				setState(146); number();
				}
				break;

			case 32:
				{
				_localctx = new MessageCourseOverGroundInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(148); match(12);
				setState(149); in();
				setState(150); numberRange();
				}
				break;

			case 33:
				{
				_localctx = new MessageLatitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(152); match(2);
				setState(153); compareTo();
				setState(154); number();
				}
				break;

			case 34:
				{
				_localctx = new MessageLatitudeInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(156); match(2);
				setState(157); in();
				setState(158); numberRange();
				}
				break;

			case 35:
				{
				_localctx = new MessageLongitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(160); match(24);
				setState(161); compareTo();
				setState(162); number();
				}
				break;

			case 36:
				{
				_localctx = new MessageLongitudeInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(164); match(24);
				setState(165); in();
				setState(166); numberRange();
				}
				break;

			case 37:
				{
				_localctx = new MessageTrueHeadingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(168); match(8);
				setState(169); compareTo();
				setState(170); number();
				}
				break;

			case 38:
				{
				_localctx = new MessageTrueHeadingInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(172); match(8);
				setState(173); in();
				setState(174); numberRange();
				}
				break;

			case 39:
				{
				_localctx = new MessageDraughtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(176); match(14);
				setState(177); compareTo();
				setState(178); number();
				}
				break;

			case 40:
				{
				_localctx = new MessageDraughtInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(180); match(14);
				setState(181); in();
				setState(182); numberRange();
				}
				break;

			case 41:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(184); match(13);
				setState(185); in();
				setState(186); stringList();
				}
				break;

			case 42:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(188); match(10);
				setState(189); filterExpression(0);
				setState(190); match(30);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(203);
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
					setState(194);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(197); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(195);
							((OrAndContext)_localctx).op = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==AND || _la==OR) ) {
								((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
							}
							consume();
							setState(196); filterExpression(0);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(199); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
					} while ( _alt!=2 && _alt!=-1 );
					}
					} 
				}
				setState(205);
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
		enterRule(_localctx, 4, RULE_compareTo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 5) | (1L << 7) | (1L << 17) | (1L << 18) | (1L << 21))) != 0)) ) {
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

	public static class InContext extends ParserRuleContext {
		public InContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_in; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitIn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InContext in() throws RecognitionException {
		InContext _localctx = new InContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_in);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 22) | (1L << 26) | (1L << 27))) != 0)) ) {
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
		enterRule(_localctx, 8, RULE_intList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(210); match(10);
				}
			}

			setState(213); match(INT);
			setState(218);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(214); match(11);
					setState(215); match(INT);
					}
					} 
				}
				setState(220);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(222);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(221); match(30);
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
		enterRule(_localctx, 10, RULE_stringList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(224); match(10);
				}
			}

			setState(227); string();
			setState(232);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(228); match(11);
					setState(229); string();
					}
					} 
				}
				setState(234);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			setState(236);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(235); match(30);
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
		enterRule(_localctx, 12, RULE_intRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(238); match(10);
				}
			}

			setState(241); match(INT);
			setState(242); match(RANGE);
			setState(243); match(INT);
			setState(245);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(244); match(30);
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
		enterRule(_localctx, 14, RULE_numberRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(247); match(10);
				}
			}

			setState(250); number();
			setState(251); match(RANGE);
			setState(252); number();
			setState(254);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(253); match(30);
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
		enterRule(_localctx, 16, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
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
		enterRule(_localctx, 18, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
				{
				setState(258); number();
				}
				break;
			case WORD:
				{
				setState(259); match(WORD);
				}
				break;
			case STRING:
				{
				setState(260); match(STRING);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3)\u010a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00c3"+
		"\n\3\3\3\3\3\3\3\6\3\u00c8\n\3\r\3\16\3\u00c9\7\3\u00cc\n\3\f\3\16\3\u00cf"+
		"\13\3\3\4\3\4\3\5\3\5\3\6\5\6\u00d6\n\6\3\6\3\6\3\6\7\6\u00db\n\6\f\6"+
		"\16\6\u00de\13\6\3\6\5\6\u00e1\n\6\3\7\5\7\u00e4\n\7\3\7\3\7\3\7\7\7\u00e9"+
		"\n\7\f\7\16\7\u00ec\13\7\3\7\5\7\u00ef\n\7\3\b\5\b\u00f2\n\b\3\b\3\b\3"+
		"\b\3\b\5\b\u00f8\n\b\3\t\5\t\u00fb\n\t\3\t\3\t\3\t\3\t\5\t\u0101\n\t\3"+
		"\n\3\n\3\13\3\13\3\13\5\13\u0108\n\13\3\13\2\3\4\f\2\4\6\b\n\f\16\20\22"+
		"\24\2\6\3\2\"#\6\2\6\7\t\t\23\24\27\27\5\2\7\7\30\30\34\35\3\2%&\u0136"+
		"\2\26\3\2\2\2\4\u00c2\3\2\2\2\6\u00d0\3\2\2\2\b\u00d2\3\2\2\2\n\u00d5"+
		"\3\2\2\2\f\u00e3\3\2\2\2\16\u00f1\3\2\2\2\20\u00fa\3\2\2\2\22\u0102\3"+
		"\2\2\2\24\u0107\3\2\2\2\26\27\5\4\3\2\27\30\7\2\2\3\30\3\3\2\2\2\31\32"+
		"\b\3\1\2\32\33\7\25\2\2\33\34\5\b\5\2\34\35\5\f\7\2\35\u00c3\3\2\2\2\36"+
		"\37\7!\2\2\37 \5\6\4\2 !\7%\2\2!\u00c3\3\2\2\2\"#\7!\2\2#$\5\b\5\2$%\5"+
		"\16\b\2%\u00c3\3\2\2\2&\'\7!\2\2\'(\5\b\5\2()\5\n\6\2)\u00c3\3\2\2\2*"+
		"+\7\13\2\2+,\5\b\5\2,-\5\f\7\2-\u00c3\3\2\2\2./\7\3\2\2/\60\5\b\5\2\60"+
		"\61\5\f\7\2\61\u00c3\3\2\2\2\62\63\7\26\2\2\63\64\5\b\5\2\64\65\5\f\7"+
		"\2\65\u00c3\3\2\2\2\66\67\7\37\2\2\678\5\6\4\289\7%\2\29\u00c3\3\2\2\2"+
		":;\7\37\2\2;<\5\b\5\2<=\5\16\b\2=\u00c3\3\2\2\2>?\7\37\2\2?@\5\b\5\2@"+
		"A\5\n\6\2A\u00c3\3\2\2\2BC\7\b\2\2CD\5\6\4\2DE\7%\2\2E\u00c3\3\2\2\2F"+
		"G\7\b\2\2GH\5\b\5\2HI\5\16\b\2I\u00c3\3\2\2\2JK\7\b\2\2KL\5\b\5\2LM\5"+
		"\n\6\2M\u00c3\3\2\2\2NO\7\36\2\2OP\5\6\4\2PQ\7%\2\2Q\u00c3\3\2\2\2RS\7"+
		"\36\2\2ST\5\b\5\2TU\5\16\b\2U\u00c3\3\2\2\2VW\7\36\2\2WX\5\b\5\2XY\5\n"+
		"\6\2Y\u00c3\3\2\2\2Z[\7\33\2\2[\\\5\6\4\2\\]\5\24\13\2]\u00c3\3\2\2\2"+
		"^_\7\33\2\2_`\5\b\5\2`a\5\16\b\2a\u00c3\3\2\2\2bc\7\33\2\2cd\5\b\5\2d"+
		"e\5\n\6\2e\u00c3\3\2\2\2fg\7\33\2\2gh\5\b\5\2hi\5\f\7\2i\u00c3\3\2\2\2"+
		"jk\7\22\2\2kl\5\6\4\2lm\5\24\13\2m\u00c3\3\2\2\2no\7\22\2\2op\5\b\5\2"+
		"pq\5\16\b\2q\u00c3\3\2\2\2rs\7\22\2\2st\5\b\5\2tu\5\n\6\2u\u00c3\3\2\2"+
		"\2vw\7\22\2\2wx\5\b\5\2xy\5\f\7\2y\u00c3\3\2\2\2z{\7\5\2\2{|\5\6\4\2|"+
		"}\5\24\13\2}\u00c3\3\2\2\2~\177\7\5\2\2\177\u0080\5\b\5\2\u0080\u0081"+
		"\5\f\7\2\u0081\u00c3\3\2\2\2\u0082\u0083\7\31\2\2\u0083\u0084\5\6\4\2"+
		"\u0084\u0085\5\24\13\2\u0085\u00c3\3\2\2\2\u0086\u0087\7\31\2\2\u0087"+
		"\u0088\5\b\5\2\u0088\u0089\5\f\7\2\u0089\u00c3\3\2\2\2\u008a\u008b\7\21"+
		"\2\2\u008b\u008c\5\6\4\2\u008c\u008d\5\22\n\2\u008d\u00c3\3\2\2\2\u008e"+
		"\u008f\7\21\2\2\u008f\u0090\5\b\5\2\u0090\u0091\5\20\t\2\u0091\u00c3\3"+
		"\2\2\2\u0092\u0093\7\16\2\2\u0093\u0094\5\6\4\2\u0094\u0095\5\22\n\2\u0095"+
		"\u00c3\3\2\2\2\u0096\u0097\7\16\2\2\u0097\u0098\5\b\5\2\u0098\u0099\5"+
		"\20\t\2\u0099\u00c3\3\2\2\2\u009a\u009b\7\4\2\2\u009b\u009c\5\6\4\2\u009c"+
		"\u009d\5\22\n\2\u009d\u00c3\3\2\2\2\u009e\u009f\7\4\2\2\u009f\u00a0\5"+
		"\b\5\2\u00a0\u00a1\5\20\t\2\u00a1\u00c3\3\2\2\2\u00a2\u00a3\7\32\2\2\u00a3"+
		"\u00a4\5\6\4\2\u00a4\u00a5\5\22\n\2\u00a5\u00c3\3\2\2\2\u00a6\u00a7\7"+
		"\32\2\2\u00a7\u00a8\5\b\5\2\u00a8\u00a9\5\20\t\2\u00a9\u00c3\3\2\2\2\u00aa"+
		"\u00ab\7\n\2\2\u00ab\u00ac\5\6\4\2\u00ac\u00ad\5\22\n\2\u00ad\u00c3\3"+
		"\2\2\2\u00ae\u00af\7\n\2\2\u00af\u00b0\5\b\5\2\u00b0\u00b1\5\20\t\2\u00b1"+
		"\u00c3\3\2\2\2\u00b2\u00b3\7\20\2\2\u00b3\u00b4\5\6\4\2\u00b4\u00b5\5"+
		"\22\n\2\u00b5\u00c3\3\2\2\2\u00b6\u00b7\7\20\2\2\u00b7\u00b8\5\b\5\2\u00b8"+
		"\u00b9\5\20\t\2\u00b9\u00c3\3\2\2\2\u00ba\u00bb\7\17\2\2\u00bb\u00bc\5"+
		"\b\5\2\u00bc\u00bd\5\f\7\2\u00bd\u00c3\3\2\2\2\u00be\u00bf\7\f\2\2\u00bf"+
		"\u00c0\5\4\3\2\u00c0\u00c1\7 \2\2\u00c1\u00c3\3\2\2\2\u00c2\31\3\2\2\2"+
		"\u00c2\36\3\2\2\2\u00c2\"\3\2\2\2\u00c2&\3\2\2\2\u00c2*\3\2\2\2\u00c2"+
		".\3\2\2\2\u00c2\62\3\2\2\2\u00c2\66\3\2\2\2\u00c2:\3\2\2\2\u00c2>\3\2"+
		"\2\2\u00c2B\3\2\2\2\u00c2F\3\2\2\2\u00c2J\3\2\2\2\u00c2N\3\2\2\2\u00c2"+
		"R\3\2\2\2\u00c2V\3\2\2\2\u00c2Z\3\2\2\2\u00c2^\3\2\2\2\u00c2b\3\2\2\2"+
		"\u00c2f\3\2\2\2\u00c2j\3\2\2\2\u00c2n\3\2\2\2\u00c2r\3\2\2\2\u00c2v\3"+
		"\2\2\2\u00c2z\3\2\2\2\u00c2~\3\2\2\2\u00c2\u0082\3\2\2\2\u00c2\u0086\3"+
		"\2\2\2\u00c2\u008a\3\2\2\2\u00c2\u008e\3\2\2\2\u00c2\u0092\3\2\2\2\u00c2"+
		"\u0096\3\2\2\2\u00c2\u009a\3\2\2\2\u00c2\u009e\3\2\2\2\u00c2\u00a2\3\2"+
		"\2\2\u00c2\u00a6\3\2\2\2\u00c2\u00aa\3\2\2\2\u00c2\u00ae\3\2\2\2\u00c2"+
		"\u00b2\3\2\2\2\u00c2\u00b6\3\2\2\2\u00c2\u00ba\3\2\2\2\u00c2\u00be\3\2"+
		"\2\2\u00c3\u00cd\3\2\2\2\u00c4\u00c7\f\4\2\2\u00c5\u00c6\t\2\2\2\u00c6"+
		"\u00c8\5\4\3\2\u00c7\u00c5\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00c7\3\2"+
		"\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00c4\3\2\2\2\u00cc"+
		"\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\5\3\2\2\2"+
		"\u00cf\u00cd\3\2\2\2\u00d0\u00d1\t\3\2\2\u00d1\7\3\2\2\2\u00d2\u00d3\t"+
		"\4\2\2\u00d3\t\3\2\2\2\u00d4\u00d6\7\f\2\2\u00d5\u00d4\3\2\2\2\u00d5\u00d6"+
		"\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00dc\7%\2\2\u00d8\u00d9\7\r\2\2\u00d9"+
		"\u00db\7%\2\2\u00da\u00d8\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2"+
		"\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00e0\3\2\2\2\u00de\u00dc\3\2\2\2\u00df"+
		"\u00e1\7 \2\2\u00e0\u00df\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\13\3\2\2\2"+
		"\u00e2\u00e4\7\f\2\2\u00e3\u00e2\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e5"+
		"\3\2\2\2\u00e5\u00ea\5\24\13\2\u00e6\u00e7\7\r\2\2\u00e7\u00e9\5\24\13"+
		"\2\u00e8\u00e6\3\2\2\2\u00e9\u00ec\3\2\2\2\u00ea\u00e8\3\2\2\2\u00ea\u00eb"+
		"\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ed\u00ef\7 \2\2\u00ee"+
		"\u00ed\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\r\3\2\2\2\u00f0\u00f2\7\f\2\2"+
		"\u00f1\u00f0\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f4"+
		"\7%\2\2\u00f4\u00f5\7$\2\2\u00f5\u00f7\7%\2\2\u00f6\u00f8\7 \2\2\u00f7"+
		"\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\17\3\2\2\2\u00f9\u00fb\7\f\2"+
		"\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fd"+
		"\5\22\n\2\u00fd\u00fe\7$\2\2\u00fe\u0100\5\22\n\2\u00ff\u0101\7 \2\2\u0100"+
		"\u00ff\3\2\2\2\u0100\u0101\3\2\2\2\u0101\21\3\2\2\2\u0102\u0103\t\5\2"+
		"\2\u0103\23\3\2\2\2\u0104\u0108\5\22\n\2\u0105\u0108\7\'\2\2\u0106\u0108"+
		"\7(\2\2\u0107\u0104\3\2\2\2\u0107\u0105\3\2\2\2\u0107\u0106\3\2\2\2\u0108"+
		"\25\3\2\2\2\20\u00c2\u00c9\u00cd\u00d5\u00dc\u00e0\u00e3\u00ea\u00ee\u00f1"+
		"\u00f7\u00fa\u0100\u0107";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}