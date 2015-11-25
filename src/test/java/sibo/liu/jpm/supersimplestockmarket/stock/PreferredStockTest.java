package sibo.liu.jpm.supersimplestockmarket.stock;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sibo.liu.jpm.supersimplestockmarket.stock.interfaces.Stock;
import sibo.liu.jpm.supersimplestockmarket.stock.PreferredStock;

public class PreferredStockTest extends CommonStockTestHelper {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Stock stock;

    private static final String FIXED_DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE = "The fixed dividend of a stock can only be larger than zero";

    @Test
    public void testConstructorWithNullSymbol() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new PreferredStock(null, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullParValue() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, null,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullLastAnnualDividend() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, null, BigInteger.ONE,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullPeriodPerYear() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, null,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullFixedDividendPerPeriod() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE,
                null);
    }

    @Test
    public void testConstructorWithLongerThanFourSymbolLength() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(INVALID_SYMBOL_EXCEPTION_MESSAGE);
        stock = new PreferredStock(LONGER_THAN_FOUR_CHARACTER_SYMBOL_NAME,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithZeroSymbolLength() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(INVALID_SYMBOL_EXCEPTION_MESSAGE);
        stock = new PreferredStock(EMPTY_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNonAlphabeticSymbol() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(INVALID_SYMBOL_EXCEPTION_MESSAGE);
        stock = new PreferredStock(NON_ALPHABETIC_SYMBOL_NAME, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativeParValue() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NEGATIVE_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                NEGATIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativePeriodPerYear() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(PERIOD_PER_YEAR_NEGATIVE_OR_ZERO_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                NEGATIVE_BIG_INTEGER, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativeLastAnnualDividend() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NEGATIVE_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, NEGATIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativeDividendPerPeriod() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(FIXED_DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE);
        stock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE,
                NEGATIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testGetterAndSetter() {
        PreferredStock preferredStock = new PreferredStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.TEN,
                POSITIVE_FRACTION_BIG_DECIMAL);
        preferredStock.setDividend(POSITIVE_FRACTION_BIG_DECIMAL);
        preferredStock.setPeriodPerYear(BigInteger.ONE);

        assertEquals(preferredStock.getDividend(), POSITIVE_FRACTION_BIG_DECIMAL);
        assertEquals(preferredStock.getPeriodPerYear(), BigInteger.ONE);

        preferredStock.setDividend(POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION);

        assertEquals(
                0,
                POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION.setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE).compareTo(preferredStock.getDividend()));
    }

    @Test
    public void testGetDividendYield() {
        stock = new PreferredStock(ONE_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE,
                POSITIVE_FRACTION_BIG_DECIMAL);
        BigDecimal yield = stock.getDividendYield(STOCK_PRICE).get();
        BigDecimal expectedYield = POSITIVE_FRACTION_BIG_DECIMAL
                .multiply(POSITIVE_NON_INTEGER_BIG_DECIMAL).multiply(BigDecimal.ONE)
                .divide(STOCK_PRICE, BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();
        assertEquals(yield.compareTo(expectedYield), 0);

        assertEquals(stock.getDividendYield(null).isPresent(), false);
    }

    @Test
    public void testGetPERatio() {
        stock = new PreferredStock(ONE_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.TEN,
                POSITIVE_FRACTION_BIG_DECIMAL);
        BigDecimal peRatio = stock.getPERatio(STOCK_PRICE).get();
        BigDecimal expectedRatio = STOCK_PRICE.divide(
                POSITIVE_FRACTION_BIG_DECIMAL.multiply(POSITIVE_NON_INTEGER_BIG_DECIMAL).multiply(
                        BigDecimal.TEN), BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();
        assertEquals(peRatio.compareTo(expectedRatio), 0);

        assertEquals(stock.getPERatio(null).isPresent(), false);
    }
}