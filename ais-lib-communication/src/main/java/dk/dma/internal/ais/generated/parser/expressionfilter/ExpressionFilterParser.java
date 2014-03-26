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
	public static class ProgContext extends ParserRuleContext {
		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ExpressionFilterParser.EOF, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ExpressionFilterVisitor ) return ((ExpressionFilterVisitor<? extends T>)visitor).visitProg(this);
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
			setState(230);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				_localctx = new SourceIdInContext(_localctx);
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
				_localctx = new SourceBasestationInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(37); match(34);
				setState(40);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(38); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(39); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(44);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(42); intRange();
					}
					break;

				case 2:
					{
					setState(43); intList();
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
				setState(46); match(9);
				setState(49);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(47); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(48); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(51); stringList();
				}
				break;

			case 5:
				{
				_localctx = new SourceTypeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(53); match(1);
				setState(56);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(54); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(55); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(58); stringList();
				}
				break;

			case 6:
				{
				_localctx = new SourceRegionInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(60); match(23);
				setState(63);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(61); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(62); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(65); stringList();
				}
				break;

			case 7:
				{
				_localctx = new MessageIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(67); match(32);
				setState(68); compareTo();
				setState(69); match(INT);
				}
				break;

			case 8:
				{
				_localctx = new MessageIdInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(71); match(32);
				setState(74);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(72); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(73); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(78);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(76); intRange();
					}
					break;

				case 2:
					{
					setState(77); intList();
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
				setState(80); match(6);
				setState(81); compareTo();
				setState(82); match(INT);
				}
				break;

			case 10:
				{
				_localctx = new MessageMmsiInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84); match(6);
				setState(87);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(85); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(86); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(91);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(89); intRange();
					}
					break;

				case 2:
					{
					setState(90); intList();
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
				setState(93); match(31);
				setState(94); compareTo();
				setState(95); match(INT);
				}
				break;

			case 12:
				{
				_localctx = new MessageImoInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(97); match(31);
				setState(100);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(98); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(99); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(104);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(102); intRange();
					}
					break;

				case 2:
					{
					setState(103); intList();
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
				setState(106); match(28);
				setState(107); compareTo();
				setState(108); string();
				}
				break;

			case 14:
				{
				_localctx = new MessageShiptypeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(110); match(28);
				setState(113);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(111); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(112); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(118);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(115); intRange();
					}
					break;

				case 2:
					{
					setState(116); intList();
					}
					break;

				case 3:
					{
					setState(117); stringList();
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
				setState(120); match(18);
				setState(121); compareTo();
				setState(122); string();
				}
				break;

			case 16:
				{
				_localctx = new MessageNavigationalStatusInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(124); match(18);
				setState(127);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(125); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(126); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(132);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(129); intRange();
					}
					break;

				case 2:
					{
					setState(130); intList();
					}
					break;

				case 3:
					{
					setState(131); stringList();
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
				setState(134); match(3);
				setState(135); compareTo();
				setState(136); string();
				}
				break;

			case 18:
				{
				_localctx = new MessageNameInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(138); match(3);
				setState(141);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(139); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(140); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(143); stringList();
				}
				break;

			case 19:
				{
				_localctx = new MessageCallsignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(145); match(26);
				setState(146); compareTo();
				setState(147); string();
				}
				break;

			case 20:
				{
				_localctx = new MessageCallsignInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(149); match(26);
				setState(152);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(150); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(151); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(154); stringList();
				}
				break;

			case 21:
				{
				_localctx = new MessageSpeedOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(156); match(17);
				setState(157); compareTo();
				setState(158); number();
				}
				break;

			case 22:
				{
				_localctx = new MessageSpeedOverGroundInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(160); match(17);
				setState(163);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(161); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(162); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(165); numberRange();
				}
				break;

			case 23:
				{
				_localctx = new MessageCourseOverGroundContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(167); match(12);
				setState(168); compareTo();
				setState(169); number();
				}
				break;

			case 24:
				{
				_localctx = new MessageCourseOverGroundInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(171); match(12);
				setState(174);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(172); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(173); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(176); numberRange();
				}
				break;

			case 25:
				{
				_localctx = new MessageLatitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(178); match(2);
				setState(179); compareTo();
				setState(180); number();
				}
				break;

			case 26:
				{
				_localctx = new MessageLatitudeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(182); match(2);
				setState(185);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(183); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(184); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(187); numberRange();
				}
				break;

			case 27:
				{
				_localctx = new MessageLongitudeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(189); match(27);
				setState(190); compareTo();
				setState(191); number();
				}
				break;

			case 28:
				{
				_localctx = new MessageLongitudeInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(193); match(27);
				setState(196);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(194); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(195); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(198); numberRange();
				}
				break;

			case 29:
				{
				_localctx = new MessageTrueHeadingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(200); match(8);
				setState(201); compareTo();
				setState(202); match(INT);
				}
				break;

			case 30:
				{
				_localctx = new MessageTrueHeadingInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(204); match(8);
				setState(207);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(205); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(206); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(209); intRange();
				}
				break;

			case 31:
				{
				_localctx = new MessageDraughtContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(211); match(16);
				setState(212); compareTo();
				setState(213); number();
				}
				break;

			case 32:
				{
				_localctx = new MessageDraughtInContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(215); match(16);
				setState(218);
				switch (_input.LA(1)) {
				case 5:
				case 25:
				case 29:
				case 30:
					{
					setState(216); in();
					}
					break;
				case 4:
				case 13:
				case 14:
				case 19:
					{
					setState(217); notin();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(220); numberRange();
				}
				break;

			case 33:
				{
				_localctx = new AisMessagetypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(222); match(15);
				setState(223); in();
				setState(224); stringList();
				}
				break;

			case 34:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(226); match(10);
				setState(227); filterExpression(0);
				setState(228); match(33);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(241);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrAndContext(new FilterExpressionContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_filterExpression);
					setState(232);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(235); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
					do {
						switch (_alt) {
						case 1:
							{
							{
							setState(233);
							((OrAndContext)_localctx).op = _input.LT(1);
							_la = _input.LA(1);
							if ( !(_la==AND || _la==OR) ) {
								((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
							}
							consume();
							setState(234); filterExpression(0);
							}
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(237); 
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
					} while ( _alt!=2 && _alt!=-1 );
					}
					} 
				}
				setState(243);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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
			setState(244);
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
			setState(246);
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
			setState(248);
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
			setState(251);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(250); match(10);
				}
			}

			setState(253); match(INT);
			setState(258);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(254); match(11);
					setState(255); match(INT);
					}
					} 
				}
				setState(260);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			setState(262);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(261); match(33);
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
			setState(265);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(264); match(10);
				}
			}

			setState(267); string();
			setState(272);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(268); match(11);
					setState(269); string();
					}
					} 
				}
				setState(274);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			}
			setState(276);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(275); match(33);
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
			setState(279);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(278); match(10);
				}
			}

			setState(281); match(INT);
			setState(282); match(RANGE);
			setState(283); match(INT);
			setState(285);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				{
				setState(284); match(33);
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
			setState(288);
			_la = _input.LA(1);
			if (_la==10) {
				{
				setState(287); match(10);
				}
			}

			setState(290); number();
			setState(291); match(RANGE);
			setState(292); number();
			setState(294);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(293); match(33);
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
			setState(296);
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
		public TerminalNode WORD() { return getToken(ExpressionFilterParser.WORD, 0); }
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
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			switch (_input.LA(1)) {
			case INT:
			case FLOAT:
				{
				setState(298); number();
				}
				break;
			case WORD:
				{
				setState(299); match(WORD);
				}
				break;
			case STRING:
				{
				setState(300); match(STRING);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3,\u0132\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\3\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3 \n\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3+\n\3\3\3\3\3\5\3/\n\3\3\3\3\3\3\3\5\3\64\n\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3;\n\3\3\3\3\3\3\3\3\3\3\3\5\3B\n\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3M\n\3\3\3\3\3\5\3Q\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3Z\n\3\3\3\3\3\5\3^\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3g\n\3\3\3"+
		"\3\3\5\3k\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3t\n\3\3\3\3\3\3\3\5\3y\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0082\n\3\3\3\3\3\3\3\5\3\u0087\n\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0090\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3\u009b\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00a6\n"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00b1\n\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3\u00bc\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5"+
		"\3\u00c7\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00d2\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00dd\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3\u00e9\n\3\3\3\3\3\3\3\6\3\u00ee\n\3\r\3\16\3\u00ef\7"+
		"\3\u00f2\n\3\f\3\16\3\u00f5\13\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\5\7\u00fe"+
		"\n\7\3\7\3\7\3\7\7\7\u0103\n\7\f\7\16\7\u0106\13\7\3\7\5\7\u0109\n\7\3"+
		"\b\5\b\u010c\n\b\3\b\3\b\3\b\7\b\u0111\n\b\f\b\16\b\u0114\13\b\3\b\5\b"+
		"\u0117\n\b\3\t\5\t\u011a\n\t\3\t\3\t\3\t\3\t\5\t\u0120\n\t\3\n\5\n\u0123"+
		"\n\n\3\n\3\n\3\n\3\n\5\n\u0129\n\n\3\13\3\13\3\f\3\f\3\f\5\f\u0130\n\f"+
		"\3\f\2\3\4\r\2\4\6\b\n\f\16\20\22\24\26\2\7\3\2%&\6\2\6\7\t\t\26\27\32"+
		"\32\5\2\7\7\33\33\37 \5\2\6\6\17\20\25\25\3\2()\u016f\2\30\3\2\2\2\4\u00e8"+
		"\3\2\2\2\6\u00f6\3\2\2\2\b\u00f8\3\2\2\2\n\u00fa\3\2\2\2\f\u00fd\3\2\2"+
		"\2\16\u010b\3\2\2\2\20\u0119\3\2\2\2\22\u0122\3\2\2\2\24\u012a\3\2\2\2"+
		"\26\u012f\3\2\2\2\30\31\5\4\3\2\31\32\7\2\2\3\32\3\3\2\2\2\33\34\b\3\1"+
		"\2\34\37\7\30\2\2\35 \5\b\5\2\36 \5\n\6\2\37\35\3\2\2\2\37\36\3\2\2\2"+
		" !\3\2\2\2!\"\5\16\b\2\"\u00e9\3\2\2\2#$\7$\2\2$%\5\6\4\2%&\7(\2\2&\u00e9"+
		"\3\2\2\2\'*\7$\2\2(+\5\b\5\2)+\5\n\6\2*(\3\2\2\2*)\3\2\2\2+.\3\2\2\2,"+
		"/\5\20\t\2-/\5\f\7\2.,\3\2\2\2.-\3\2\2\2/\u00e9\3\2\2\2\60\63\7\13\2\2"+
		"\61\64\5\b\5\2\62\64\5\n\6\2\63\61\3\2\2\2\63\62\3\2\2\2\64\65\3\2\2\2"+
		"\65\66\5\16\b\2\66\u00e9\3\2\2\2\67:\7\3\2\28;\5\b\5\29;\5\n\6\2:8\3\2"+
		"\2\2:9\3\2\2\2;<\3\2\2\2<=\5\16\b\2=\u00e9\3\2\2\2>A\7\31\2\2?B\5\b\5"+
		"\2@B\5\n\6\2A?\3\2\2\2A@\3\2\2\2BC\3\2\2\2CD\5\16\b\2D\u00e9\3\2\2\2E"+
		"F\7\"\2\2FG\5\6\4\2GH\7(\2\2H\u00e9\3\2\2\2IL\7\"\2\2JM\5\b\5\2KM\5\n"+
		"\6\2LJ\3\2\2\2LK\3\2\2\2MP\3\2\2\2NQ\5\20\t\2OQ\5\f\7\2PN\3\2\2\2PO\3"+
		"\2\2\2Q\u00e9\3\2\2\2RS\7\b\2\2ST\5\6\4\2TU\7(\2\2U\u00e9\3\2\2\2VY\7"+
		"\b\2\2WZ\5\b\5\2XZ\5\n\6\2YW\3\2\2\2YX\3\2\2\2Z]\3\2\2\2[^\5\20\t\2\\"+
		"^\5\f\7\2][\3\2\2\2]\\\3\2\2\2^\u00e9\3\2\2\2_`\7!\2\2`a\5\6\4\2ab\7("+
		"\2\2b\u00e9\3\2\2\2cf\7!\2\2dg\5\b\5\2eg\5\n\6\2fd\3\2\2\2fe\3\2\2\2g"+
		"j\3\2\2\2hk\5\20\t\2ik\5\f\7\2jh\3\2\2\2ji\3\2\2\2k\u00e9\3\2\2\2lm\7"+
		"\36\2\2mn\5\6\4\2no\5\26\f\2o\u00e9\3\2\2\2ps\7\36\2\2qt\5\b\5\2rt\5\n"+
		"\6\2sq\3\2\2\2sr\3\2\2\2tx\3\2\2\2uy\5\20\t\2vy\5\f\7\2wy\5\16\b\2xu\3"+
		"\2\2\2xv\3\2\2\2xw\3\2\2\2y\u00e9\3\2\2\2z{\7\24\2\2{|\5\6\4\2|}\5\26"+
		"\f\2}\u00e9\3\2\2\2~\u0081\7\24\2\2\177\u0082\5\b\5\2\u0080\u0082\5\n"+
		"\6\2\u0081\177\3\2\2\2\u0081\u0080\3\2\2\2\u0082\u0086\3\2\2\2\u0083\u0087"+
		"\5\20\t\2\u0084\u0087\5\f\7\2\u0085\u0087\5\16\b\2\u0086\u0083\3\2\2\2"+
		"\u0086\u0084\3\2\2\2\u0086\u0085\3\2\2\2\u0087\u00e9\3\2\2\2\u0088\u0089"+
		"\7\5\2\2\u0089\u008a\5\6\4\2\u008a\u008b\5\26\f\2\u008b\u00e9\3\2\2\2"+
		"\u008c\u008f\7\5\2\2\u008d\u0090\5\b\5\2\u008e\u0090\5\n\6\2\u008f\u008d"+
		"\3\2\2\2\u008f\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0092\5\16\b\2"+
		"\u0092\u00e9\3\2\2\2\u0093\u0094\7\34\2\2\u0094\u0095\5\6\4\2\u0095\u0096"+
		"\5\26\f\2\u0096\u00e9\3\2\2\2\u0097\u009a\7\34\2\2\u0098\u009b\5\b\5\2"+
		"\u0099\u009b\5\n\6\2\u009a\u0098\3\2\2\2\u009a\u0099\3\2\2\2\u009b\u009c"+
		"\3\2\2\2\u009c\u009d\5\16\b\2\u009d\u00e9\3\2\2\2\u009e\u009f\7\23\2\2"+
		"\u009f\u00a0\5\6\4\2\u00a0\u00a1\5\24\13\2\u00a1\u00e9\3\2\2\2\u00a2\u00a5"+
		"\7\23\2\2\u00a3\u00a6\5\b\5\2\u00a4\u00a6\5\n\6\2\u00a5\u00a3\3\2\2\2"+
		"\u00a5\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8\5\22\n\2\u00a8\u00e9"+
		"\3\2\2\2\u00a9\u00aa\7\16\2\2\u00aa\u00ab\5\6\4\2\u00ab\u00ac\5\24\13"+
		"\2\u00ac\u00e9\3\2\2\2\u00ad\u00b0\7\16\2\2\u00ae\u00b1\5\b\5\2\u00af"+
		"\u00b1\5\n\6\2\u00b0\u00ae\3\2\2\2\u00b0\u00af\3\2\2\2\u00b1\u00b2\3\2"+
		"\2\2\u00b2\u00b3\5\22\n\2\u00b3\u00e9\3\2\2\2\u00b4\u00b5\7\4\2\2\u00b5"+
		"\u00b6\5\6\4\2\u00b6\u00b7\5\24\13\2\u00b7\u00e9\3\2\2\2\u00b8\u00bb\7"+
		"\4\2\2\u00b9\u00bc\5\b\5\2\u00ba\u00bc\5\n\6\2\u00bb\u00b9\3\2\2\2\u00bb"+
		"\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00be\5\22\n\2\u00be\u00e9\3"+
		"\2\2\2\u00bf\u00c0\7\35\2\2\u00c0\u00c1\5\6\4\2\u00c1\u00c2\5\24\13\2"+
		"\u00c2\u00e9\3\2\2\2\u00c3\u00c6\7\35\2\2\u00c4\u00c7\5\b\5\2\u00c5\u00c7"+
		"\5\n\6\2\u00c6\u00c4\3\2\2\2\u00c6\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8"+
		"\u00c9\5\22\n\2\u00c9\u00e9\3\2\2\2\u00ca\u00cb\7\n\2\2\u00cb\u00cc\5"+
		"\6\4\2\u00cc\u00cd\7(\2\2\u00cd\u00e9\3\2\2\2\u00ce\u00d1\7\n\2\2\u00cf"+
		"\u00d2\5\b\5\2\u00d0\u00d2\5\n\6\2\u00d1\u00cf\3\2\2\2\u00d1\u00d0\3\2"+
		"\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d4\5\20\t\2\u00d4\u00e9\3\2\2\2\u00d5"+
		"\u00d6\7\22\2\2\u00d6\u00d7\5\6\4\2\u00d7\u00d8\5\24\13\2\u00d8\u00e9"+
		"\3\2\2\2\u00d9\u00dc\7\22\2\2\u00da\u00dd\5\b\5\2\u00db\u00dd\5\n\6\2"+
		"\u00dc\u00da\3\2\2\2\u00dc\u00db\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00df"+
		"\5\22\n\2\u00df\u00e9\3\2\2\2\u00e0\u00e1\7\21\2\2\u00e1\u00e2\5\b\5\2"+
		"\u00e2\u00e3\5\16\b\2\u00e3\u00e9\3\2\2\2\u00e4\u00e5\7\f\2\2\u00e5\u00e6"+
		"\5\4\3\2\u00e6\u00e7\7#\2\2\u00e7\u00e9\3\2\2\2\u00e8\33\3\2\2\2\u00e8"+
		"#\3\2\2\2\u00e8\'\3\2\2\2\u00e8\60\3\2\2\2\u00e8\67\3\2\2\2\u00e8>\3\2"+
		"\2\2\u00e8E\3\2\2\2\u00e8I\3\2\2\2\u00e8R\3\2\2\2\u00e8V\3\2\2\2\u00e8"+
		"_\3\2\2\2\u00e8c\3\2\2\2\u00e8l\3\2\2\2\u00e8p\3\2\2\2\u00e8z\3\2\2\2"+
		"\u00e8~\3\2\2\2\u00e8\u0088\3\2\2\2\u00e8\u008c\3\2\2\2\u00e8\u0093\3"+
		"\2\2\2\u00e8\u0097\3\2\2\2\u00e8\u009e\3\2\2\2\u00e8\u00a2\3\2\2\2\u00e8"+
		"\u00a9\3\2\2\2\u00e8\u00ad\3\2\2\2\u00e8\u00b4\3\2\2\2\u00e8\u00b8\3\2"+
		"\2\2\u00e8\u00bf\3\2\2\2\u00e8\u00c3\3\2\2\2\u00e8\u00ca\3\2\2\2\u00e8"+
		"\u00ce\3\2\2\2\u00e8\u00d5\3\2\2\2\u00e8\u00d9\3\2\2\2\u00e8\u00e0\3\2"+
		"\2\2\u00e8\u00e4\3\2\2\2\u00e9\u00f3\3\2\2\2\u00ea\u00ed\f\4\2\2\u00eb"+
		"\u00ec\t\2\2\2\u00ec\u00ee\5\4\3\2\u00ed\u00eb\3\2\2\2\u00ee\u00ef\3\2"+
		"\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f2\3\2\2\2\u00f1"+
		"\u00ea\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2"+
		"\2\2\u00f4\5\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f7\t\3\2\2\u00f7\7\3"+
		"\2\2\2\u00f8\u00f9\t\4\2\2\u00f9\t\3\2\2\2\u00fa\u00fb\t\5\2\2\u00fb\13"+
		"\3\2\2\2\u00fc\u00fe\7\f\2\2\u00fd\u00fc\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe"+
		"\u00ff\3\2\2\2\u00ff\u0104\7(\2\2\u0100\u0101\7\r\2\2\u0101\u0103\7(\2"+
		"\2\u0102\u0100\3\2\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2\2\2\u0104\u0105"+
		"\3\2\2\2\u0105\u0108\3\2\2\2\u0106\u0104\3\2\2\2\u0107\u0109\7#\2\2\u0108"+
		"\u0107\3\2\2\2\u0108\u0109\3\2\2\2\u0109\r\3\2\2\2\u010a\u010c\7\f\2\2"+
		"\u010b\u010a\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u0112"+
		"\5\26\f\2\u010e\u010f\7\r\2\2\u010f\u0111\5\26\f\2\u0110\u010e\3\2\2\2"+
		"\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113\u0116"+
		"\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0117\7#\2\2\u0116\u0115\3\2\2\2\u0116"+
		"\u0117\3\2\2\2\u0117\17\3\2\2\2\u0118\u011a\7\f\2\2\u0119\u0118\3\2\2"+
		"\2\u0119\u011a\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011c\7(\2\2\u011c\u011d"+
		"\7\'\2\2\u011d\u011f\7(\2\2\u011e\u0120\7#\2\2\u011f\u011e\3\2\2\2\u011f"+
		"\u0120\3\2\2\2\u0120\21\3\2\2\2\u0121\u0123\7\f\2\2\u0122\u0121\3\2\2"+
		"\2\u0122\u0123\3\2\2\2\u0123\u0124\3\2\2\2\u0124\u0125\5\24\13\2\u0125"+
		"\u0126\7\'\2\2\u0126\u0128\5\24\13\2\u0127\u0129\7#\2\2\u0128\u0127\3"+
		"\2\2\2\u0128\u0129\3\2\2\2\u0129\23\3\2\2\2\u012a\u012b\t\6\2\2\u012b"+
		"\25\3\2\2\2\u012c\u0130\5\24\13\2\u012d\u0130\7*\2\2\u012e\u0130\7+\2"+
		"\2\u012f\u012c\3\2\2\2\u012f\u012d\3\2\2\2\u012f\u012e\3\2\2\2\u0130\27"+
		"\3\2\2\2(\37*.\63:ALPY]fjsx\u0081\u0086\u008f\u009a\u00a5\u00b0\u00bb"+
		"\u00c6\u00d1\u00dc\u00e8\u00ef\u00f3\u00fd\u0104\u0108\u010b\u0112\u0116"+
		"\u0119\u011f\u0122\u0128\u012f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}