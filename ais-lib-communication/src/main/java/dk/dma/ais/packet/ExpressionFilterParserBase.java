/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.packet;

import com.google.common.collect.ImmutableSet;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.message.ShipTypeCargo;
import dk.dma.ais.packet.AisPacketTags.SourceType;
import dk.dma.enav.model.Country;
import dk.dma.enav.model.geometry.Area;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Circle;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import java.util.function.Predicate;
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

/**
 * This class provides shared behaviour for different types of ExpressionFilterParsers.
 */
abstract class ExpressionFilterParserBase {

    /**
     * Create a filter predicate with will compare the left-hand side to the right-hand side both assumed to be present
     * in the supplied ANTLR context.
     *
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForComparison(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        Predicate<T> filter = null;
        String fieldName = ctx.getStart().getText();

        if (hasCompareTo(ctx)) {
            String operator = invokeCompareTo(ctx).getText();
            if (hasINT(ctx)) {
                Integer rhs = Integer.valueOf(invokeINT(ctx).getText());
                filter = createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, fieldName, operator, rhs);
            } else if (hasNumber(ctx)) {
                Float rhs = Float.valueOf(invokeNumber(ctx).getText());
                filter = createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, fieldName, operator, rhs);
            } else if (hasString(ctx)) {
                   /* if (isNamedValue(fieldName)) {
                        filter = createFilterPredicateForComparisonOfNamedValue(filterPredicateFactoryClass, filterPredicateFactory, ctx);
                    } else { */
                ExpressionFilterParser.StringContext stringCtx = invokeString(ctx);
                if (stringCtx.number() != null) {
                    Integer rhs = Integer.valueOf(extractString(stringCtx));
                    filter = createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, fieldName, operator, rhs);
                } else if (stringCtx.STRING() != null) {
                    String rhs = stringCtx.getText();
                    filter = createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, fieldName, operator, rhs);
                }
                // }
            } else {
                throw new IllegalArgumentException();
            }
        }
        return filter;
    }

    /**
     * Create a filter predicate with will test the left-side value assumed to be present
     * in the supplied ANTLR context against the list or range supplied on the right-hand side.
     *
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForRangeOrList(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        Predicate<T> filter = null;
        String fieldName = ctx.getStart().getText();
        if (hasIntList(ctx)) {
            Integer[] ints = extractIntegers(invokeIntList(ctx).INT());
            filter = createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, ints);
        } else if (hasStringList(ctx)) {
            String[] strings = extractStrings(invokeStringList(ctx).string());
            filter = createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, strings);
        } else if (hasIntRange(ctx)) {
            int min = Integer.valueOf(invokeIntRange(ctx).INT().get(0).getText());
            int max = Integer.valueOf(invokeIntRange(ctx).INT().get(1).getText());
            filter = createFilterPredicateForRange(filterPredicateFactoryClass, filterPredicateFactory, fieldName, min, max);
        } else if (hasNumberRange(ctx)) {
            float min = Float.valueOf(invokeNumberRange(ctx).number().get(0).getText());
            float max = Float.valueOf(invokeNumberRange(ctx).number().get(1).getText());
            filter = createFilterPredicateForRange(filterPredicateFactoryClass, filterPredicateFactory, fieldName, min, max);
        } else {
            throw new IllegalArgumentException();
        }
        return checkNegate(ctx, filter);
    }

    /**
     * Create predicate for comparisons of strings, including the LIKE operator.
     * @param filterPredicateFactoryClass
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForStringComparison(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        Predicate<T> filter = null;
        if (hasString(ctx)) {
            String fieldName = ctx.getStart().getText();
            String string = invokeString(ctx).getText();
            if (hasCompareTo(ctx)) {
                ExpressionFilterParser.CompareToContext compareToContext = invokeCompareTo(ctx);
                String operator = compareToContext.getText();
                filter = createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, fieldName, operator, string);
            } else if (hasLIKE(ctx)) {
                filter = createFilterPredicateForMatch(filterPredicateFactoryClass, filterPredicateFactory, fieldName, string);
            }
        }
        return filter;
    }

    /**
     * Create predicate for comparison of ship types (as numbers or named in strings).
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForComparisonOfShipType(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasCompareTo(ctx) && hasString(ctx)) {
            ExpressionFilterParser.StringContext rhsCtx = invokeString(ctx);
            if (rhsCtx.STRING() != null) {
                Set<Integer> shipTypes = getShipTypes(extractString(rhsCtx));
                String fieldName = ctx.getStart().getText();
                String operator = invokeCompareTo(ctx).getText();
                if (!"=".equals(operator)) {
                    throw new IllegalArgumentException("Sorry, only '=' operator supported for comparison on named ship type.");
                }
                Predicate<T> filter = createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, shipTypes.toArray(new Integer[shipTypes.size()]));
                return filter;
            } else {
                return createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, ctx);
            }
        } else {
            throw new IllegalArgumentException(ctx.toStringTree());
        }
    }

    /**
     * Create predicate for testing against lists of ship types (as numbers or named in strings).
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForListOfShipType(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasStringList(ctx)) {
            Set<Integer> shipTypes = getShipTypes(extractStrings(invokeStringList(ctx).string()));
            String fieldName = ctx.getStart().getText();
            return createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, shipTypes.toArray(new Integer[shipTypes.size()]));
        } else {
            return createFilterPredicateForRangeOrList(filterPredicateFactoryClass, filterPredicateFactory, ctx);
        }
    }

    /**
     * Create a predicate for comparing navigational statuses (as numbers or named in strings).
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForComparisonOfNavigationalStatus(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasCompareTo(ctx) && hasString(ctx)) {
            ExpressionFilterParser.StringContext rhsCtx = invokeString(ctx);
            if (rhsCtx.STRING() != null) {
                Set<Integer> navstats = getNavigationalStatuses(new String[]{extractString(rhsCtx)});
                String fieldName = ctx.getStart().getText();
                String operator = invokeCompareTo(ctx).getText();
                if (!operator.equals("=")) {
                    throw new IllegalArgumentException("Sorry, only '=' operator supported for comparison on named navigational status.");
                }
                return createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, navstats.toArray(new Integer[navstats.size()]));
            } else {
                return createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, ctx);
            }
        } else {
            throw new IllegalArgumentException(ctx.toStringTree());
        }
    }

    /**
     * Create predicate for testing against lists of navigational statuses (as numbers or named in strings).
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForListOfNavigationalStatus(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasStringList(ctx)) {
            Set<Integer> navstats = getNavigationalStatuses(extractStrings(invokeStringList(ctx).string()));
            String fieldName = ctx.getStart().getText();
            return createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, navstats.toArray(new Integer[navstats.size()]));
        } else {
            return createFilterPredicateForRangeOrList(filterPredicateFactoryClass, filterPredicateFactory, ctx);
        }
    }

    /**
     * Create predicate for testing against lists of countries.
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForListOfCountry(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasStringList(ctx)) {
            Country[] countries = getCountries(extractStrings(invokeStringList(ctx).string()));
            String fieldName = ctx.getStart().getText();
            return checkNegate(ctx, createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, countries));
        } else {
            throw new IllegalArgumentException(ctx.toStringTree());
        }
    }

    /**
     * Create predicate for testing against lists of source types.
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForListOfSourceType(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasStringList(ctx)) {
            SourceType[] sourceTypes = getSourceTypes(extractStrings(invokeStringList(ctx).string()));
            String fieldName = ctx.getStart().getText();
            return checkNegate(ctx, createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, sourceTypes));
        } else {
            throw new IllegalArgumentException(ctx.toStringTree());
        }
    }

    /**
     * Create predicate for comparing months (as numbers or named in strings).
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForComparisonOfMonth(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasCompareTo(ctx)) {
            if (hasString(ctx)) {
                int month = mapStringToCalendarMonth(extractString(invokeString(ctx)));
                String fieldName = ctx.getStart().getText();
                String operator = invokeCompareTo(ctx).getText();
                return createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, fieldName, operator, month);
            } else {
                return createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, ctx);
            }
        } else {
            throw new IllegalArgumentException(ctx.toStringTree());
        }
    }

    /**
     * Create predicate for testing against lists of navigational statuses (as numbers or named in strings) or
     * ranges of months (as numbers).
     *
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForRangeOrListOfMonth(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        Predicate<T> filter = null;
        String fieldName = ctx.getStart().getText();
        Set<Integer> months;
        if (hasStringList(ctx)) {
            months = mapStringsToCalendarMonths(extractStrings(invokeStringList(ctx).string()));
            filter = checkNegate(ctx, createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, months.toArray(new Integer[months.size()])));
        } else if (hasIntList(ctx)) {
            months = ImmutableSet.copyOf(extractIntegers(invokeIntList(ctx).INT()));
            filter = checkNegate(ctx, createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, months.toArray(new Integer[months.size()])));
        } else if (hasIntRange(ctx)) {
            int min = Integer.parseInt(invokeIntRange(ctx).INT(0).getText());
            int max = Integer.parseInt(invokeIntRange(ctx).INT(1).getText());
            filter = checkNegate(ctx, createFilterPredicateForRange(filterPredicateFactoryClass, filterPredicateFactory, fieldName, min, max));
        }
        return filter;
    }

    /**
     * Create predicate for comparing weekdays (as numbers (monday = 1) or named in strings (in English)).
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForComparisonOfWeekday(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        if (hasCompareTo(ctx)) {
            if (hasString(ctx)) {
                int dow = mapStringToCalendarDayOfWeek(extractString(invokeString(ctx)));
                String fieldName = ctx.getStart().getText();
                String operator = invokeCompareTo(ctx).getText();
                return createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, fieldName, operator, dow);
            } else {
                return createFilterPredicateForComparison(filterPredicateFactoryClass, filterPredicateFactory, ctx);
            }
        } else {
            throw new IllegalArgumentException(ctx.toStringTree());
        }
    }

    /**
     * Create predicate for testing against lists of weekdays (as numbers (monday = 1) or named in strings (in
     * English))) or ranges of months (as numbers).
     *
     * @param filterPredicateFactoryClass
     * @param filterPredicateFactory
     * @param ctx
     * @param <T>
     * @return
     */
    protected static <T> Predicate<T> createFilterPredicateForRangeOrListOfWeekday(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        Predicate<T> filter = null;
        String fieldName = ctx.getStart().getText();
        Set<Integer> weekdays;
        if (hasStringList(ctx)) {
            weekdays = mapStringsToCalendarDaysOfWeek(extractStrings(invokeStringList(ctx).string()));
            filter = checkNegate(ctx, createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, weekdays.toArray(new Integer[weekdays.size()])));
        } else if (hasIntList(ctx)) {
            weekdays = ImmutableSet.copyOf(extractIntegers(invokeIntList(ctx).INT()));
            filter = checkNegate(ctx, createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, weekdays.toArray(new Integer[weekdays.size()])));
        } else if (hasIntRange(ctx)) {
            int min = Integer.parseInt(invokeIntRange(ctx).INT(0).getText());
            int max = Integer.parseInt(invokeIntRange(ctx).INT(1).getText());
            filter = checkNegate(ctx, createFilterPredicateForRange(filterPredicateFactoryClass, filterPredicateFactory, fieldName, min, max));
        }
        return filter;
    }

    protected static <T> Predicate<T> createFilterPredicateForComparisonOfMessageReceiveTime(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx, int calendarField) {
        Predicate<T> filter = null;
        String filterPredicateFactoryMethodName = "filterOnMessageReceiveTime";
        try {
            CompareToOperator operator = CompareToOperator.fromString(invokeCompareTo(ctx).getText());
            Method filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName, CompareToOperator.class, int.class, int.class);
            filter = (Predicate<T>) filterPredicateFactoryMethod.invoke(filterPredicateFactory, operator, calendarField, Integer.parseInt(invokeINT(ctx).getText()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace(System.err);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return filter;
    }

    protected static <T> Predicate<T> createFilterPredicateForRangeOrListOfMessageReceiveTime(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        Predicate<T> filter = null;
        String filterPredicateFactoryMethodName = mapFieldTokenToFilterPredicateFactoryMethodName(ctx.getStart().getText());
        try {
            Method filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName, int.class, int.class);
            if (hasIntList(ctx)) {
                String fieldName = ctx.getStart().getText();
                Set<Integer> intList = ImmutableSet.copyOf(extractIntegers(invokeIntList(ctx).INT()));
                filter = checkNegate(ctx, createFilterPredicateForList(filterPredicateFactoryClass, filterPredicateFactory, fieldName, intList.toArray(new Integer[intList.size()])));
            } else if (hasIntRange(ctx)) {
                int min = Integer.parseInt(invokeIntRange(ctx).INT(0).getText());
                int max = Integer.parseInt(invokeIntRange(ctx).INT(1).getText());
                filter = (Predicate<T>) filterPredicateFactoryMethod.invoke(filterPredicateFactory, min, max);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace(System.err);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return filter;
    }

    /**
     *
     */
    protected static <T> Predicate<T> createFilterPredicateForPositionWithin(Class filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, ParserRuleContext ctx) {
        Predicate<T> filter = null;
        Area area = null;

        if (hasBbox(ctx)) {
            List<ExpressionFilterParser.NumberContext> params = invokeBbox(ctx).number();
            float latitude1 = Float.valueOf(params.get(0).getText());
            float longitude1 = Float.valueOf(params.get(1).getText());
            float latitude2 = Float.valueOf(params.get(2).getText());
            float longitude2 = Float.valueOf(params.get(3).getText());
            Position corner1 = Position.create(latitude1, longitude1);
            Position corner2 = Position.create(latitude2, longitude2);
            area = BoundingBox.create(corner1, corner2, CoordinateSystem.CARTESIAN);
        } else if (hasCircle(ctx)) {
            List<ExpressionFilterParser.NumberContext> params = invokeCircle(ctx).number();
            float latitude = Float.valueOf(params.get(0).getText());
            float longitude = Float.valueOf(params.get(1).getText());
            float radius = Float.valueOf(params.get(2).getText());
            area = new Circle(latitude, longitude, radius, CoordinateSystem.CARTESIAN);
        }

        if (area != null) {
            String filterPredicateFactoryMethodName = mapFieldTokenToFilterPredicateFactoryMethodName(ctx.getStart().getText());
            try {
                Method filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName, Area.class);
                filter = (Predicate<T>) filterPredicateFactoryMethod.invoke(filterPredicateFactory, area);
            } catch (NoSuchMethodException e) {
                e.printStackTrace(System.err);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return filter;
    }

    /**
     * Create a new predicate which will perform a comparison of the given field in an AisMessage to the given value.
     *
     * @param filterPredicateFactoryClass the class of FilterPredicateFactory to use. Can be null only if a non-null statefulFilterPredicateFactory parameters is supplied.
     * @param filterPredicateFactory the statefulFilterPredicateFactory to use. Can be null to use static factory method calls on class filterPredicateFactoryClass.
     * @param fieldName the field to compare
     * @param operator the type of comparison to make (=, !=, >, >=, <=, <)
     * @param value the fixed value to compare against
     * @return true if filter is passed or indeterminate; false if filter blocks.
     */
    private static <T, R> Predicate<T> createFilterPredicateForComparison(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, @NotNull String fieldName, @NotNull String operator, @NotNull R value) {
        // Validate args
        if (filterPredicateFactoryClass == null && filterPredicateFactory == null) {
            throw new IllegalArgumentException();
        }
        if (filterPredicateFactoryClass != null && filterPredicateFactory != null && !filterPredicateFactory.getClass().equals(filterPredicateFactoryClass)) {
            throw new IllegalArgumentException();
        }
        if (filterPredicateFactoryClass == null && filterPredicateFactory != null) {
            filterPredicateFactoryClass = filterPredicateFactory.getClass();
        }
        //
        Predicate<T> filter = null;
        String filterPredicateFactoryMethodName = mapFieldTokenToFilterPredicateFactoryMethodName(fieldName);
        try {
            Method filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName, CompareToOperator.class, value.getClass());
            filter = (Predicate<T>) filterPredicateFactoryMethod.invoke(filterPredicateFactory, CompareToOperator.fromString(operator), value);
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
     * Create a new predicate which will perform a glob matchesGlob of the given field in an AisMessage to the given value.
     *
     * @param filterPredicateFactoryClass
     * @param fieldToken
     * @param value
     * @param <T>
     * @param <R>
     * @return
     */
    private static <T, R> Predicate<T> createFilterPredicateForMatch(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, @NotNull String fieldToken, @NotNull R value) {
        Predicate<T> filter = null;
        String filterPredicateFactoryMethodName = mapFieldTokenToFilterPredicateFactoryMethodName(fieldToken);
        try {
            Method filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName + "Match", String.class);
            filter = (Predicate<T>) filterPredicateFactoryMethod.invoke(filterPredicateFactory, value);
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
     * @param min the minimum value that the field must have to pass the filter.
     * @param max the maximum value that the field must have to pass the filter.
     * @return true if filter is passed or indeterminate; false if filter blocks.
     */
    private static <T> Predicate<T> createFilterPredicateForRange(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, @NotNull String fieldName, Number min, Number max) {
        if (max.floatValue() < min.floatValue()) {
            throw new IllegalArgumentException("max < min");
        }
        Predicate<T> filter = null;
        try {
            String filterPredicateFactoryMethodName = mapFieldTokenToFilterPredicateFactoryMethodName(fieldName);
            Method filterPredicateFactoryMethod = null;
            if (min instanceof Integer) {
                filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName, int.class, int.class);
            } else if (min instanceof Float) {
                filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName, float.class, float.class);
            }
            filter = (Predicate<T>) filterPredicateFactoryMethod.invoke(filterPredicateFactory, min, max);
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
     * @param list an array of allowed values for the field to pass the filter
     * @param <T>
     * @return true if filter is passed or indeterminate; false if filter blocks.
     */
    private static <T, R> Predicate<T> createFilterPredicateForList(Class<? extends FilterPredicateFactory> filterPredicateFactoryClass, FilterPredicateFactory filterPredicateFactory, String fieldName, R[] list) {
        Predicate<T> filter = null;
        try {
            String filterPredicateFactoryMethodName = mapFieldTokenToFilterPredicateFactoryMethodName(fieldName);
            Method filterPredicateFactoryMethod = filterPredicateFactoryClass.getDeclaredMethod(filterPredicateFactoryMethodName, list.getClass());
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
            filter = (Predicate<T>) filterPredicateFactoryMethod.invoke(filterPredicateFactory, (Object) list);
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
     * Map grammar field tokens to names of filter predicates
     *
     * @param fieldToken
     * @return name of filter predicate.
     */
    private static String mapFieldTokenToFilterPredicateFactoryMethodName(String fieldToken) {
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
            case "m.country":
                return "filterOnMessageCountry";                
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
            case "m.pos":
                return "filterOnMessagePositionWithin";
            case "t.imo":
                return "filterOnTargetImo";
            case "t.type":
                return "filterOnTargetShiptype";
            case "t.navstat":
                return "filterOnTargetNavigationalStatus";
            case "t.name":
                return "filterOnTargetName";
            case "t.cs":
                return "filterOnTargetCallsign";
            case "t.sog":
                return "filterOnTargetSpeedOverGround";
            case "t.cog":
                return "filterOnTargetCourseOverGround";
            case "t.hdg":
                return "filterOnTargetTrueHeading";
            case "t.draught":
                return "filterOnTargetDraught";
            case "t.lon":
                return "filterOnTargetLongitude";
            case "t.lat":
                return "filterOnTargetLatitude";
            case "t.pos":
                return "filterOnTargetPositionWithin";
            case "t.country":
                return "filterOnTargetCountry";
            default:
                throw new IllegalArgumentException("No mapping to predicate name for field " + fieldToken);
        }
    }

    /**
     * Extract an array of Integers from an ANTLR list of TerminalNodes
     *
     * @param ints
     * @return
     */
    private static Integer[] extractIntegers(List<TerminalNode> ints) {
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
    private static String[] extractStrings(List strs) {
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
    private static String extractString(ExpressionFilterParser.StringContext string) {
        String result = null;
        if (string.STRING() != null) {
            result = removeSurroundingApostrophes(string.STRING().getText());
        } else if (string.number() != null) {
            result = string.number().getText();
        }
        return result;
    }

    /**
     * Check if context has LIKE() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasLIKE(ParserRuleContext ctx) {
        return hasMethod(ctx, "LIKE") && invokeMethod(ctx, "LIKE") != null;
    }

    /**
     * Check if context has INT() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasINT(ParserRuleContext ctx) {
        return hasMethod(ctx, "INT") && invokeMethod(ctx, "INT") != null;
    }

    /**
     * Check if context has String() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasString(ParserRuleContext ctx) {
        return hasMethod(ctx, "string") && invokeMethod(ctx, "string") != null;
    }

    /**
     * Check if context has number() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasNumber(ParserRuleContext ctx) {
        return hasMethod(ctx, "number") && invokeMethod(ctx, "number") != null;
    }

    /**
     * Check if context has compareTo() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasCompareTo(ParserRuleContext ctx) {
        return hasMethod(ctx, "compareTo") && invokeMethod(ctx, "compareTo") != null;
    }

    /**
     * Check if context has intList() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasIntList(ParserRuleContext ctx) {
        return hasMethod(ctx, "intList") && invokeMethod(ctx, "intList") != null;
    }

    /**
     * Check if context has stringList() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasStringList(ParserRuleContext ctx) {
        return hasMethod(ctx, "stringList") && invokeMethod(ctx, "stringList") != null;
    }

    /**
     * Check if context has intRange() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasIntRange(ParserRuleContext ctx) {
        return hasMethod(ctx, "intRange") && invokeMethod(ctx, "intRange") != null;
    }

    /**
     * Check if context has numberRange() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasNumberRange(ParserRuleContext ctx) {
        return hasMethod(ctx, "numberRange") && invokeMethod(ctx, "numberRange") != null;
    }

    /**
     * Check if context has bbox() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasBbox(ParserRuleContext ctx) {
        return hasMethod(ctx, "bbox") && invokeMethod(ctx, "bbox") != null;
    }

    /**
     * Check if context has bbox() method and it returns a value different from null.
     *
     * @param ctx
     * @return
     */
    private static boolean hasCircle(ParserRuleContext ctx) {
        return hasMethod(ctx, "circle") && invokeMethod(ctx, "circle") != null;
    }

    /**
     * Call the numberRange method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static Object invokeMethod(ParserRuleContext ctx, String methodName) {
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
    private static boolean hasMethod(ParserRuleContext ctx, String methodName) {
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
    private static ExpressionFilterParser.CompareToContext invokeCompareTo(ParserRuleContext ctx) {
        return (ExpressionFilterParser.CompareToContext) invokeMethod(ctx, "compareTo");
    }

    /**
     * Call the string() method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.StringContext invokeString(ParserRuleContext ctx) {
        return (ExpressionFilterParser.StringContext) invokeMethod(ctx, "string");
    }

    /**
     * Call the INT() method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static TerminalNode invokeINT(ParserRuleContext ctx) {
        return (TerminalNode) invokeMethod(ctx, "INT");
    }

    /**
     * Call the number() method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.NumberContext invokeNumber(ParserRuleContext ctx) {
        return (ExpressionFilterParser.NumberContext) invokeMethod(ctx, "number");
    }

    /**
     * Call the intList method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.IntListContext invokeIntList(ParserRuleContext ctx) {
        return (ExpressionFilterParser.IntListContext) invokeMethod(ctx, "intList");
    }

    /**
     * Call the stringList method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.StringListContext invokeStringList(ParserRuleContext ctx) {
        return (ExpressionFilterParser.StringListContext) invokeMethod(ctx, "stringList");
    }

    /**
     * Call the intRange method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.IntRangeContext invokeIntRange(ParserRuleContext ctx) {
        return (ExpressionFilterParser.IntRangeContext) invokeMethod(ctx, "intRange");
    }

    /**
     * Call the numberRange method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.NumberRangeContext invokeNumberRange(ParserRuleContext ctx) {
        return (ExpressionFilterParser.NumberRangeContext) invokeMethod(ctx, "numberRange");
    }

    /**
     * Call the bbox() method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.BboxContext invokeBbox(ParserRuleContext ctx) {
        return (ExpressionFilterParser.BboxContext) invokeMethod(ctx, "bbox");
    }

    /**
     * Call the circle() method on the ctx (if it exists) and return the result.
     *
     * @param ctx
     * @return
     */
    private static ExpressionFilterParser.CircleContext invokeCircle(ParserRuleContext ctx) {
        return (ExpressionFilterParser.CircleContext) invokeMethod(ctx, "circle");
    }

    /**
     * Check if the notin token exists in the context; and if so, negate the filter.
     *
     * @param ctx
     * @param filter
     * @return
     */
    private static Predicate checkNegate(ParserRuleContext ctx, Predicate filter) {
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
        }
        return filter;
    }

    /**
     * Remove the leading and trailing apostrophe from the string.
     *
     * @param string
     * @return
     */
    private static String removeSurroundingApostrophes(String string) {
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
    private static Set<Integer> getShipTypes(String[] labels) {
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
    private static ImmutableSet<Integer> getShipTypes(String label) {
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
    private static ImmutableSet<Integer> getNavigationalStatuses(String[] labels) {
        HashSet<Integer> navstats = new HashSet<>();
        for (String label : labels) {
            try {
                NavigationalStatus navigationalStatus = NavigationalStatus.valueOf(label.toUpperCase());
                navstats.add(navigationalStatus.getCode());
            } catch (IllegalArgumentException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }
        return ImmutableSet.copyOf(navstats);
    }

    /**
     * Convert an array of string representing sources types into the corresponding typed values.
     * @param labels
     * @return
     */
    private static SourceType[] getSourceTypes(String[] labels) {
        ArrayList<SourceType> r = new ArrayList<>();
        for (String label : labels) {
            r.add(SourceType.fromString(label));
        }
        return r.toArray(new SourceType[r.size()]);
    }

    /**
     * Convert an array of string representing countries into the corresponding typed values.
     * @param countryCodes
     * @return
     */
    private static Country[] getCountries(String[] countryCodes) {
        List<Country> countries = Country.findAllByCode(countryCodes);
        return countries.toArray(new Country[countries.size()]);
    }

    private static ImmutableSet<Integer> mapStringsToCalendarDaysOfWeek(String[] strings) {
        HashSet<Integer> set = new HashSet<>();
        for (String string : strings) {
            set.add(mapStringToCalendarDayOfWeek(string));
        }
        return ImmutableSet.copyOf(set);
    }

    private static ImmutableSet<Integer> mapStringsToCalendarMonths(String[] strings) {
        HashSet<Integer> set = new HashSet<>();
        for (String string : strings) {
            set.add(mapStringToCalendarMonth(string));
        }
        return ImmutableSet.copyOf(set);
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
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            throw new IllegalArgumentException(msg + " @ character " + charPositionInLine + (offendingSymbol != null ? " " + offendingSymbol : ""));
        }
    }
}
