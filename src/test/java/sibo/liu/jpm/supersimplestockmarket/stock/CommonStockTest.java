package sibo.liu.jpm.supersimplestockmarket.stock;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import sibo.liu.jpm.supersimplestockmarket.stock.interfaces.Stock;
import sibo.liu.jpm.supersimplestockmarket.stock.CommonStock;

public class CommonStockTest extends CommonStockTestHelper {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Stock stock;

    private static final String DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE = "The current dividend of a stock can only be larger than or equal to zero";

    @Test
    public void testConstructorWithNullSymbol() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new CommonStock(null, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullParValue() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, null,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullLastAnnualDividend() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                null, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullPeriodPerYear() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, null, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNullDividendPerPeriod() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, null);
    }

    @Test
    public void testConstructorWithLongerThanFourSymbolLength() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(INVALID_SYMBOL_EXCEPTION_MESSAGE);
        stock = new CommonStock(LONGER_THAN_FOUR_CHARACTER_SYMBOL_NAME,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithZeroSymbolLength() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(INVALID_SYMBOL_EXCEPTION_MESSAGE);
        stock = new CommonStock(EMPTY_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNonAlphabeticSymbol() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(INVALID_SYMBOL_EXCEPTION_MESSAGE);
        stock = new CommonStock(NON_ALPHABETIC_SYMBOL_NAME, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativeParValue() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NEGATIVE_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, NEGATIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativePeriodPerYear() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(PERIOD_PER_YEAR_NEGATIVE_OR_ZERO_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, NEGATIVE_BIG_INTEGER,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativeLastAnnualDividend() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NEGATIVE_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                NEGATIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testConstructorWithNegativeDividendPerPeriod() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE);
        stock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, NEGATIVE_NON_INTEGER_BIG_DECIMAL);
    }

    @Test
    public void testGetterAndSetter() {
        CommonStock commonStock = new CommonStock(FOUR_CHARACTER_ALPHABETIC_SYMBOL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.TEN,
                POSITIVE_NON_INTEGER_BIG_DECIMAL);
        commonStock.setDividend(POSITIVE_NON_INTEGER_BIG_DECIMAL);
        commonStock.setPeriodPerYear(BigInteger.ONE);

        assertEquals(POSITIVE_NON_INTEGER_BIG_DECIMAL, commonStock.getDividend());
        assertEquals(BigInteger.ONE, commonStock.getPeriodPerYear());

        commonStock.setDividend(null);
        commonStock.setPeriodPerYear(null);

        assertEquals(POSITIVE_NON_INTEGER_BIG_DECIMAL, commonStock.getDividend());
        assertEquals(BigInteger.ONE, commonStock.getPeriodPerYear());

        commonStock.setDividend(POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION);

        assertEquals(
                0,
                POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION.setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE).compareTo(commonStock.getDividend()));
    }

    @Test
    public void testGetDividendYield() {
        stock = new CommonStock(ONE_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.ONE, POSITIVE_NON_INTEGER_BIG_DECIMAL);
        BigDecimal yield = stock.getDividendYield(STOCK_PRICE).get();
        BigDecimal expectedYield = POSITIVE_NON_INTEGER_BIG_DECIMAL.divide(STOCK_PRICE,
                BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();
        assertEquals(0, yield.compareTo(expectedYield));

        assertFalse(stock.getDividendYield(null).isPresent());
    }

    @Test
    public void testGetPERatio() {
        stock = new CommonStock(ONE_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_NON_INTEGER_BIG_DECIMAL,
                POSITIVE_NON_INTEGER_BIG_DECIMAL, BigInteger.TEN, POSITIVE_NON_INTEGER_BIG_DECIMAL);
        BigDecimal peRatio = stock.getPERatio(STOCK_PRICE).get();
        BigDecimal expectedRatio = STOCK_PRICE.divide(
                POSITIVE_NON_INTEGER_BIG_DECIMAL.multiply(BigDecimal.TEN), BIG_DECIMAL_SCALE,
                ROUNDING_MODE).stripTrailingZeros();
        assertEquals(0, peRatio.compareTo(expectedRatio));

        assertFalse(stock.getPERatio(null).isPresent());
    }
}