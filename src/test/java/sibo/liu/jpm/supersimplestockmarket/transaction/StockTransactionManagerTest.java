package sibo.liu.jpm.supersimplestockmarket.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

import sibo.liu.jpm.supersimplestockmarket.stock.CommonStock;
import sibo.liu.jpm.supersimplestockmarket.stock.PreferredStock;
import sibo.liu.jpm.supersimplestockmarket.stock.StockImpl;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doCallRealMethod;

public class StockTransactionManagerTest {
    private static final int BIG_DECIMAL_SCALE = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private StockTransactionManager stockManager = StockTransactionManager.INSTANCE;
    private static final StockImpl COMMON_STOCK_MOCK = mock(CommonStock.class);
    private static final StockImpl PREFERRED_STOCK_MOCK = mock(PreferredStock.class);
    private static final Transaction TRANSACTION_MOCK = mock(Transaction.class);
    private static final BigDecimal BIG_DECIMAL_MOCK = mock(BigDecimal.class);
    private static final BigInteger BIG_INTEGER_MOCK = mock(BigInteger.class);
    private static final String COMMON_STOCK_SYMBOL = "JPM";
    private static final String PREFERRED_STOCK_SYMBOL = "MSFT";

    @Test
    public void testSingleton() {
        StockTransactionManager testStockManager = StockTransactionManager.INSTANCE;
        assertTrue(stockManager.equals(testStockManager));
    }

    @Test
    public void testAddGetRemoveStock() {
        assertTrue(stockManager.clearAll());
        when(COMMON_STOCK_MOCK.getSymbol()).thenReturn(COMMON_STOCK_SYMBOL);
        when(PREFERRED_STOCK_MOCK.getSymbol()).thenReturn(PREFERRED_STOCK_SYMBOL);

        assertTrue(stockManager.addStock(COMMON_STOCK_MOCK));
        assertFalse(stockManager.addStock(COMMON_STOCK_MOCK));
        assertTrue(stockManager.getStock(COMMON_STOCK_SYMBOL).isPresent());
        assertEquals(COMMON_STOCK_MOCK, stockManager.getStock(COMMON_STOCK_SYMBOL).get());

        assertTrue(stockManager.addStock(PREFERRED_STOCK_MOCK));
        assertFalse(stockManager.addStock(PREFERRED_STOCK_MOCK));
        assertTrue(stockManager.getStock(PREFERRED_STOCK_SYMBOL).isPresent());
        assertEquals(PREFERRED_STOCK_MOCK, stockManager.getStock(PREFERRED_STOCK_SYMBOL).get());

        assertTrue(stockManager.removeStock(COMMON_STOCK_SYMBOL));
        assertFalse(stockManager.getStock(COMMON_STOCK_SYMBOL).isPresent());
        assertFalse(stockManager.removeStock(COMMON_STOCK_SYMBOL));

        assertTrue(stockManager.removeStock(PREFERRED_STOCK_SYMBOL));
        assertFalse(stockManager.getStock(PREFERRED_STOCK_SYMBOL).isPresent());
        assertFalse(stockManager.removeStock(PREFERRED_STOCK_SYMBOL));

        assertFalse(stockManager.addStock(null));
        assertFalse(stockManager.removeStock(null));
        assertFalse(stockManager.getStock(null).isPresent());
    }

    @Test
    public void testAddTransaction() {
        assertTrue(stockManager.clearAll());
        when(BIG_DECIMAL_MOCK.signum()).thenReturn(1);
        when(BIG_INTEGER_MOCK.signum()).thenReturn(1);
        StockImpl stockImpl = new CommonStock(COMMON_STOCK_SYMBOL, BIG_DECIMAL_MOCK,
                BIG_DECIMAL_MOCK, BIG_INTEGER_MOCK, BIG_DECIMAL_MOCK);

        assertTrue(stockManager.addStock(stockImpl));
        assertTrue(stockManager.addTransaction(COMMON_STOCK_SYMBOL, TRANSACTION_MOCK));
        assertEquals(1, stockManager.getStock(COMMON_STOCK_SYMBOL).get().getTransactions().size());
        assertEquals(TRANSACTION_MOCK, stockManager.getStock(COMMON_STOCK_SYMBOL).get()
                .getTransactions().get(0));

        assertTrue(stockManager.removeStock(COMMON_STOCK_SYMBOL));
        assertFalse(stockManager.addTransaction(COMMON_STOCK_SYMBOL, TRANSACTION_MOCK));

        assertFalse(stockManager.addTransaction(COMMON_STOCK_SYMBOL, null));
        assertFalse(stockManager.addTransaction(null, TRANSACTION_MOCK));
        assertFalse(stockManager.addTransaction(null, null));
    }

    @Test
    public void testSetLastAnnualDividend() {
        assertTrue(stockManager.clearAll());
        when(COMMON_STOCK_MOCK.getSymbol()).thenReturn(COMMON_STOCK_SYMBOL);
        doCallRealMethod().when(COMMON_STOCK_MOCK).setLastAnnualDividend(BIG_DECIMAL_MOCK);
        doCallRealMethod().when(COMMON_STOCK_MOCK).getLastAnnualDividend();

        assertTrue(stockManager.addStock(COMMON_STOCK_MOCK));
        assertTrue(stockManager.setStockLastAnnualDividend(COMMON_STOCK_SYMBOL, BIG_DECIMAL_MOCK));
        assertEquals(BIG_DECIMAL_MOCK, stockManager.getStock(COMMON_STOCK_SYMBOL).get()
                .getLastAnnualDividend());

        assertFalse(stockManager.setStockLastAnnualDividend(null, BIG_DECIMAL_MOCK));
        assertFalse(stockManager.setStockLastAnnualDividend(COMMON_STOCK_SYMBOL, null));
        assertFalse(stockManager.setStockLastAnnualDividend(null, null));

        assertTrue(stockManager.removeStock(COMMON_STOCK_SYMBOL));
        assertFalse(stockManager.setStockLastAnnualDividend(COMMON_STOCK_SYMBOL, BIG_DECIMAL_MOCK));
    }

    @Test
    public void testSetDividendPeriodPerYear() {
        assertTrue(stockManager.clearAll());
        when(COMMON_STOCK_MOCK.getSymbol()).thenReturn(COMMON_STOCK_SYMBOL);
        doCallRealMethod().when(COMMON_STOCK_MOCK).setPeriodPerYear(BIG_INTEGER_MOCK);
        doCallRealMethod().when(COMMON_STOCK_MOCK).getPeriodPerYear();

        assertTrue(stockManager.addStock(COMMON_STOCK_MOCK));
        assertTrue(stockManager
                .setStockDividendPeriodPerYear(COMMON_STOCK_SYMBOL, BIG_INTEGER_MOCK));
        assertEquals(BIG_INTEGER_MOCK, stockManager.getStock(COMMON_STOCK_SYMBOL).get()
                .getPeriodPerYear());

        assertFalse(stockManager.setStockDividendPeriodPerYear(null, BIG_INTEGER_MOCK));
        assertFalse(stockManager.setStockDividendPeriodPerYear(COMMON_STOCK_SYMBOL, null));
        assertFalse(stockManager.setStockDividendPeriodPerYear(null, null));

        assertTrue(stockManager.removeStock(COMMON_STOCK_SYMBOL));
        assertFalse(stockManager.setStockDividendPeriodPerYear(COMMON_STOCK_SYMBOL,
                BIG_INTEGER_MOCK));
    }

    @Test
    public void testSetDividend() {
        assertTrue(stockManager.clearAll());
        when(COMMON_STOCK_MOCK.getSymbol()).thenReturn(COMMON_STOCK_SYMBOL);
        doCallRealMethod().when(COMMON_STOCK_MOCK).setDividend(BIG_DECIMAL_MOCK);
        doCallRealMethod().when(COMMON_STOCK_MOCK).getDividend();

        assertTrue(stockManager.addStock(COMMON_STOCK_MOCK));
        assertTrue(stockManager.setDividend(COMMON_STOCK_SYMBOL, BIG_DECIMAL_MOCK));
        assertEquals(BIG_DECIMAL_MOCK, stockManager.getStock(COMMON_STOCK_SYMBOL).get()
                .getDividend());

        assertFalse(stockManager.setDividend(null, BIG_DECIMAL_MOCK));
        assertFalse(stockManager.setDividend(COMMON_STOCK_SYMBOL, null));
        assertFalse(stockManager.setDividend(null, null));

        assertTrue(stockManager.removeStock(COMMON_STOCK_SYMBOL));
        assertFalse(stockManager.setDividend(COMMON_STOCK_SYMBOL, BIG_DECIMAL_MOCK));
    }

    @Test
    public void testGetGBCEAllShareIndex() {
        assertTrue(stockManager.clearAll());
        assertFalse(stockManager.getGBCEAllShareIndex().isPresent());

        Transaction localTransactionMockOne = mock(Transaction.class);
        Transaction localTransactionMockTwo = mock(Transaction.class);
        Transaction localTransactionMockThree = mock(Transaction.class);
        BigDecimal expectedResult = new BigDecimal(1.1990190463).setScale(BIG_DECIMAL_SCALE,
                ROUNDING_MODE);

        when(TRANSACTION_MOCK.getPrice()).thenReturn(new BigDecimal(3.12));
        when(localTransactionMockOne.getPrice()).thenReturn(new BigDecimal(5.7823));
        when(localTransactionMockTwo.getPrice()).thenReturn(new BigDecimal(200.123422));
        when(localTransactionMockThree.getPrice()).thenReturn(new BigDecimal(0.00022));
        when(BIG_DECIMAL_MOCK.signum()).thenReturn(1);
        when(BIG_INTEGER_MOCK.signum()).thenReturn(1);

        StockImpl commonImpl = new CommonStock(COMMON_STOCK_SYMBOL, BIG_DECIMAL_MOCK,
                BIG_DECIMAL_MOCK, BIG_INTEGER_MOCK, BIG_DECIMAL_MOCK);
        StockImpl preferredImpl = new PreferredStock(PREFERRED_STOCK_SYMBOL, BIG_DECIMAL_MOCK,
                BIG_DECIMAL_MOCK, BIG_INTEGER_MOCK, BIG_DECIMAL_MOCK);

        assertTrue(stockManager.addStock(commonImpl));
        assertTrue(stockManager.addTransaction(COMMON_STOCK_SYMBOL, TRANSACTION_MOCK));
        assertTrue(stockManager.addTransaction(COMMON_STOCK_SYMBOL, localTransactionMockTwo));
        assertTrue(stockManager.addTransaction(COMMON_STOCK_SYMBOL, TRANSACTION_MOCK));

        assertTrue(stockManager.addStock(preferredImpl));
        assertTrue(stockManager.addTransaction(PREFERRED_STOCK_SYMBOL, localTransactionMockOne));
        assertTrue(stockManager.addTransaction(PREFERRED_STOCK_SYMBOL, localTransactionMockThree));

        assertTrue(stockManager.getGBCEAllShareIndex().isPresent());
        assertEquals(0, expectedResult.compareTo(stockManager.getGBCEAllShareIndex().get()));
    }
}