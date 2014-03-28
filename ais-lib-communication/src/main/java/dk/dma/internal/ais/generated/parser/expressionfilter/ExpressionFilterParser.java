// Generated from ExpressionFilter.g4 by ANTLR 4.2
package dk.dma.internal.ais.generated.parser.expressionfilter;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExpressionFilterParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__40=1, T__39=2, T__38=3, T__37=4, T__36=5, T__35=6, T__34=7, T__33=8, 
		T__32=9, T__31=10, T__30=11, T__29=12, T__28=13, T__27=14, T__26=15, T__25=16, 
		T__24=17, T__23=18, T__22=19, T__21=20, T__20=21, T__19=22, T__18=23, 
		T__17=24, T__16=25, T__15=26, T__14=27, T__13=28, T__12=29, T__11=30, 
		T__10=31, T__9=32, T__8=33, T__7=34, T__6=35, T__5=36, T__4=37, T__3=38, 
		T__2=39, T__1=40, T__0=41, AND=42, OR=43, RANGE=44, BBOX=45, CIRCLE=46, 
		WITHIN=47, INT=48, FLOAT=49, STRING=50, WS=51;
	public static final String[] tokenNames = {
		"<INVALID>", "'s.type'", "'m.lat'", "'m.name'", "'m.hour'", "'!='", "'='", 
		"'m.year'", "'m.mmsi'", "'<='", "'m.dow'", "'m.hdg'", "'m.dom'", "'s.country'", 
		"'('", "','", "'m.cog'", "'NOT IN'", "'not in'", "'messagetype'", "'m.draught'", 
		"'m.sog'", "'m.month'", "'m.navstat'", "'!@'", "'>='", "'<'", "'s.id'", 
		"'s.region'", "'>'", "'@'", "'m.cs'", "'m.pos'", "'m.lon'", "'m.type'", 
		"'IN'", "'m.minute'", "'in'", "'m.imo'", "'m.id'", "')'", "'s.bs'", "'&'", 
		"'|'", "'..'", "BBOX", "CIRCLE", "WITHIN", "INT", "FLOAT", "STRING", "WS"
	};
	public static final int
		RULE_filter = 0, RULE_filterExpression = 1, RULE_compareTo = 2, RULE_in = 3, 
		RULE_notin = 4, RULE_intList = 5, RULE_stringList = 6, RULE_intRange = 7, 
		RULE_numberRange = 8, RULE_number = 9, RULE_string = 10, RULE_bbox = 11, 
		RULE_circle = 12;
	public static final String[] ruleNames = {
		"filter", "filterExpression", "compareTo", "in", "notin", "intList", "stringList", 
		"intRange", "numberRange", "number", "string", "bbox", "circle"
	};

	@Override
	public String getGrammarFileName() { return "ExpressionFilter.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ExpressionFilterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FilterContext extends ParserRuleContext {
		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ExpressionFilterParser.EOF, 0); }
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_filter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26); filterExpression(0);
			setState(27); match(EOF);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageTrueHeadingContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(ExpressionFilterParser.INT, 0); }
		public MessageTrueHeadingContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageTrueHeading(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageTimeContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public TerminalNode INT() { return getToken(ExpressionFilterParser.INT, 0); }
		public MessageTimeContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageTime(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageTrueHeadingInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageTrueHeadingInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageTrueHeadingIn(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitParens(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageLatitude(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageDraughtInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageDraughtInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageDraughtIn(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageNavigationalStatus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceBasestationContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(ExpressionFilterParser.INT, 0); }
		public SourceBasestationContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitSourceBasestation(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageCourseOverGround(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessagePositionInsideContext extends FilterExpressionContext {
		public BboxContext bbox() {
			return getRuleContext(BboxContext.class,0);
		}
		public CircleContext circle() {
			return getRuleContext(CircleContext.class,0);
		}
		public TerminalNode WITHIN() { return getToken(ExpressionFilterParser.WITHIN, 0); }
		public MessagePositionInsideContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessagePositionInside(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageShiptypeInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageShiptypeInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageShiptypeIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceBasestationInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public SourceBasestationInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitSourceBasestationIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageImoInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageImoInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageImoIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNavigationalStatusInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageNavigationalStatusInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageNavigationalStatusIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrAndContext extends FilterExpressionContext {
		public Token op;
		public TerminalNode AND(int i) {
			return getToken(ExpressionFilterParser.AND, i);
		}
		public List<FilterExpressionContext> filterExpression() {
			return getRuleContexts(FilterExpressionContext.class);
		}
		public FilterExpressionContext filterExpression(int i) {
			return getRuleContext(FilterExpressionContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(ExpressionFilterParser.AND); }
		public List<TerminalNode> OR() { return getTokens(ExpressionFilterParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(ExpressionFilterParser.OR, i);
		}
		public OrAndContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitOrAnd(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitAisMessagetype(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceIdInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public SourceIdInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitSourceIdIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceRegionInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public SourceRegionInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitSourceRegionIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageCallsignInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageCallsignInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageCallsignIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageLongitudeInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageLongitudeInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageLongitudeIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceTypeInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public SourceTypeInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitSourceTypeIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageLatitudeInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageLatitudeInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageLatitudeIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(ExpressionFilterParser.INT, 0); }
		public MessageIdContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageId(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageCallsign(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageSpeedOverGround(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageNameInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageNameInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageNameIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageSpeedOverGroundInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageSpeedOverGroundInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageSpeedOverGroundIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageMmsiContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(ExpressionFilterParser.INT, 0); }
		public MessageMmsiContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageMmsi(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageCourseOverGroundInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public NumberRangeContext numberRange() {
			return getRuleContext(NumberRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public MessageCourseOverGroundInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageCourseOverGroundIn(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageLongitude(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageDraught(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageTimeInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageTimeInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageTimeIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageMmsiInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageMmsiInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageMmsiIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceCountryInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public StringListContext stringList() {
			return getRuleContext(StringListContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public SourceCountryInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitSourceCountryIn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageIdInContext extends FilterExpressionContext {
		public InContext in() {
			return getRuleContext(InContext.class,0);
		}
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public NotinContext notin() {
			return getRuleContext(NotinContext.class,0);
		}
		public IntListContext intList() {
			return getRuleContext(IntListContext.class,0);
		}
		public MessageIdInContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageIdIn(this);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageShiptype(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MessageImoContext extends FilterExpressionContext {
		public CompareToContext compareTo() {
			return getRuleContext(CompareToContext.class,0);
		}
		public TerminalNode INT() { return getToken(ExpressionFilterParser.INT, 0); }
		public MessageImoContext(FilterExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitMessageImo(this);
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
			setState(256);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				_localctx = new SourceIdInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(30); match(27);
				setState(33);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(31); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(32); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(35); stringList();
				}
				break;

			case 2:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(37); match(41);
				setState(38); compareTo();
				setState(39); match(INT);
				}
				break;

			case 3:
				{
				_localctx = new SourceBasestationInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(41); match(41);
				setState(44);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(42); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(43); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(48);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(46); intRange();
					}
					break;

				case 2:
					{
					setState(47); intList();
					}
					break;
				}
				}
				break;

			case 4:
				{
				_localctx = new SourceCountryInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(50); match(13);
				setState(53);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(51); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(52); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(55); stringList();
				}
				break;

			case 5:
				{
				_localctx = new SourceTypeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(57); match(1);
				setState(60);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(58); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(59); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(62); stringList();
				}
				break;

			case 6:
				{
				_localctx = new SourceRegionInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64); match(28);
				setState(67);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(65); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(66); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(69); stringList();
				}
				break;

			case 7:
				{
				_localctx = new MessageIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(71); match(39);
				setState(72); compareTo();
				setState(73); match(INT);
				}
				break;

			case 8:
				{
				_localctx = new MessageIdInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(75); match(39);
				setState(78);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(76); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(77); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(82);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(80); intRange();
					}
					break;

				case 2:
					{
					setState(81); intList();
					}
					break;
				}
				}
				break;

			case 9:
				{
				_localctx = new MessageMmsiContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84); match(8);
				setState(85); compareTo();
				setState(86); match(INT);
				}
				break;

			case 10:
				{
				_localctx = new MessageMmsiInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(88); match(8);
				setState(91);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(89); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(90); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(95);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(93); intRange();
					}
					break;

				case 2:
					{
					setState(94); intList();
					}
					break;
				}
				}
				break;

			case 11:
				{
				_localctx = new MessageImoContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(97); match(38);
				setState(98); compareTo();
				setState(99); match(INT);
				}
				break;

			case 12:
				{
				_localctx = new MessageImoInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(101); match(38);
				setState(104);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(102); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(103); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(108);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(106); intRange();
					}
					break;

				case 2:
					{
					setState(107); intList();
					}
					break;
				}
				}
				break;

			case 13:
				{
				_localctx = new MessageShiptypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(110); match(34);
				setState(111); compareTo();
				setState(112); string();
				}
				break;

			case 14:
				{
				_localctx = new MessageShiptypeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(114); match(34);
				setState(117);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(115); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(116); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(122);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(119); intRange();
					}
					break;

				case 2:
					{
					setState(120); intList();
					}
					break;

				case 3:
					{
					setState(121); stringList();
					}
					break;
				}
				}
				break;

			case 15:
				{
				_localctx = new MessageNavigationalStatusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(124); match(23);
				setState(125); compareTo();
				setState(126); string();
				}
				break;

			case 16:
				{
				_localctx = new MessageNavigationalStatusInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(128); match(23);
				setState(131);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(129); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(130); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(136);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(133); intRange();
					}
					break;

				case 2:
					{
					setState(134); intList();
					}
					break;

				case 3:
					{
					setState(135); stringList();
					}
					break;
				}
				}
				break;

			case 17:
				{
				_localctx = new MessageNameContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(138); match(3);
				setState(139); compareTo();
				setState(140); string();
				}
				break;

			case 18:
				{
				_localctx = new MessageNameInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(142); match(3);
				setState(145);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(143); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(144); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(147); stringList();
				}
				break;

			case 19:
				{
				_localctx = new MessageCallsignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(149); match(31);
				setState(150); compareTo();
				setState(151); string();
				}
				break;

			case 20:
				{
				_localctx = new MessageCallsignInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(153); match(31);
				setState(156);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(154); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(155); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(158); stringList();
				}
				break;

			case 21:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(160); match(21);
				setState(161); compareTo();
				setState(162); number();
				}
				break;

			case 22:
				{
				_localctx = new MessageSpeedOverGroundInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(164); match(21);
				setState(167);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(165); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(166); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(169); numberRange();
				}
				break;

			case 23:
				{
				_localctx = new MessageCourseOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(171); match(16);
				setState(172); compareTo();
				setState(173); number();
				}
				break;

			case 24:
				{
				_localctx = new MessageCourseOverGroundInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(175); match(16);
				setState(178);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(176); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(177); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(180); numberRange();
				}
				break;

			case 25:
				{
				_localctx = new MessageLatitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(182); match(2);
				setState(183); compareTo();
				setState(184); number();
				}
				break;

			case 26:
				{
				_localctx = new MessageLatitudeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(186); match(2);
				setState(189);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(187); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(188); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(191); numberRange();
				}
				break;

			case 27:
				{
				_localctx = new MessageLongitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(193); match(33);
				setState(194); compareTo();
				setState(195); number();
				}
				break;

			case 28:
				{
				_localctx = new MessageLongitudeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(197); match(33);
				setState(200);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(198); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(199); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(202); numberRange();
				}
				break;

			case 29:
				{
				_localctx = new MessagePositionInsideContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(204); match(32);
				setState(205); match(WITHIN);
				setState(208);
				switch (_input.LA(1)) {
				case CIRCLE:
					{
					setState(206); circle();
					}
					break;
				case BBOX:
					{
					setState(207); bbox();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;

			case 30:
				{
				_localctx = new MessageTrueHeadingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(210); match(11);
				setState(211); compareTo();
				setState(212); match(INT);
				}
				break;

			case 31:
				{
				_localctx = new MessageTrueHeadingInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(214); match(11);
				setState(217);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(215); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(216); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(219); intRange();
				}
				break;

			case 32:
				{
				_localctx = new MessageDraughtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(221); match(20);
				setState(222); compareTo();
				setState(223); number();
				}
				break;

			case 33:
				{
				_localctx = new MessageDraughtInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(225); match(20);
				setState(228);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(226); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(227); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(230); numberRange();
				}
				break;

			case 34:
				{
				_localctx = new MessageTimeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(232);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 7) | (1L << 10) | (1L << 12) | (1L << 22) | (1L << 36))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(233); compareTo();
				setState(236);
				switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
				case 1:
					{
					setState(234); match(INT);
					}
					break;

				case 2:
					{
					setState(235); string();
					}
					break;
				}
				}
				break;

			case 35:
				{
				_localctx = new MessageTimeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(238);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << 7) | (1L << 10) | (1L << 12) | (1L << 22) | (1L << 36))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(241);
				switch (_input.LA(1)) {
				case 6:
				case 30:
				case 35:
				case 37:
					{
					setState(239); in();
					}
					break;
				case 5:
				case 17:
				case 18:
				case 24:
					{
					setState(240); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(246);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(243); intRange();
					}
					break;

				case 2:
					{
					setState(244); intList();
					}
					break;

				case 3:
					{
					setState(245); stringList();
					}
					break;
				}
				}
				break;

			case 36:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(248); match(19);
				setState(249); in();
				setState(250); stringList();
				}
				break;

			case 37:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(252); match(14);
				setState(253); filterExpression(0);
				setState(254); match(40);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(267);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrAndContext(new FilterExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_filterExpression);
					setState(258);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(261); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(259);
							((OrAndContext)_localctx).op = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==AND || _la==OR) ) {
								((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
							}
							consume();
							setState(260); filterExpression(0);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(263); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
					} while ( _alt!=2 && _alt!=-1 );
					}
					} 
				}
				setState(269);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitCompareTo(this);
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
			setState(270);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 6) | (1L << 9) | (1L << 25) | (1L << 26) | (1L << 29))) != 0)) ) {
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitIn(this);
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
			setState(272);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 6) | (1L << 30) | (1L << 35) | (1L << 37))) != 0)) ) {
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitNotin(this);
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
			setState(274);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 17) | (1L << 18) | (1L << 24))) != 0)) ) {
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
			return getToken(ExpressionFilterParser.INT, i);
		}
		public List<TerminalNode> INT() { return getTokens(ExpressionFilterParser.INT); }
		public IntListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitIntList(this);
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
			setState(277);
			_la = _input.LA(1);
			if (_la==14) {
				{
				setState(276); match(14);
				}
			}

			setState(279); match(INT);
			setState(284);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(280); match(15);
					setState(281); match(INT);
					}
					} 
				}
				setState(286);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			setState(288);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(287); match(40);
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
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitStringList(this);
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
			setState(291);
			_la = _input.LA(1);
			if (_la==14) {
				{
				setState(290); match(14);
				}
			}

			setState(293); string();
			setState(298);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(294); match(15);
					setState(295); string();
					}
					} 
				}
				setState(300);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
			}
			setState(302);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(301); match(40);
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
			return getToken(ExpressionFilterParser.INT, i);
		}
		public TerminalNode RANGE() { return getToken(ExpressionFilterParser.RANGE, 0); }
		public List<TerminalNode> INT() { return getTokens(ExpressionFilterParser.INT); }
		public IntRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitIntRange(this);
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
			setState(305);
			_la = _input.LA(1);
			if (_la==14) {
				{
				setState(304); match(14);
				}
			}

			setState(307); match(INT);
			setState(308); match(RANGE);
			setState(309); match(INT);
			setState(311);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(310); match(40);
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
		public TerminalNode RANGE() { return getToken(ExpressionFilterParser.RANGE, 0); }
		public NumberContext number(int i) {
			return getRuleContext(NumberContext.class,i);
		}
		public NumberRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitNumberRange(this);
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
			setState(314);
			_la = _input.LA(1);
			if (_la==14) {
				{
				setState(313); match(14);
				}
			}

			setState(316); number();
			setState(317); match(RANGE);
			setState(318); number();
			setState(320);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(319); match(40);
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
		public TerminalNode INT() { return getToken(ExpressionFilterParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(ExpressionFilterParser.FLOAT, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitNumber(this);
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
			setState(322);
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
		public TerminalNode STRING() { return getToken(ExpressionFilterParser.STRING, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_string);
		try {
			setState(326);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
				enterOuterAlt(_localctx, 1);
				{
				setState(324); number();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(325); match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class BboxContext extends ParserRuleContext {
		public List<NumberContext> number() {
			return getRuleContexts(NumberContext.class);
		}
		public NumberContext number(int i) {
			return getRuleContext(NumberContext.class,i);
		}
		public TerminalNode BBOX() { return getToken(ExpressionFilterParser.BBOX, 0); }
		public BboxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bbox; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitBbox(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BboxContext bbox() throws RecognitionException {
		BboxContext _localctx = new BboxContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_bbox);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328); match(BBOX);
			setState(330);
			_la = _input.LA(1);
			if (_la==14) {
				{
				setState(329); match(14);
				}
			}

			setState(332); number();
			setState(333); match(15);
			setState(334); number();
			setState(335); match(15);
			setState(336); number();
			setState(337); match(15);
			setState(338); number();
			setState(340);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(339); match(40);
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

	public static class CircleContext extends ParserRuleContext {
		public List<NumberContext> number() {
			return getRuleContexts(NumberContext.class);
		}
		public NumberContext number(int i) {
			return getRuleContext(NumberContext.class,i);
		}
		public TerminalNode CIRCLE() { return getToken(ExpressionFilterParser.CIRCLE, 0); }
		public CircleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_circle; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitCircle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CircleContext circle() throws RecognitionException {
		CircleContext _localctx = new CircleContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_circle);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342); match(CIRCLE);
			setState(344);
			_la = _input.LA(1);
			if (_la==14) {
				{
				setState(343); match(14);
				}
			}

			setState(346); number();
			setState(347); match(15);
			setState(348); number();
			setState(349); match(15);
			setState(350); number();
			setState(352);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				setState(351); match(40);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\65\u0165\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3$\n\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3/\n\3\3\3\3\3\5\3\63\n\3\3\3\3\3"+
		"\3\3\5\38\n\3\3\3\3\3\3\3\3\3\3\3\5\3?\n\3\3\3\3\3\3\3\3\3\3\3\5\3F\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3Q\n\3\3\3\3\3\5\3U\n\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\5\3^\n\3\3\3\3\3\5\3b\n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\5\3k\n\3\3\3\3\3\5\3o\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3x\n\3"+
		"\3\3\3\3\3\3\5\3}\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0086\n\3\3\3\3"+
		"\3\3\3\5\3\u008b\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0094\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u009f\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3\u00aa\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00b5\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00c0\n\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u00cb\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00d3\n\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00dc\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3\u00e7\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00ef\n\3\3\3\3\3\3"+
		"\3\5\3\u00f4\n\3\3\3\3\3\3\3\5\3\u00f9\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\5\3\u0103\n\3\3\3\3\3\3\3\6\3\u0108\n\3\r\3\16\3\u0109\7\3\u010c"+
		"\n\3\f\3\16\3\u010f\13\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\5\7\u0118\n\7\3\7"+
		"\3\7\3\7\7\7\u011d\n\7\f\7\16\7\u0120\13\7\3\7\5\7\u0123\n\7\3\b\5\b\u0126"+
		"\n\b\3\b\3\b\3\b\7\b\u012b\n\b\f\b\16\b\u012e\13\b\3\b\5\b\u0131\n\b\3"+
		"\t\5\t\u0134\n\t\3\t\3\t\3\t\3\t\5\t\u013a\n\t\3\n\5\n\u013d\n\n\3\n\3"+
		"\n\3\n\3\n\5\n\u0143\n\n\3\13\3\13\3\f\3\f\5\f\u0149\n\f\3\r\3\r\5\r\u014d"+
		"\n\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u0157\n\r\3\16\3\16\5\16\u015b"+
		"\n\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u0163\n\16\3\16\2\3\4\17\2\4"+
		"\6\b\n\f\16\20\22\24\26\30\32\2\b\b\2\6\6\t\t\f\f\16\16\30\30&&\3\2,-"+
		"\6\2\7\b\13\13\33\34\37\37\6\2\b\b  %%\'\'\5\2\7\7\23\24\32\32\3\2\62"+
		"\63\u01ab\2\34\3\2\2\2\4\u0102\3\2\2\2\6\u0110\3\2\2\2\b\u0112\3\2\2\2"+
		"\n\u0114\3\2\2\2\f\u0117\3\2\2\2\16\u0125\3\2\2\2\20\u0133\3\2\2\2\22"+
		"\u013c\3\2\2\2\24\u0144\3\2\2\2\26\u0148\3\2\2\2\30\u014a\3\2\2\2\32\u0158"+
		"\3\2\2\2\34\35\5\4\3\2\35\36\7\2\2\3\36\3\3\2\2\2\37 \b\3\1\2 #\7\35\2"+
		"\2!$\5\b\5\2\"$\5\n\6\2#!\3\2\2\2#\"\3\2\2\2$%\3\2\2\2%&\5\16\b\2&\u0103"+
		"\3\2\2\2\'(\7+\2\2()\5\6\4\2)*\7\62\2\2*\u0103\3\2\2\2+.\7+\2\2,/\5\b"+
		"\5\2-/\5\n\6\2.,\3\2\2\2.-\3\2\2\2/\62\3\2\2\2\60\63\5\20\t\2\61\63\5"+
		"\f\7\2\62\60\3\2\2\2\62\61\3\2\2\2\63\u0103\3\2\2\2\64\67\7\17\2\2\65"+
		"8\5\b\5\2\668\5\n\6\2\67\65\3\2\2\2\67\66\3\2\2\289\3\2\2\29:\5\16\b\2"+
		":\u0103\3\2\2\2;>\7\3\2\2<?\5\b\5\2=?\5\n\6\2><\3\2\2\2>=\3\2\2\2?@\3"+
		"\2\2\2@A\5\16\b\2A\u0103\3\2\2\2BE\7\36\2\2CF\5\b\5\2DF\5\n\6\2EC\3\2"+
		"\2\2ED\3\2\2\2FG\3\2\2\2GH\5\16\b\2H\u0103\3\2\2\2IJ\7)\2\2JK\5\6\4\2"+
		"KL\7\62\2\2L\u0103\3\2\2\2MP\7)\2\2NQ\5\b\5\2OQ\5\n\6\2PN\3\2\2\2PO\3"+
		"\2\2\2QT\3\2\2\2RU\5\20\t\2SU\5\f\7\2TR\3\2\2\2TS\3\2\2\2U\u0103\3\2\2"+
		"\2VW\7\n\2\2WX\5\6\4\2XY\7\62\2\2Y\u0103\3\2\2\2Z]\7\n\2\2[^\5\b\5\2\\"+
		"^\5\n\6\2][\3\2\2\2]\\\3\2\2\2^a\3\2\2\2_b\5\20\t\2`b\5\f\7\2a_\3\2\2"+
		"\2a`\3\2\2\2b\u0103\3\2\2\2cd\7(\2\2de\5\6\4\2ef\7\62\2\2f\u0103\3\2\2"+
		"\2gj\7(\2\2hk\5\b\5\2ik\5\n\6\2jh\3\2\2\2ji\3\2\2\2kn\3\2\2\2lo\5\20\t"+
		"\2mo\5\f\7\2nl\3\2\2\2nm\3\2\2\2o\u0103\3\2\2\2pq\7$\2\2qr\5\6\4\2rs\5"+
		"\26\f\2s\u0103\3\2\2\2tw\7$\2\2ux\5\b\5\2vx\5\n\6\2wu\3\2\2\2wv\3\2\2"+
		"\2x|\3\2\2\2y}\5\20\t\2z}\5\f\7\2{}\5\16\b\2|y\3\2\2\2|z\3\2\2\2|{\3\2"+
		"\2\2}\u0103\3\2\2\2~\177\7\31\2\2\177\u0080\5\6\4\2\u0080\u0081\5\26\f"+
		"\2\u0081\u0103\3\2\2\2\u0082\u0085\7\31\2\2\u0083\u0086\5\b\5\2\u0084"+
		"\u0086\5\n\6\2\u0085\u0083\3\2\2\2\u0085\u0084\3\2\2\2\u0086\u008a\3\2"+
		"\2\2\u0087\u008b\5\20\t\2\u0088\u008b\5\f\7\2\u0089\u008b\5\16\b\2\u008a"+
		"\u0087\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u0089\3\2\2\2\u008b\u0103\3\2"+
		"\2\2\u008c\u008d\7\5\2\2\u008d\u008e\5\6\4\2\u008e\u008f\5\26\f\2\u008f"+
		"\u0103\3\2\2\2\u0090\u0093\7\5\2\2\u0091\u0094\5\b\5\2\u0092\u0094\5\n"+
		"\6\2\u0093\u0091\3\2\2\2\u0093\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095"+
		"\u0096\5\16\b\2\u0096\u0103\3\2\2\2\u0097\u0098\7!\2\2\u0098\u0099\5\6"+
		"\4\2\u0099\u009a\5\26\f\2\u009a\u0103\3\2\2\2\u009b\u009e\7!\2\2\u009c"+
		"\u009f\5\b\5\2\u009d\u009f\5\n\6\2\u009e\u009c\3\2\2\2\u009e\u009d\3\2"+
		"\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1\5\16\b\2\u00a1\u0103\3\2\2\2\u00a2"+
		"\u00a3\7\27\2\2\u00a3\u00a4\5\6\4\2\u00a4\u00a5\5\24\13\2\u00a5\u0103"+
		"\3\2\2\2\u00a6\u00a9\7\27\2\2\u00a7\u00aa\5\b\5\2\u00a8\u00aa\5\n\6\2"+
		"\u00a9\u00a7\3\2\2\2\u00a9\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ac"+
		"\5\22\n\2\u00ac\u0103\3\2\2\2\u00ad\u00ae\7\22\2\2\u00ae\u00af\5\6\4\2"+
		"\u00af\u00b0\5\24\13\2\u00b0\u0103\3\2\2\2\u00b1\u00b4\7\22\2\2\u00b2"+
		"\u00b5\5\b\5\2\u00b3\u00b5\5\n\6\2\u00b4\u00b2\3\2\2\2\u00b4\u00b3\3\2"+
		"\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\5\22\n\2\u00b7\u0103\3\2\2\2\u00b8"+
		"\u00b9\7\4\2\2\u00b9\u00ba\5\6\4\2\u00ba\u00bb\5\24\13\2\u00bb\u0103\3"+
		"\2\2\2\u00bc\u00bf\7\4\2\2\u00bd\u00c0\5\b\5\2\u00be\u00c0\5\n\6\2\u00bf"+
		"\u00bd\3\2\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\5\22"+
		"\n\2\u00c2\u0103\3\2\2\2\u00c3\u00c4\7#\2\2\u00c4\u00c5\5\6\4\2\u00c5"+
		"\u00c6\5\24\13\2\u00c6\u0103\3\2\2\2\u00c7\u00ca\7#\2\2\u00c8\u00cb\5"+
		"\b\5\2\u00c9\u00cb\5\n\6\2\u00ca\u00c8\3\2\2\2\u00ca\u00c9\3\2\2\2\u00cb"+
		"\u00cc\3\2\2\2\u00cc\u00cd\5\22\n\2\u00cd\u0103\3\2\2\2\u00ce\u00cf\7"+
		"\"\2\2\u00cf\u00d2\7\61\2\2\u00d0\u00d3\5\32\16\2\u00d1\u00d3\5\30\r\2"+
		"\u00d2\u00d0\3\2\2\2\u00d2\u00d1\3\2\2\2\u00d3\u0103\3\2\2\2\u00d4\u00d5"+
		"\7\r\2\2\u00d5\u00d6\5\6\4\2\u00d6\u00d7\7\62\2\2\u00d7\u0103\3\2\2\2"+
		"\u00d8\u00db\7\r\2\2\u00d9\u00dc\5\b\5\2\u00da\u00dc\5\n\6\2\u00db\u00d9"+
		"\3\2\2\2\u00db\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de\5\20\t\2"+
		"\u00de\u0103\3\2\2\2\u00df\u00e0\7\26\2\2\u00e0\u00e1\5\6\4\2\u00e1\u00e2"+
		"\5\24\13\2\u00e2\u0103\3\2\2\2\u00e3\u00e6\7\26\2\2\u00e4\u00e7\5\b\5"+
		"\2\u00e5\u00e7\5\n\6\2\u00e6\u00e4\3\2\2\2\u00e6\u00e5\3\2\2\2\u00e7\u00e8"+
		"\3\2\2\2\u00e8\u00e9\5\22\n\2\u00e9\u0103\3\2\2\2\u00ea\u00eb\t\2\2\2"+
		"\u00eb\u00ee\5\6\4\2\u00ec\u00ef\7\62\2\2\u00ed\u00ef\5\26\f\2\u00ee\u00ec"+
		"\3\2\2\2\u00ee\u00ed\3\2\2\2\u00ef\u0103\3\2\2\2\u00f0\u00f3\t\2\2\2\u00f1"+
		"\u00f4\5\b\5\2\u00f2\u00f4\5\n\6\2\u00f3\u00f1\3\2\2\2\u00f3\u00f2\3\2"+
		"\2\2\u00f4\u00f8\3\2\2\2\u00f5\u00f9\5\20\t\2\u00f6\u00f9\5\f\7\2\u00f7"+
		"\u00f9\5\16\b\2\u00f8\u00f5\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f8\u00f7\3"+
		"\2\2\2\u00f9\u0103\3\2\2\2\u00fa\u00fb\7\25\2\2\u00fb\u00fc\5\b\5\2\u00fc"+
		"\u00fd\5\16\b\2\u00fd\u0103\3\2\2\2\u00fe\u00ff\7\20\2\2\u00ff\u0100\5"+
		"\4\3\2\u0100\u0101\7*\2\2\u0101\u0103\3\2\2\2\u0102\37\3\2\2\2\u0102\'"+
		"\3\2\2\2\u0102+\3\2\2\2\u0102\64\3\2\2\2\u0102;\3\2\2\2\u0102B\3\2\2\2"+
		"\u0102I\3\2\2\2\u0102M\3\2\2\2\u0102V\3\2\2\2\u0102Z\3\2\2\2\u0102c\3"+
		"\2\2\2\u0102g\3\2\2\2\u0102p\3\2\2\2\u0102t\3\2\2\2\u0102~\3\2\2\2\u0102"+
		"\u0082\3\2\2\2\u0102\u008c\3\2\2\2\u0102\u0090\3\2\2\2\u0102\u0097\3\2"+
		"\2\2\u0102\u009b\3\2\2\2\u0102\u00a2\3\2\2\2\u0102\u00a6\3\2\2\2\u0102"+
		"\u00ad\3\2\2\2\u0102\u00b1\3\2\2\2\u0102\u00b8\3\2\2\2\u0102\u00bc\3\2"+
		"\2\2\u0102\u00c3\3\2\2\2\u0102\u00c7\3\2\2\2\u0102\u00ce\3\2\2\2\u0102"+
		"\u00d4\3\2\2\2\u0102\u00d8\3\2\2\2\u0102\u00df\3\2\2\2\u0102\u00e3\3\2"+
		"\2\2\u0102\u00ea\3\2\2\2\u0102\u00f0\3\2\2\2\u0102\u00fa\3\2\2\2\u0102"+
		"\u00fe\3\2\2\2\u0103\u010d\3\2\2\2\u0104\u0107\f\4\2\2\u0105\u0106\t\3"+
		"\2\2\u0106\u0108\5\4\3\2\u0107\u0105\3\2\2\2\u0108\u0109\3\2\2\2\u0109"+
		"\u0107\3\2\2\2\u0109\u010a\3\2\2\2\u010a\u010c\3\2\2\2\u010b\u0104\3\2"+
		"\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e"+
		"\5\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0111\t\4\2\2\u0111\7\3\2\2\2\u0112"+
		"\u0113\t\5\2\2\u0113\t\3\2\2\2\u0114\u0115\t\6\2\2\u0115\13\3\2\2\2\u0116"+
		"\u0118\7\20\2\2\u0117\u0116\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u0119\3"+
		"\2\2\2\u0119\u011e\7\62\2\2\u011a\u011b\7\21\2\2\u011b\u011d\7\62\2\2"+
		"\u011c\u011a\3\2\2\2\u011d\u0120\3\2\2\2\u011e\u011c\3\2\2\2\u011e\u011f"+
		"\3\2\2\2\u011f\u0122\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0123\7*\2\2\u0122"+
		"\u0121\3\2\2\2\u0122\u0123\3\2\2\2\u0123\r\3\2\2\2\u0124\u0126\7\20\2"+
		"\2\u0125\u0124\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0127\3\2\2\2\u0127\u012c"+
		"\5\26\f\2\u0128\u0129\7\21\2\2\u0129\u012b\5\26\f\2\u012a\u0128\3\2\2"+
		"\2\u012b\u012e\3\2\2\2\u012c\u012a\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u0130"+
		"\3\2\2\2\u012e\u012c\3\2\2\2\u012f\u0131\7*\2\2\u0130\u012f\3\2\2\2\u0130"+
		"\u0131\3\2\2\2\u0131\17\3\2\2\2\u0132\u0134\7\20\2\2\u0133\u0132\3\2\2"+
		"\2\u0133\u0134\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0136\7\62\2\2\u0136"+
		"\u0137\7.\2\2\u0137\u0139\7\62\2\2\u0138\u013a\7*\2\2\u0139\u0138\3\2"+
		"\2\2\u0139\u013a\3\2\2\2\u013a\21\3\2\2\2\u013b\u013d\7\20\2\2\u013c\u013b"+
		"\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u013f\5\24\13\2"+
		"\u013f\u0140\7.\2\2\u0140\u0142\5\24\13\2\u0141\u0143\7*\2\2\u0142\u0141"+
		"\3\2\2\2\u0142\u0143\3\2\2\2\u0143\23\3\2\2\2\u0144\u0145\t\7\2\2\u0145"+
		"\25\3\2\2\2\u0146\u0149\5\24\13\2\u0147\u0149\7\64\2\2\u0148\u0146\3\2"+
		"\2\2\u0148\u0147\3\2\2\2\u0149\27\3\2\2\2\u014a\u014c\7/\2\2\u014b\u014d"+
		"\7\20\2\2\u014c\u014b\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u014e\3\2\2\2"+
		"\u014e\u014f\5\24\13\2\u014f\u0150\7\21\2\2\u0150\u0151\5\24\13\2\u0151"+
		"\u0152\7\21\2\2\u0152\u0153\5\24\13\2\u0153\u0154\7\21\2\2\u0154\u0156"+
		"\5\24\13\2\u0155\u0157\7*\2\2\u0156\u0155\3\2\2\2\u0156\u0157\3\2\2\2"+
		"\u0157\31\3\2\2\2\u0158\u015a\7\60\2\2\u0159\u015b\7\20\2\2\u015a\u0159"+
		"\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015d\5\24\13\2"+
		"\u015d\u015e\7\21\2\2\u015e\u015f\5\24\13\2\u015f\u0160\7\21\2\2\u0160"+
		"\u0162\5\24\13\2\u0161\u0163\7*\2\2\u0162\u0161\3\2\2\2\u0162\u0163\3"+
		"\2\2\2\u0163\33\3\2\2\2\60#.\62\67>EPT]ajnw|\u0085\u008a\u0093\u009e\u00a9"+
		"\u00b4\u00bf\u00ca\u00d2\u00db\u00e6\u00ee\u00f3\u00f8\u0102\u0109\u010d"+
		"\u0117\u011e\u0122\u0125\u012c\u0130\u0133\u0139\u013c\u0142\u0148\u014c"+
		"\u0156\u015a\u0162";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}