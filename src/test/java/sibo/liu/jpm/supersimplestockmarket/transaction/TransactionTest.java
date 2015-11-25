package sibo.liu.jpm.supersimplestockmarket.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TransactionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Transaction transaction;

    private static final int BIG_DECIMAL_SCALE = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final String FOUR_CHARACTER_ALPHABETIC_SYMBOL = "MSFT";
    private static final DateTime CURRENT_TIMESTAMP = new DateTime(DateTimeZone.getDefault());
    private static final BigInteger QUANTITY = BigInteger.valueOf(100);
    private static final BuySellIndicator SELL_INDICATOR = BuySellIndicator.SELL;
    private static final BuySellIndicator BUY_INDICATOR = BuySellIndicator.BUY;
    private static final BigDecimal PRICE = new BigDecimal(23.6).setScale(BIG_DECIMAL_SCALE,
            ROUNDING_MODE).stripTrailingZeros();
    private static final BigDecimal TEN_FRACTION_DIGIT_PRICE = new BigDecimal(23.6273645342);

    private static final String NULL_INPUT_EXCEPTION_MESSAGE = "No input can be null";
    private static final String NON_POSITIVE_EXCEPTION_MESSAGE = "Transaction quantity and price can only be larger than zero";

    @Test
    public void testNullSymbolInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        transaction = new Transaction(null, CURRENT_TIMESTAMP, QUANTITY, SELL_INDICATOR, PRICE);
    }

    @Test
    public void testNullTimeStampInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, null, QUANTITY,
                SELL_INDICATOR, PRICE);
    }

    @Test
    public void testNullQuantityInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP, null,
                SELL_INDICATOR, PRICE);
    }

    @Test
    public void testNullIndicatorInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                QUANTITY, null, PRICE);
    }

    @Test
    public void testNullPriceInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NULL_INPUT_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                QUANTITY, SELL_INDICATOR, null);
    }

    @Test
    public void testNegativeQuantityInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NON_POSITIVE_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                QUANTITY.negate(), SELL_INDICATOR, PRICE);
    }

    @Test
    public void testZeroQuantityInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NON_POSITIVE_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                BigInteger.ZERO, SELL_INDICATOR, PRICE);
    }

    @Test
    public void testNegativePriceInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NON_POSITIVE_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                QUANTITY, SELL_INDICATOR, PRICE.negate());
    }

    @Test
    public void testZeroPriceInput() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(NON_POSITIVE_EXCEPTION_MESSAGE);
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                QUANTITY, SELL_INDICATOR, BigDecimal.ZERO);
    }

    @Test
    public void testGetterAndSetter() {
        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                QUANTITY, BUY_INDICATOR, PRICE);

        assertEquals(FOUR_CHARACTER_ALPHABETIC_SYMBOL, transaction.getSymbol());
        assertEquals(CURRENT_TIMESTAMP, transaction.getTimeStamp());
        assertEquals(QUANTITY, transaction.getQuantity());
        assertEquals(BUY_INDICATOR.getIndicatorString(), transaction.getIndicator()
                .getIndicatorString());
        assertEquals(PRICE, transaction.getPrice());
        assertNotEquals(SELL_INDICATOR.getIndicatorString(), transaction.getIndicator()
                .getIndicatorString());

        transaction = new Transaction(FOUR_CHARACTER_ALPHABETIC_SYMBOL, CURRENT_TIMESTAMP,
                QUANTITY, BUY_INDICATOR, TEN_FRACTION_DIGIT_PRICE);
        assertEquals(0, TEN_FRACTION_DIGIT_PRICE.setScale(BIG_DECIMAL_SCALE, ROUNDING_MODE)
                .compareTo(transaction.getPrice()));
    }
}