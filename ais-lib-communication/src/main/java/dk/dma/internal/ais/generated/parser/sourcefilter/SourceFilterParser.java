/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		T__7=1, T__6=2, T__5=3, T__4=4, T__3=5, T__2=6, T__1=7, T__0=8, EQUALS=9, 
		NEQUALS=10, WS=11, AND=12, OR=13, ID=14;
	public static final String[] tokenNames = {
		"<INVALID>", "'country'", "'id'", "'('", "')'", "'bs'", "'region'", "','", 
		"'type'", "'='", "'!='", "WS", "'&'", "'|'", "ID"
	};
	public static final int
		RULE_prog = 0, RULE_expr = 1, RULE_equalityTest = 2, RULE_idList = 3;
	public static final String[] ruleNames = {
		"prog", "expr", "equalityTest", "idList"
	};

	@Override
	public String getGrammarFileName() { return "SourceFilter.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public SourceFilterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SourceFilterParser.EOF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
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
			setState(8); expr(0);
			setState(9); match(EOF);
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
		public int _p;
		public ExprContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public ExprContext(ParserRuleContext parent, int invokingState, int _p) {
			super(parent, invokingState);
			this._p = _p;
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
			this._p = ctx._p;
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
	public static class SourceRegionContext extends ExprContext {
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public SourceRegionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceRegion(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceBasestationContext extends ExprContext {
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public SourceBasestationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceBasestation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SourceTypeContext extends ExprContext {
		public EqualityTestContext equalityTest() {
			return getRuleContext(EqualityTestContext.class,0);
		}
		public TerminalNode ID() { return getToken(SourceFilterParser.ID, 0); }
		public SourceTypeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SourceFilterVisitor ) return ((SourceFilterVisitor<? extends T>)visitor).visitSourceType(this);
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

	public final ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState, _p);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, RULE_expr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			switch (_input.LA(1)) {
			case 2:
				{
				_localctx = new SourceIdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(12); match(2);
				setState(13); equalityTest();
				setState(14); idList();
				}
				break;
			case 5:
				{
				_localctx = new SourceBasestationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(16); match(5);
				setState(17); equalityTest();
				setState(18); idList();
				}
				break;
			case 1:
				{
				_localctx = new SourceCountryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(20); match(1);
				setState(21); equalityTest();
				setState(22); idList();
				}
				break;
			case 8:
				{
				_localctx = new SourceTypeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(24); match(8);
				setState(25); equalityTest();
				setState(26); match(ID);
				}
				break;
			case 6:
				{
				_localctx = new SourceRegionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28); match(6);
				setState(29); equalityTest();
				setState(30); idList();
				}
				break;
			case 3:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(32); match(3);
				setState(33); expr(0);
				setState(34); match(4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(43);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new OrAndContext(new ExprContext(_parentctx, _parentState, _p));
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(38);
					if (!(2 >= _localctx._p)) throw new FailedPredicateException(this, "2 >= $_p");
					setState(39);
					((OrAndContext)_localctx).op = _input.LT(1);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
						((OrAndContext)_localctx).op = (Token)_errHandler.recoverInline(this);
					}
					consume();
					setState(40); expr(3);
					}
					} 
				}
				setState(45);
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
			setState(46);
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
		enterRule(_localctx, 6, RULE_idList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(48); match(ID);
			setState(53);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(49); match(7);
					setState(50); match(ID);
					}
					} 
				}
				setState(55);
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
		case 0: return 2 >= _localctx._p;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\20;\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\'\n\3"+
		"\3\3\3\3\3\3\7\3,\n\3\f\3\16\3/\13\3\3\4\3\4\3\5\3\5\3\5\7\5\66\n\5\f"+
		"\5\16\59\13\5\3\5\2\6\2\4\6\b\2\4\3\2\16\17\3\2\13\f=\2\n\3\2\2\2\4&\3"+
		"\2\2\2\6\60\3\2\2\2\b\62\3\2\2\2\n\13\5\4\3\2\13\f\7\2\2\3\f\3\3\2\2\2"+
		"\r\16\b\3\1\2\16\17\7\4\2\2\17\20\5\6\4\2\20\21\5\b\5\2\21\'\3\2\2\2\22"+
		"\23\7\7\2\2\23\24\5\6\4\2\24\25\5\b\5\2\25\'\3\2\2\2\26\27\7\3\2\2\27"+
		"\30\5\6\4\2\30\31\5\b\5\2\31\'\3\2\2\2\32\33\7\n\2\2\33\34\5\6\4\2\34"+
		"\35\7\20\2\2\35\'\3\2\2\2\36\37\7\b\2\2\37 \5\6\4\2 !\5\b\5\2!\'\3\2\2"+
		"\2\"#\7\5\2\2#$\5\4\3\2$%\7\6\2\2%\'\3\2\2\2&\r\3\2\2\2&\22\3\2\2\2&\26"+
		"\3\2\2\2&\32\3\2\2\2&\36\3\2\2\2&\"\3\2\2\2\'-\3\2\2\2()\6\3\2\3)*\t\2"+
		"\2\2*,\5\4\3\2+(\3\2\2\2,/\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\5\3\2\2\2/-\3"+
		"\2\2\2\60\61\t\3\2\2\61\7\3\2\2\2\62\67\7\20\2\2\63\64\7\t\2\2\64\66\7"+
		"\20\2\2\65\63\3\2\2\2\669\3\2\2\2\67\65\3\2\2\2\678\3\2\2\28\t\3\2\2\2"+
		"9\67\3\2\2\2\5&-\67";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
