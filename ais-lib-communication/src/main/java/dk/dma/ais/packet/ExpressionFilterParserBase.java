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
package dk.dma.ais.packet;

import com.google.common.collect.ImmutableSet;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import dk.dma.enav.util.function.Predicate;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterLexer;
import dk.dma.internal.ais.generated.parser.expressionfilter.ExpressionFilterParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static java.util.Objects.requireNonNull;

abstract class ExpressionFilterParserBase {

     /**
      * Map grammar field tokens to names of filter predicates
      *
      * @param fieldToken
      * @return name of filter predicate.
      */
     protected static String mapFieldTokenToFilterFactoryMethodName(String fieldToken) {
         switch (fieldToken) {
             case "s.id":
                 return "filterOnSourceId";
             case "s.bs":
                 return "filterOnSourceBasestation";
             case "s.country":
                 return "filterOnSourceCountry";
             case "s.type":
                 return "filterOnSourceType";
             case "s.region":
                 return "filterOnSourceRegion";
             case "m.year":
                 return "filterOnMessageReceiveTimeYear";
             case "m.month":
                 return "filterOnMessageReceiveTimeMonth";
             case "m.dom":
                 return "filterOnMessageReceiveTimeDayOfMonth";
             case "m.dow":
                 return "filterOnMessageReceiveTimeDayOfWeek";
             case "m.hour":
                 return "filterOnMessageReceiveTimeHour";
             case "m.minute":
                 return "filterOnMessageReceiveTimeMinute";
             case "m.id":
                 return "filterOnMessageId";
             case "m.mmsi":
                 return "filterOnMessageMmsi";
             case "m.imo":
                 return "filterOnMessageImo";
             case "m.type":
                 return "filterOnMessageShiptype";
             case "m.navstat":
                 return "filterOnMessageNavigationalStatus";
             case "m.name":
                 return "filterOnMessageName";
             case "m.cs":
                 return "filterOnMessageCallsign";
             case "m.sog":
                 return "filterOnMessageSpeedOverGround";
             case "m.cog":
                 return "filterOnMessageCourseOverGround";
             case "m.hdg":
                 return "filterOnMessageTrueHeading";
             case "m.lon":
                 return "filterOnMessageLongitude";
             case "m.lat":
                 return "filterOnMessageLatitude";
             case "m.draught":
                 return "filterOnMessageDraught";
         }
         throw new IllegalArgumentException("No mapping to predicate name for field " + fieldToken);
     }

     /**
      * Extract an array of Integers from an ANTLR list of TerminalNodes
      *
      * @param ints
      * @return
      */
     protected static Integer[] extractIntegers(List<TerminalNode> ints) {
         int n = ints.size();
         Integer[] integers = new Integer[n];
         for (int i = 0; i < n; i++) {
             integers[i] = Integer.valueOf(ints.get(i).getText());
         }
         return integers;
     }

     /**
      * Extract an array of Strings from ANTLR list of TerminalNodes or StringContexts.
      *
      * @param strs
      * @return
      */
     protected static String[] extractStrings(List strs) {
         int n = strs.size();
         String[] strings = new String[n];
         for (int i = 0; i < n; i++) {
             Object o = strs.get(i);
             if (o instanceof TerminalNode) {
                 strings[i] = removeSurroundingApostrophes(((TerminalNode) o).getText());
             } else if (o instanceof ExpressionFilterParser.StringContext) {
                 strings[i] = removeSurroundingApostrophes(((ExpressionFilterParser.StringContext) o).getText());
             } else {
                 throw new IllegalArgumentException(o.getClass().toString());
             }
         }
         return strings;
     }

     /**
      * Extract string value from a 'string' token
      *
      * @param string
      * @return
      */
     protected static String extractString(ExpressionFilterParser.StringContext string) {
         String result = null;
         if (string.STRING() != null) {
             result = removeSurroundingApostrophes(string.STRING().getText());
         } else if (string.WORD() != null) {
             result = string.WORD().getText();
         } else if (string.number() != null) {
             result = string.number().getText();
         }
         return result;
     }

     /**
      * Check if context has INT() method and it returns a value different from null.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasINT(ParserRuleContext ctx) {
         return hasMethod(ctx, "INT") && invokeMethod(ctx, "INT") != null;
     }

     /**
      * Check if context has String() method and it returns a value different from null.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasString(ParserRuleContext ctx) {
         return hasMethod(ctx, "string") && invokeMethod(ctx, "string") != null;
     }

     /**
      * Check if context has number() method and it returns a value different from null.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasNumber(ParserRuleContext ctx) {
         return hasMethod(ctx, "number") && invokeMethod(ctx, "number") != null;
     }

     /**
      * Check if context has compareTo() method and it returns a value different from null.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasCompareTo(ParserRuleContext ctx) {
         return hasMethod(ctx, "compareTo") && invokeMethod(ctx, "compareTo") != null;
     }

     /**
      * Check if context has intList() method and it returns a value different from null.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasIntList(ParserRuleContext ctx) {
         return hasMethod(ctx, "intList") && invokeMethod(ctx, "intList") != null;
     }

     /**
      * Check if context has intRange() method and it returns a value different from null.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasIntRange(ParserRuleContext ctx) {
         return hasMethod(ctx, "intRange") && invokeMethod(ctx, "intRange") != null;
     }

     /**
      * Check if context has numberRange() method and it returns a value different from null.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasNumberRange(ParserRuleContext ctx) {
         return hasMethod(ctx, "numberRange") && invokeMethod(ctx, "numberRange") != null;
     }

     /**
      * Call the numberRange method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static Object invokeMethod(ParserRuleContext ctx, String methodName) {
         try {
             return ctx.getClass().getMethod(methodName).invoke(ctx);
         } catch (NoSuchMethodException e) {
             e.printStackTrace(System.err);
             return null;
         } catch (InvocationTargetException e) {
             e.printStackTrace(System.err);
             return null;
         } catch (IllegalAccessException e) {
             e.printStackTrace(System.err);
             return null;
         }
     }

     /**
      * Check if context has a method of of the given name which takes no arguments.
      *
      * @param ctx
      * @return
      */
     protected static boolean hasMethod(ParserRuleContext ctx, String methodName) {
         try {
             ctx.getClass().getMethod(methodName);
         } catch (NoSuchMethodException e) {
             return false;
         }
         return true;
     }

     /**
      * Call the compareTo method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static ExpressionFilterParser.CompareToContext invokeCompareTo(ParserRuleContext ctx) {
         return (ExpressionFilterParser.CompareToContext) invokeMethod(ctx, "compareTo");
     }

     /**
      * Call the string() method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static ExpressionFilterParser.StringContext invokeString(ParserRuleContext ctx) {
         return (ExpressionFilterParser.StringContext) invokeMethod(ctx, "string");
     }

     /**
      * Call the INT() method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static TerminalNode invokeINT(ParserRuleContext ctx) {
         return (TerminalNode) invokeMethod(ctx, "INT");
     }

     /**
      * Call the number() method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static ExpressionFilterParser.NumberContext invokeNumber(ParserRuleContext ctx) {
         return (ExpressionFilterParser.NumberContext) invokeMethod(ctx, "number");
     }

     /**
      * Call the intList method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static ExpressionFilterParser.IntListContext invokeIntList(ParserRuleContext ctx) {
         return (ExpressionFilterParser.IntListContext) invokeMethod(ctx, "intList");
     }

     /**
      * Call the intRange method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static ExpressionFilterParser.IntRangeContext invokeIntRange(ParserRuleContext ctx) {
         return (ExpressionFilterParser.IntRangeContext) invokeMethod(ctx, "intRange");
     }

     /**
      * Call the numberRange method on the ctx (if it exists) and return the result.
      *
      * @param ctx
      * @return
      */
     protected static ExpressionFilterParser.NumberRangeContext invokeNumberRange(ParserRuleContext ctx) {
         return (ExpressionFilterParser.NumberRangeContext) invokeMethod(ctx, "numberRange");
     }

     /**
      * Check if the notin token exists in the context; and if so, negate the filter.
      *
      * @param ctx
      * @param filter
      * @return
      */
     protected static Predicate checkNegate(ParserRuleContext ctx, Predicate filter) {
         if (filter != null) {
             try {
                 Method notin = ctx.getClass().getDeclaredMethod("notin");
                 Object negate = notin.invoke(ctx);
                 if (negate != null) {
                     filter = filter.negate();
                 }
             } catch (NoSuchMethodException e) {
                 // Expected behaviour
             } catch (InvocationTargetException e) {
                 e.printStackTrace(System.err);
             } catch (IllegalAccessException e) {
                 e.printStackTrace(System.err);
             }
               /*
               if (ctx.notin() != null) {
                   filter = filter.negate();
               }
               */
         }
         return filter;
     }

     /**
      * Remove the leading and trailing apostrophe from the string.
      *
      * @param string
      * @return
      */
     protected static String removeSurroundingApostrophes(String string) {
         String result = string;
         if (result.startsWith("'")) {
             result = result.substring(1);
         }
         if (result.endsWith("'")) {
             result = result.substring(0, result.length() - 1);
         }
         return result;
     }

     /**
      * Get the list of AIS ship types matching the label text.
      * Based on Rec. ITU-R M.1371-4 - table 50.
      *
      * @param labels the ship type label; e.g. 'fishing'.
      * @return a list of matching ship types.
      */
     protected static Set<Integer> getShipTypes(String[] labels) {
         HashSet<Integer> shipTypes = new HashSet<>();
         for (String label : labels) {
             shipTypes.addAll(getShipTypes(label));
         }
         return shipTypes;
     }

     /**
      * Get the list of AIS ship types matching the label text.
      * Based on Rec. ITU-R M.1371-4 - table 50.
      *
      * @param label the ship type label; e.g. 'fishing'.
      * @return a list of matching ship types.
      */
     protected static ImmutableSet<Integer> getShipTypes(String label) {
         HashSet<Integer> shipTypes = new HashSet<>();
         for (int i = 0; i < 100; i++) {
             ShipTypeCargo shipTypeCargo = new ShipTypeCargo(i);
             if (shipTypeCargo.getShipType().toString().equalsIgnoreCase(label)) {
                 shipTypes.add(i);
             }
         }
         return ImmutableSet.copyOf(shipTypes);
     }

     /**
      * Get the list of AIS navigational statuses matching the label text.
      * Based on Rec. ITU-R M.1371-4 - table 45.
      *
      * @param labels
      * @return
      */
     protected static ImmutableSet<Integer> getNavigationalStatuses(String[] labels) {
         HashSet<Integer> navstats = new HashSet<>();
         for (String label : labels) {
             try {
                 navstats.add(NavigationalStatus.valueOf(label).getCode());
             } catch (IllegalArgumentException e) {
                 System.out.println("WARN: " + e.getMessage());
             }
         }
         return ImmutableSet.copyOf(navstats);
     }

     protected static SourceType[] getSourceTypes(String[] labels) {
         ArrayList<SourceType> r = new ArrayList<>();
         for (String label : labels) {
             r.add(SourceType.fromString(label));
         }
         return r.toArray(new SourceType[r.size()]);
     }

     protected static Country[] getCountries(String[] countryCodes) {
         List<Country> countries = Country.findAllByCode(countryCodes);
         return countries.toArray(new Country[countries.size()]);
     }

     protected static ImmutableSet<Integer> getWeekdays(String[] strings) {
         HashSet<Integer> set = new HashSet<>();
         for (String string : strings) {
             set.add(mapStringToCalendarDayOfWeek(string));
         }
         return ImmutableSet.copyOf(set);
     }

     protected static ImmutableSet<Integer> getMonths(String[] strings) {
         HashSet<Integer> set = new HashSet<>();
         for (String string : strings) {
             set.add(mapStringToCalendarMonth(string));
         }
         return ImmutableSet.copyOf(set);
     }

    // ---

     /**
      * Create a new predicate which will perform a comparison of the given field in an AisMessage to the given value.
      *
      * @param fieldToken the field to compare
      * @param operator   the type of comparison to make (=, !=, >, >=, <=, <)
      * @param value      the fixed value to compare against
      * @return true if filter is passed or indeterminate; false if filter blocks.
      */
     protected static <T, R> Predicate<T> createFilterPredicateForComparison(Class<? extends FilterFactory> filterFactoryClass, @NotNull String fieldToken, @NotNull String operator, @NotNull R value) {
         Predicate<T> filter = null;
         String filterFactoryMethodName = mapFieldTokenToFilterFactoryMethodName(fieldToken);
         try {
             Method filterFactoryMethod = filterFactoryClass.getDeclaredMethod(filterFactoryMethodName, CompareToOperator.class, value.getClass());
             filter = (Predicate<T>) filterFactoryMethod.invoke(null, CompareToOperator.fromString(operator), value);
         } catch (InvocationTargetException e) {
             e.printStackTrace(System.err);
         } catch (NoSuchMethodException e) {
             e.printStackTrace(System.err);
         } catch (IllegalAccessException e) {
             e.printStackTrace(System.err);
         }
         return filter;
     }

     /**
      * Create new predicate to filter for given field to be in a range of values.
      *
      * @param fieldName the field to filter
      * @param min       the minimum value that the field must have to pass the filter.
      * @param max       the maximum value that the field must have to pass the filter.
      * @return true if filter is passed or indeterminate; false if filter blocks.
      */
     protected static <T> Predicate<T> createFilterPredicateForRange(Class<? extends FilterFactory> filterFactoryClass, @NotNull String fieldName, Number min, Number max) {
         if (max.floatValue() < min.floatValue()) {
             throw new IllegalArgumentException("max < min");
         }
         Predicate<T> filter = null;
         try {
             String filterFactoryMethodName = mapFieldTokenToFilterFactoryMethodName(fieldName);
             Method filterFactoryMethod = null;
             if (min instanceof Integer) {
                 filterFactoryMethod = filterFactoryClass.getDeclaredMethod(filterFactoryMethodName, int.class, int.class);
             } else if (min instanceof Float) {
                 filterFactoryMethod = filterFactoryClass.getDeclaredMethod(filterFactoryMethodName, float.class, float.class);
             }
             filter = (Predicate<T>) filterFactoryMethod.invoke(null, min, max);
         } catch (NoSuchMethodException e) {
             e.printStackTrace(System.err);
         } catch (InvocationTargetException e) {
             e.printStackTrace(System.err);
         } catch (IllegalAccessException e) {
             e.printStackTrace(System.err);
         }
         return filter;
     }

     /**
      * Create new predicate to filter for given field to be in list of allowed values.
      *
      * @param fieldName the field to filter
      * @param list      an array of allowed values for the field to pass the filter
      * @param <T>
      * @return true if filter is passed or indeterminate; false if filter blocks.
      */
     protected static <T, R> Predicate<T> createFilterPredicateForList(Class<? extends FilterFactory> filterFactoryClass, String fieldName, R[] list) {
         Predicate<T> filter = null;
         try {
             String filterFactoryMethodName = mapFieldTokenToFilterFactoryMethodName(fieldName);
             Method filterFactoryMethod = filterFactoryClass.getDeclaredMethod(filterFactoryMethodName, list.getClass());
             if (list instanceof String[]) {
                 String[] preprocessedStrings = new String[list.length];
                 for (int i = 0; i < list.length; i++) {
                     String preprocessedString = (String) list[i];
                     if (preprocessedString.startsWith("'")) {
                         preprocessedString = preprocessedString.substring(1);
                     }
                     if (preprocessedString.endsWith("'")) {
                         preprocessedString = preprocessedString.substring(0, preprocessedString.length() - 1);
                     }
                     preprocessedStrings[i] = preprocessedString;
                 }
                 list = (R[]) preprocessedStrings;
             }
             filter = (Predicate<T>) filterFactoryMethod.invoke(null, (Object) list);
         } catch (NoSuchMethodException e) {
             e.printStackTrace(System.err);
         } catch (InvocationTargetException e) {
             e.printStackTrace(System.err);
         } catch (IllegalAccessException e) {
             e.printStackTrace(System.err);
         }
         return filter;
     }

     protected static <T> Predicate<T> createFilterPredicateForStringComparison(Class<? extends FilterFactory> filterFactoryClass, ParserRuleContext ctx) {
         Predicate<T> filter = null;
         if (hasCompareTo(ctx) && hasString(ctx)) {
             String fieldName = ctx.getStart().getText();
             String operator = invokeCompareTo(ctx).getText();
             String string = invokeString(ctx).getText();
             filter = createFilterPredicateForComparison(filterFactoryClass, fieldName, operator, string);
         }
         return filter;
     }

     protected static <T> Predicate<T> createFilterPredicateForIntComparison(Class<? extends FilterFactory> filterFactoryClass, ParserRuleContext ctx) {
         Predicate<T> filter = null;
         if (hasCompareTo(ctx) && hasINT(ctx)) {
             String fieldName = ctx.getStart().getText();
             String operator = invokeCompareTo(ctx).getText();
             Integer id = Integer.valueOf(invokeINT(ctx).getText());
             filter = createFilterPredicateForComparison(filterFactoryClass, fieldName, operator, id);
         }
         return filter;
     }

     protected static <T> Predicate<T> createFilterPredicateForNumberComparison(Class<? extends FilterFactory> filterFactoryClass, ParserRuleContext ctx) {
         Predicate<T> filter = null;
         String fieldName = ctx.getStart().getText();
         if (hasCompareTo(ctx) && hasNumber(ctx)) {
             String operator = invokeCompareTo(ctx).getText();
             Float sog = Float.valueOf(invokeNumber(ctx).getText());
             filter = createFilterPredicateForComparison(filterFactoryClass, fieldName, operator, sog);
         }
         return filter;
     }

     protected static <T> Predicate<T> createFilterPredicateForNumberRange(Class<? extends FilterFactory> filterFactoryClass, ParserRuleContext ctx) {
         Predicate<T> filter = null;
         String fieldName = ctx.getStart().getText();
         if (hasNumberRange(ctx)) {
             float min = Float.valueOf(invokeNumberRange(ctx).number().get(0).getText());
             float max = Float.valueOf(invokeNumberRange(ctx).number().get(1).getText());
             filter = createFilterPredicateForRange(filterFactoryClass, fieldName, min, max);
         }
         return checkNegate(ctx, filter);
     }

     protected static <T> Predicate<T> createFilterPredicateForIntRangeOrIntList(Class<? extends FilterFactory> filterFactoryClass, ParserRuleContext ctx) {
         Predicate<T> filter = null;
         String fieldName = ctx.getStart().getText();
         if (hasIntList(ctx)) {
             Integer[] ints = extractIntegers(invokeIntList(ctx).INT());
             filter = createFilterPredicateForList(filterFactoryClass, fieldName, ints);
         } else if (hasIntRange(ctx)) {
             int min = Integer.valueOf(invokeIntRange(ctx).INT().get(0).getText());
             int max = Integer.valueOf(invokeIntRange(ctx).INT().get(1).getText());
             filter = createFilterPredicateForRange(filterFactoryClass, fieldName, min, max);
         }
         return checkNegate(ctx, filter);
     }

     // ---

     protected static <T> Predicate<T> createFilterPredicateForTimeComparison(Class<? extends AisPacketFilters> filterFactoryClass, ExpressionFilterParser.MessageTimeContext ctx) {
               Predicate<T> filter = null;

               try {
                   String field = ctx.getStart().getText();
                   CompareToOperator operator = CompareToOperator.fromString(invokeCompareTo(ctx).getText());
                   Method filterFactoryMethod = filterFactoryClass.getDeclaredMethod("filterOnMessageReceiveTime", CompareToOperator.class, int.class, int.class);

                   if ("m.year".equalsIgnoreCase(field)) {
                       filter = (Predicate<T>) filterFactoryMethod.invoke(null, operator, Calendar.YEAR, Integer.valueOf(ctx.INT().getText()));
                   } else if ("m.month".equalsIgnoreCase(field)) {
                       int month = ctx.INT() != null ? Integer.parseInt(ctx.INT().getText()) : mapStringToCalendarMonth(ctx.string().getText());
                       filter = (Predicate<T>) filterFactoryMethod.invoke(null, operator, Calendar.MONTH, month);
                   } else if ("m.dom".equalsIgnoreCase(field)) {
                       filter = (Predicate<T>) filterFactoryMethod.invoke(null, operator, Calendar.DAY_OF_MONTH, Integer.valueOf(ctx.INT().getText()));
                   } else if ("m.dow".equalsIgnoreCase(field)) {
                       int dow = ctx.INT() != null ? Integer.parseInt(ctx.INT().getText()) : mapStringToCalendarDayOfWeek(ctx.string().getText());
                       filter = (Predicate<T>) filterFactoryMethod.invoke(null, operator, Calendar.DAY_OF_WEEK, dow);
                   } else if ("m.hour".equalsIgnoreCase(field)) {
                       filter = (Predicate<T>) filterFactoryMethod.invoke(null, operator, Calendar.HOUR_OF_DAY, Integer.valueOf(ctx.INT().getText()));
                   } else if ("m.minute".equalsIgnoreCase(field)) {
                       filter = (Predicate<T>) filterFactoryMethod.invoke(null, operator, Calendar.MINUTE, Integer.valueOf(ctx.INT().getText()));
                   } else {
                       throw new IllegalArgumentException(field);
                   }
               } catch (NoSuchMethodException e) {
                   e.printStackTrace(System.err);
               } catch (InvocationTargetException e) {
                   e.printStackTrace(System.err);
               } catch (IllegalAccessException e) {
                   e.printStackTrace(System.err);
               }

               return filter;
           }

     private static int mapStringToCalendarDayOfWeek(String text) {
         int dow = -1;
         try {
             Date date = new SimpleDateFormat("EEE", Locale.ENGLISH).parse(text.toUpperCase());
             Calendar calendar = Calendar.getInstance();
             calendar.setTime(date);
             dow = calendar.get(Calendar.DAY_OF_WEEK);
         } catch (ParseException e) {
             e.printStackTrace(System.err);
         }
         return dow - 1; // 1 = man, 7 = sun
     }

     private static int mapStringToCalendarMonth(String text) {
         int month = -1;
         try {
             Date date = new SimpleDateFormat("MMMMM", Locale.ENGLISH).parse(text.toUpperCase());
             Calendar calendar = Calendar.getInstance();
             calendar.setTime(date);
             month = calendar.get(Calendar.MONTH);
         } catch (ParseException e) {
             e.printStackTrace(System.err);
         }
         return month + 1; // 1 = jan
     }

    static ExpressionFilterParser.FilterContext createFilterContext(String filter) {
        ANTLRInputStream input = new ANTLRInputStream(requireNonNull(filter));
        ExpressionFilterLexer lexer = new ExpressionFilterLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionFilterParser parser = new ExpressionFilterParser(tokens);

        // Better errors
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(new VerboseListener());
        parser.addErrorListener(new VerboseListener());

        return parser.filter();
    }

    static class VerboseListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                                String msg, RecognitionException e) {
            throw new IllegalArgumentException(msg + " @ character " + charPositionInLine);
            // if (recognizer instanceof Parser)
            // List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
            // Collections.reverse(stack);
            // System.err.println("rule stack: " + stack);
            // System.err.println("line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + sentenceStr);
        }
    }

}
