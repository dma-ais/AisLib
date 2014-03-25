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
		T__33=1, T__32=2, T__31=3, T__30=4, T__29=5, T__28=6, T__27=7, T__26=8, 
		T__25=9, T__24=10, T__23=11, T__22=12, T__21=13, T__20=14, T__19=15, T__18=16, 
		T__17=17, T__16=18, T__15=19, T__14=20, T__13=21, T__12=22, T__11=23, 
		T__10=24, T__9=25, T__8=26, T__7=27, T__6=28, T__5=29, T__4=30, T__3=31, 
		T__2=32, T__1=33, T__0=34, AND=35, OR=36, RANGE=37, INT=38, FLOAT=39, 
		WORD=40, STRING=41, WS=42;
	public static final String[] tokenNames = {
		"<INVALID>", "'s.type'", "'m.lat'", "'m.name'", "'!='", "'='", "'m.mmsi'", 
		"'<='", "'m.hdg'", "'s.country'", "'('", "','", "'m.cog'", "'NOT IN'", 
		"'not in'", "'messagetype'", "'m.draught'", "'m.sog'", "'m.navstat'", 
		"'!@'", "'>='", "'<'", "'s.id'", "'s.region'", "'>'", "'@'", "'m.cs'", 
		"'m.lon'", "'m.type'", "'IN'", "'in'", "'m.imo'", "'m.id'", "')'", "'s.bs'", 
		"'&'", "'|'", "'..'", "INT", "FLOAT", "WORD", "STRING", "WS"
	};
	public static final int
		RULE_prog = 0, RULE_filterExpression = 1, RULE_compareTo = 2, RULE_in = 3, 
		RULE_notin = 4, RULE_intList = 5, RULE_stringList = 6, RULE_intRange = 7, 
		RULE_numberRange = 8, RULE_number = 9, RULE_string = 10;
	public static final String[] ruleNames = {
		"prog", "filterExpression", "compareTo", "in", "notin", "intList", "stringList", 
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
			setState(22); filterExpression(0);
			setState(23); match(EOF);
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
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
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
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
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
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
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
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
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
			setState(206);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				_localctx = new SourceIdInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(26); match(22);
				setState(29);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(27); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(28); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(31); stringList();
				}
				break;

			case 2:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(33); match(34);
				setState(34); compareTo();
				setState(35); match(INT);
				}
				break;

			case 3:
				{
				_localctx = new SourceBasestationInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(37); match(34);
				setState(38); in();
				setState(39); intRange();
				}
				break;

			case 4:
				{
				_localctx = new SourceBasestationInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(41); match(34);
				setState(42); in();
				setState(43); intList();
				}
				break;

			case 5:
				{
				_localctx = new SourceCountryInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(45); match(9);
				setState(48);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(46); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(47); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(50); stringList();
				}
				break;

			case 6:
				{
				_localctx = new SourceTypeInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(52); match(1);
				setState(55);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(53); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(54); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(57); stringList();
				}
				break;

			case 7:
				{
				_localctx = new SourceRegionInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(59); match(23);
				setState(62);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(60); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(61); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(64); stringList();
				}
				break;

			case 8:
				{
				_localctx = new MessageIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(66); match(32);
				setState(67); compareTo();
				setState(68); match(INT);
				}
				break;

			case 9:
				{
				_localctx = new MessageIdInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(70); match(32);
				setState(71); in();
				setState(72); intRange();
				}
				break;

			case 10:
				{
				_localctx = new MessageIdInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(74); match(32);
				setState(75); in();
				setState(76); intList();
				}
				break;

			case 11:
				{
				_localctx = new MessageMmsiContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(78); match(6);
				setState(79); compareTo();
				setState(80); match(INT);
				}
				break;

			case 12:
				{
				_localctx = new MessageMmsiInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(82); match(6);
				setState(83); in();
				setState(84); intRange();
				}
				break;

			case 13:
				{
				_localctx = new MessageMmsiInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(86); match(6);
				setState(87); in();
				setState(88); intList();
				}
				break;

			case 14:
				{
				_localctx = new MessageImoContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(90); match(31);
				setState(91); compareTo();
				setState(92); match(INT);
				}
				break;

			case 15:
				{
				_localctx = new MessageImoInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(94); match(31);
				setState(95); in();
				setState(96); intRange();
				}
				break;

			case 16:
				{
				_localctx = new MessageImoInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(98); match(31);
				setState(99); in();
				setState(100); intList();
				}
				break;

			case 17:
				{
				_localctx = new MessageShiptypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(102); match(28);
				setState(103); compareTo();
				setState(104); string();
				}
				break;

			case 18:
				{
				_localctx = new MessageShiptypeInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(106); match(28);
				setState(107); in();
				setState(108); intRange();
				}
				break;

			case 19:
				{
				_localctx = new MessageShiptypeInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(110); match(28);
				setState(111); in();
				setState(112); intList();
				}
				break;

			case 20:
				{
				_localctx = new MessageShiptypeInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(114); match(28);
				setState(115); in();
				setState(116); stringList();
				}
				break;

			case 21:
				{
				_localctx = new MessageNavigationalStatusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(118); match(18);
				setState(119); compareTo();
				setState(120); string();
				}
				break;

			case 22:
				{
				_localctx = new MessageNavigationalStatusInIntRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(122); match(18);
				setState(123); in();
				setState(124); intRange();
				}
				break;

			case 23:
				{
				_localctx = new MessageNavigationalStatusInIntListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(126); match(18);
				setState(127); in();
				setState(128); intList();
				}
				break;

			case 24:
				{
				_localctx = new MessageNavigationalStatusInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(130); match(18);
				setState(131); in();
				setState(132); stringList();
				}
				break;

			case 25:
				{
				_localctx = new MessageNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(134); match(3);
				setState(135); compareTo();
				setState(136); string();
				}
				break;

			case 26:
				{
				_localctx = new MessageNameInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(138); match(3);
				setState(139); in();
				setState(140); stringList();
				}
				break;

			case 27:
				{
				_localctx = new MessageCallsignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(142); match(26);
				setState(143); compareTo();
				setState(144); string();
				}
				break;

			case 28:
				{
				_localctx = new MessageCallsignInStringListContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(146); match(26);
				setState(147); in();
				setState(148); stringList();
				}
				break;

			case 29:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(150); match(17);
				setState(151); compareTo();
				setState(152); number();
				}
				break;

			case 30:
				{
				_localctx = new MessageSpeedOverGroundInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(154); match(17);
				setState(155); in();
				setState(156); numberRange();
				}
				break;

			case 31:
				{
				_localctx = new MessageCourseOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(158); match(12);
				setState(159); compareTo();
				setState(160); number();
				}
				break;

			case 32:
				{
				_localctx = new MessageCourseOverGroundInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(162); match(12);
				setState(163); in();
				setState(164); numberRange();
				}
				break;

			case 33:
				{
				_localctx = new MessageLatitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(166); match(2);
				setState(167); compareTo();
				setState(168); number();
				}
				break;

			case 34:
				{
				_localctx = new MessageLatitudeInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(170); match(2);
				setState(171); in();
				setState(172); numberRange();
				}
				break;

			case 35:
				{
				_localctx = new MessageLongitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(174); match(27);
				setState(175); compareTo();
				setState(176); number();
				}
				break;

			case 36:
				{
				_localctx = new MessageLongitudeInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(178); match(27);
				setState(179); in();
				setState(180); numberRange();
				}
				break;

			case 37:
				{
				_localctx = new MessageTrueHeadingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(182); match(8);
				setState(183); compareTo();
				setState(184); number();
				}
				break;

			case 38:
				{
				_localctx = new MessageTrueHeadingInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(186); match(8);
				setState(187); in();
				setState(188); numberRange();
				}
				break;

			case 39:
				{
				_localctx = new MessageDraughtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(190); match(16);
				setState(191); compareTo();
				setState(192); number();
				}
				break;

			case 40:
				{
				_localctx = new MessageDraughtInNumberRangeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(194); match(16);
				setState(195); in();
				setState(196); numberRange();
				}
				break;

			case 41:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(198); match(15);
				setState(199); in();
				setState(200); stringList();
				}
				break;

			case 42:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(202); match(10);
				setState(203); filterExpression(0);
				setState(204); match(33);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(217);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrAndContext(new FilterExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_filterExpression);
					setState(208);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(211); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(209);
							((OrAndContext)_localctx).op = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==AND || _la==OR) ) {
								((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
							}
							consume();
							setState(210); filterExpression(0);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(213); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
					} while ( _alt!=2 && _alt!=-1 );
					}
					} 
				}
				setState(219);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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
			setState(220);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 5) | (1L << 7) | (1L << 20) | (1L << 21) | (1L << 24))) != 0)) ) {
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
			setState(222);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 25) | (1L << 29) | (1L << 30))) != 0)) ) {
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

	public static class NotinContext extends ParserRuleContext {
		public NotinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notin; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitNotin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotinContext notin() throws RecognitionException {
		NotinContext _localctx = new NotinContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_notin);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 13) | (1L << 14) | (1L << 19))) != 0)) ) {
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
		enterRule(_localctx, 10, RULE_intList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(226); match(10);
				}
			}

			setState(229); match(INT);
			setState(234);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(230); match(11);
					setState(231); match(INT);
					}
					} 
				}
				setState(236);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(238);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(237); match(33);
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
		enterRule(_localctx, 12, RULE_stringList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(241);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(240); match(10);
				}
			}

			setState(243); string();
			setState(248);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(244); match(11);
					setState(245); string();
					}
					} 
				}
				setState(250);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(252);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(251); match(33);
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
		enterRule(_localctx, 14, RULE_intRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(254); match(10);
				}
			}

			setState(257); match(INT);
			setState(258); match(RANGE);
			setState(259); match(INT);
			setState(261);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(260); match(33);
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
		enterRule(_localctx, 16, RULE_numberRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(264);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(263); match(10);
				}
			}

			setState(266); number();
			setState(267); match(RANGE);
			setState(268); number();
			setState(270);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(269); match(33);
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
		enterRule(_localctx, 18, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(272);
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
		enterRule(_localctx, 20, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
				{
				setState(274); number();
				}
				break;
			case WORD:
				{
				setState(275); match(WORD);
				}
				break;
			case STRING:
				{
				setState(276); match(STRING);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3,\u011a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\3\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3 \n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\63\n\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3:\n\3\3\3\3\3\3\3\3\3\3\3\5\3A\n\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00d1\n\3\3"+
		"\3\3\3\3\3\6\3\u00d6\n\3\r\3\16\3\u00d7\7\3\u00da\n\3\f\3\16\3\u00dd\13"+
		"\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\5\7\u00e6\n\7\3\7\3\7\3\7\7\7\u00eb\n\7"+
		"\f\7\16\7\u00ee\13\7\3\7\5\7\u00f1\n\7\3\b\5\b\u00f4\n\b\3\b\3\b\3\b\7"+
		"\b\u00f9\n\b\f\b\16\b\u00fc\13\b\3\b\5\b\u00ff\n\b\3\t\5\t\u0102\n\t\3"+
		"\t\3\t\3\t\3\t\5\t\u0108\n\t\3\n\5\n\u010b\n\n\3\n\3\n\3\n\3\n\5\n\u0111"+
		"\n\n\3\13\3\13\3\f\3\f\3\f\5\f\u0118\n\f\3\f\2\3\4\r\2\4\6\b\n\f\16\20"+
		"\22\24\26\2\7\3\2%&\6\2\6\7\t\t\26\27\32\32\5\2\7\7\33\33\37 \5\2\6\6"+
		"\17\20\25\25\3\2()\u0149\2\30\3\2\2\2\4\u00d0\3\2\2\2\6\u00de\3\2\2\2"+
		"\b\u00e0\3\2\2\2\n\u00e2\3\2\2\2\f\u00e5\3\2\2\2\16\u00f3\3\2\2\2\20\u0101"+
		"\3\2\2\2\22\u010a\3\2\2\2\24\u0112\3\2\2\2\26\u0117\3\2\2\2\30\31\5\4"+
		"\3\2\31\32\7\2\2\3\32\3\3\2\2\2\33\34\b\3\1\2\34\37\7\30\2\2\35 \5\b\5"+
		"\2\36 \5\n\6\2\37\35\3\2\2\2\37\36\3\2\2\2 !\3\2\2\2!\"\5\16\b\2\"\u00d1"+
		"\3\2\2\2#$\7$\2\2$%\5\6\4\2%&\7(\2\2&\u00d1\3\2\2\2\'(\7$\2\2()\5\b\5"+
		"\2)*\5\20\t\2*\u00d1\3\2\2\2+,\7$\2\2,-\5\b\5\2-.\5\f\7\2.\u00d1\3\2\2"+
		"\2/\62\7\13\2\2\60\63\5\b\5\2\61\63\5\n\6\2\62\60\3\2\2\2\62\61\3\2\2"+
		"\2\63\64\3\2\2\2\64\65\5\16\b\2\65\u00d1\3\2\2\2\669\7\3\2\2\67:\5\b\5"+
		"\28:\5\n\6\29\67\3\2\2\298\3\2\2\2:;\3\2\2\2;<\5\16\b\2<\u00d1\3\2\2\2"+
		"=@\7\31\2\2>A\5\b\5\2?A\5\n\6\2@>\3\2\2\2@?\3\2\2\2AB\3\2\2\2BC\5\16\b"+
		"\2C\u00d1\3\2\2\2DE\7\"\2\2EF\5\6\4\2FG\7(\2\2G\u00d1\3\2\2\2HI\7\"\2"+
		"\2IJ\5\b\5\2JK\5\20\t\2K\u00d1\3\2\2\2LM\7\"\2\2MN\5\b\5\2NO\5\f\7\2O"+
		"\u00d1\3\2\2\2PQ\7\b\2\2QR\5\6\4\2RS\7(\2\2S\u00d1\3\2\2\2TU\7\b\2\2U"+
		"V\5\b\5\2VW\5\20\t\2W\u00d1\3\2\2\2XY\7\b\2\2YZ\5\b\5\2Z[\5\f\7\2[\u00d1"+
		"\3\2\2\2\\]\7!\2\2]^\5\6\4\2^_\7(\2\2_\u00d1\3\2\2\2`a\7!\2\2ab\5\b\5"+
		"\2bc\5\20\t\2c\u00d1\3\2\2\2de\7!\2\2ef\5\b\5\2fg\5\f\7\2g\u00d1\3\2\2"+
		"\2hi\7\36\2\2ij\5\6\4\2jk\5\26\f\2k\u00d1\3\2\2\2lm\7\36\2\2mn\5\b\5\2"+
		"no\5\20\t\2o\u00d1\3\2\2\2pq\7\36\2\2qr\5\b\5\2rs\5\f\7\2s\u00d1\3\2\2"+
		"\2tu\7\36\2\2uv\5\b\5\2vw\5\16\b\2w\u00d1\3\2\2\2xy\7\24\2\2yz\5\6\4\2"+
		"z{\5\26\f\2{\u00d1\3\2\2\2|}\7\24\2\2}~\5\b\5\2~\177\5\20\t\2\177\u00d1"+
		"\3\2\2\2\u0080\u0081\7\24\2\2\u0081\u0082\5\b\5\2\u0082\u0083\5\f\7\2"+
		"\u0083\u00d1\3\2\2\2\u0084\u0085\7\24\2\2\u0085\u0086\5\b\5\2\u0086\u0087"+
		"\5\16\b\2\u0087\u00d1\3\2\2\2\u0088\u0089\7\5\2\2\u0089\u008a\5\6\4\2"+
		"\u008a\u008b\5\26\f\2\u008b\u00d1\3\2\2\2\u008c\u008d\7\5\2\2\u008d\u008e"+
		"\5\b\5\2\u008e\u008f\5\16\b\2\u008f\u00d1\3\2\2\2\u0090\u0091\7\34\2\2"+
		"\u0091\u0092\5\6\4\2\u0092\u0093\5\26\f\2\u0093\u00d1\3\2\2\2\u0094\u0095"+
		"\7\34\2\2\u0095\u0096\5\b\5\2\u0096\u0097\5\16\b\2\u0097\u00d1\3\2\2\2"+
		"\u0098\u0099\7\23\2\2\u0099\u009a\5\6\4\2\u009a\u009b\5\24\13\2\u009b"+
		"\u00d1\3\2\2\2\u009c\u009d\7\23\2\2\u009d\u009e\5\b\5\2\u009e\u009f\5"+
		"\22\n\2\u009f\u00d1\3\2\2\2\u00a0\u00a1\7\16\2\2\u00a1\u00a2\5\6\4\2\u00a2"+
		"\u00a3\5\24\13\2\u00a3\u00d1\3\2\2\2\u00a4\u00a5\7\16\2\2\u00a5\u00a6"+
		"\5\b\5\2\u00a6\u00a7\5\22\n\2\u00a7\u00d1\3\2\2\2\u00a8\u00a9\7\4\2\2"+
		"\u00a9\u00aa\5\6\4\2\u00aa\u00ab\5\24\13\2\u00ab\u00d1\3\2\2\2\u00ac\u00ad"+
		"\7\4\2\2\u00ad\u00ae\5\b\5\2\u00ae\u00af\5\22\n\2\u00af\u00d1\3\2\2\2"+
		"\u00b0\u00b1\7\35\2\2\u00b1\u00b2\5\6\4\2\u00b2\u00b3\5\24\13\2\u00b3"+
		"\u00d1\3\2\2\2\u00b4\u00b5\7\35\2\2\u00b5\u00b6\5\b\5\2\u00b6\u00b7\5"+
		"\22\n\2\u00b7\u00d1\3\2\2\2\u00b8\u00b9\7\n\2\2\u00b9\u00ba\5\6\4\2\u00ba"+
		"\u00bb\5\24\13\2\u00bb\u00d1\3\2\2\2\u00bc\u00bd\7\n\2\2\u00bd\u00be\5"+
		"\b\5\2\u00be\u00bf\5\22\n\2\u00bf\u00d1\3\2\2\2\u00c0\u00c1\7\22\2\2\u00c1"+
		"\u00c2\5\6\4\2\u00c2\u00c3\5\24\13\2\u00c3\u00d1\3\2\2\2\u00c4\u00c5\7"+
		"\22\2\2\u00c5\u00c6\5\b\5\2\u00c6\u00c7\5\22\n\2\u00c7\u00d1\3\2\2\2\u00c8"+
		"\u00c9\7\21\2\2\u00c9\u00ca\5\b\5\2\u00ca\u00cb\5\16\b\2\u00cb\u00d1\3"+
		"\2\2\2\u00cc\u00cd\7\f\2\2\u00cd\u00ce\5\4\3\2\u00ce\u00cf\7#\2\2\u00cf"+
		"\u00d1\3\2\2\2\u00d0\33\3\2\2\2\u00d0#\3\2\2\2\u00d0\'\3\2\2\2\u00d0+"+
		"\3\2\2\2\u00d0/\3\2\2\2\u00d0\66\3\2\2\2\u00d0=\3\2\2\2\u00d0D\3\2\2\2"+
		"\u00d0H\3\2\2\2\u00d0L\3\2\2\2\u00d0P\3\2\2\2\u00d0T\3\2\2\2\u00d0X\3"+
		"\2\2\2\u00d0\\\3\2\2\2\u00d0`\3\2\2\2\u00d0d\3\2\2\2\u00d0h\3\2\2\2\u00d0"+
		"l\3\2\2\2\u00d0p\3\2\2\2\u00d0t\3\2\2\2\u00d0x\3\2\2\2\u00d0|\3\2\2\2"+
		"\u00d0\u0080\3\2\2\2\u00d0\u0084\3\2\2\2\u00d0\u0088\3\2\2\2\u00d0\u008c"+
		"\3\2\2\2\u00d0\u0090\3\2\2\2\u00d0\u0094\3\2\2\2\u00d0\u0098\3\2\2\2\u00d0"+
		"\u009c\3\2\2\2\u00d0\u00a0\3\2\2\2\u00d0\u00a4\3\2\2\2\u00d0\u00a8\3\2"+
		"\2\2\u00d0\u00ac\3\2\2\2\u00d0\u00b0\3\2\2\2\u00d0\u00b4\3\2\2\2\u00d0"+
		"\u00b8\3\2\2\2\u00d0\u00bc\3\2\2\2\u00d0\u00c0\3\2\2\2\u00d0\u00c4\3\2"+
		"\2\2\u00d0\u00c8\3\2\2\2\u00d0\u00cc\3\2\2\2\u00d1\u00db\3\2\2\2\u00d2"+
		"\u00d5\f\4\2\2\u00d3\u00d4\t\2\2\2\u00d4\u00d6\5\4\3\2\u00d5\u00d3\3\2"+
		"\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8"+
		"\u00da\3\2\2\2\u00d9\u00d2\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2"+
		"\2\2\u00db\u00dc\3\2\2\2\u00dc\5\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00df"+
		"\t\3\2\2\u00df\7\3\2\2\2\u00e0\u00e1\t\4\2\2\u00e1\t\3\2\2\2\u00e2\u00e3"+
		"\t\5\2\2\u00e3\13\3\2\2\2\u00e4\u00e6\7\f\2\2\u00e5\u00e4\3\2\2\2\u00e5"+
		"\u00e6\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00ec\7(\2\2\u00e8\u00e9\7\r"+
		"\2\2\u00e9\u00eb\7(\2\2\u00ea\u00e8\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec"+
		"\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00f0\3\2\2\2\u00ee\u00ec\3\2"+
		"\2\2\u00ef\u00f1\7#\2\2\u00f0\u00ef\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1"+
		"\r\3\2\2\2\u00f2\u00f4\7\f\2\2\u00f3\u00f2\3\2\2\2\u00f3\u00f4\3\2\2\2"+
		"\u00f4\u00f5\3\2\2\2\u00f5\u00fa\5\26\f\2\u00f6\u00f7\7\r\2\2\u00f7\u00f9"+
		"\5\26\f\2\u00f8\u00f6\3\2\2\2\u00f9\u00fc\3\2\2\2\u00fa\u00f8\3\2\2\2"+
		"\u00fa\u00fb\3\2\2\2\u00fb\u00fe\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fd\u00ff"+
		"\7#\2\2\u00fe\u00fd\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\17\3\2\2\2\u0100"+
		"\u0102\7\f\2\2\u0101\u0100\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0103\3\2"+
		"\2\2\u0103\u0104\7(\2\2\u0104\u0105\7\'\2\2\u0105\u0107\7(\2\2\u0106\u0108"+
		"\7#\2\2\u0107\u0106\3\2\2\2\u0107\u0108\3\2\2\2\u0108\21\3\2\2\2\u0109"+
		"\u010b\7\f\2\2\u010a\u0109\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010c\3\2"+
		"\2\2\u010c\u010d\5\24\13\2\u010d\u010e\7\'\2\2\u010e\u0110\5\24\13\2\u010f"+
		"\u0111\7#\2\2\u0110\u010f\3\2\2\2\u0110\u0111\3\2\2\2\u0111\23\3\2\2\2"+
		"\u0112\u0113\t\6\2\2\u0113\25\3\2\2\2\u0114\u0118\5\24\13\2\u0115\u0118"+
		"\7*\2\2\u0116\u0118\7+\2\2\u0117\u0114\3\2\2\2\u0117\u0115\3\2\2\2\u0117"+
		"\u0116\3\2\2\2\u0118\27\3\2\2\2\24\37\629@\u00d0\u00d7\u00db\u00e5\u00ec"+
		"\u00f0\u00f3\u00fa\u00fe\u0101\u0107\u010a\u0110\u0117";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}