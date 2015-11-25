package sibo.liu.jpm.supersimplestockmarket.stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import sibo.liu.jpm.supersimplestockmarket.stock.CommonStock;
import sibo.liu.jpm.supersimplestockmarket.stock.StockImpl;
import sibo.liu.jpm.supersimplestockmarket.transaction.Transaction;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockImplTest extends CommonStockTestHelper {
    private StockImpl stockImpl;

    private static final List<Transaction> EMPTY_TRANSACTION_LIST = new ArrayList<Transaction>();
    private static final Transaction TRANSACTION_MOCK = mock(Transaction.class);

    @Before
    public void setUp() {
        stockImpl = new CommonStock(ONE_CHARACTER_ALPHABETIC_SYMBOL, POSITIVE_FRACTION_BIG_DECIMAL,
                POSITIVE_FRACTION_BIG_DECIMAL, BigInteger.ONE, POSITIVE_FRACTION_BIG_DECIMAL);
    }

    @Test
    public void testGetterAndSetter() {
        stockImpl.setSymbol(FOUR_CHARACTER_ALPHABETIC_SYMBOL);
        stockImpl.setParValue(POSITIVE_NON_INTEGER_BIG_DECIMAL);
        stockImpl.setLastAnnualDividend(POSITIVE_NON_INTEGER_BIG_DECIMAL);

        assertEquals(FOUR_CHARACTER_ALPHABETIC_SYMBOL, stockImpl.getSymbol());
        assertEquals(POSITIVE_NON_INTEGER_BIG_DECIMAL, stockImpl.getParValue());
        assertEquals(POSITIVE_NON_INTEGER_BIG_DECIMAL, stockImpl.getLastAnnualDividend());
        assertEquals(EMPTY_TRANSACTION_LIST, stockImpl.getTransactions());

        stockImpl.setParValue(POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION);
        stockImpl.setLastAnnualDividend(POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION);

        assertEquals(
                0,
                POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION.setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE).compareTo(stockImpl.getLastAnnualDividend()));
        assertEquals(
                0,
                POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION.setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE).compareTo(stockImpl.getParValue()));
    }

    @Test
    public void testAddTransaction() {
        stockImpl.addTransaction(TRANSACTION_MOCK);

        assertEquals(1, stockImpl.getTransactions().size());
        for (Transaction transaction : stockImpl.getTransactions()) {
            assertEquals(TRANSACTION_MOCK, transaction);
        }
    }

    @Test
    public void testGetVolumeWeightStockPrice() {
        Transaction localTransactionMockOne = mock(Transaction.class);
        Transaction localTransactionMockTwo = mock(Transaction.class);
        Transaction localTransactionMockThree = mock(Transaction.class);
        DateTime current = DateTime.now();
        BigDecimal expectedResult = new BigDecimal(41.96861852).setScale(BIG_DECIMAL_SCALE,
                ROUNDING_MODE);

        when(TRANSACTION_MOCK.getTimeStamp()).thenReturn(current.minusMinutes(16));
        when(TRANSACTION_MOCK.getQuantity()).thenReturn(BigInteger.valueOf(100));
        when(TRANSACTION_MOCK.getPrice()).thenReturn(new BigDecimal(3.123));
        when(localTransactionMockOne.getTimeStamp()).thenReturn(current.minusMinutes(1));
        when(localTransactionMockOne.getQuantity()).thenReturn(BigInteger.valueOf(50));
        when(localTransactionMockOne.getPrice()).thenReturn(new BigDecimal(100.3286));
        when(localTransactionMockTwo.getTimeStamp()).thenReturn(current.minusMinutes(5));
        when(localTransactionMockTwo.getQuantity()).thenReturn(BigInteger.valueOf(350));
        when(localTransactionMockTwo.getPrice()).thenReturn(new BigDecimal(34.231));
        when(localTransactionMockThree.getTimeStamp()).thenReturn(current.minusMinutes(10));
        when(localTransactionMockThree.getQuantity()).thenReturn(BigInteger.valueOf(5));
        when(localTransactionMockThree.getPrice()).thenReturn(new BigDecimal(0.0021));

        assertFalse(stockImpl.getVolumeWeightedStockPrice(15).isPresent());

        stockImpl.addTransaction(TRANSACTION_MOCK);
        stockImpl.addTransaction(localTransactionMockOne);
        stockImpl.addTransaction(localTransactionMockTwo);
        stockImpl.addTransaction(localTransactionMockThree);

        assertTrue(stockImpl.getVolumeWeightedStockPrice(15).isPresent());
        assertEquals(expectedResult, stockImpl.getVolumeWeightedStockPrice(15).get());
    }
}