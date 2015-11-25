package sibo.liu.jpm.supersimplestockmarket.transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import sibo.liu.jpm.supersimplestockmarket.stock.StockImpl;

/**
 * A thread-safe singleton class that manages stock and transaction through a
 * {@code static final ConcurrentHashMap}. Limited functionality as of right
 * now. All operation is thread safe.
 */
public enum StockTransactionManager {
    INSTANCE();

    // Make sure only one copy of MANAGER will ever exist in the environment
    private static final ConcurrentMap<String, StockImpl> MANAGER = new ConcurrentHashMap<String, StockImpl>();
    private static final int BIG_DECIMAL_SCALE = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Add a stock if {@code stock} is not null and dose not exist in the
     * storage.
     * 
     * @param stock
     *            the stock to be added
     * @return Return {@code true} if successfully added the stock into the map.
     *         {@code False} otherwise
     */
    public final boolean addStock(StockImpl stock) {
        if (stock != null && MANAGER.putIfAbsent(stock.getSymbol(), stock) == null) {
            return true;
        }
        return false;
    }

    /**
     * Remove a single stock based on stock symbol.
     * 
     * @param symbol
     *            symbol of the stock to be removed
     * @return Return {@code true} if the operation is successful, {@code false}
     *         otherwise
     */
    public final boolean removeStock(String symbol) {
        if (symbol != null && MANAGER.remove(symbol) != null) {
            return true;
        }
        return false;
    }

    /**
     * Retrieve a single stock based on stock symbol.
     * 
     * @param symbol
     *            symbol of the stock to be retrieved
     * @return Return {@code Optional<StockImpl>} if {@code symbol} is not null
     *         and the stock exists in the storage, return
     *         {@code Optional.empty()} otherwise
     */
    public final Optional<StockImpl> getStock(String symbol) {
        if (symbol != null) {
            StockImpl stockImpl = MANAGER.get(symbol);
            if (stockImpl != null) {
                return Optional.of(stockImpl);
            }
        }
        return Optional.empty();

    }

    /**
     * Add a single transaction to the stock represented by {@code symbol}
     * 
     * @param symbol
     *            symbol of a stock
     * @param transaction
     *            transaction to be added
     * @return Return {@code true} if {@code symbol} and {@code transaction} are
     *         not null and the transaction is successfully added to the stock.
     *         Return {@code false} otherwise
     */
    public final boolean addTransaction(String symbol, Transaction transaction) {
        if (symbol != null && transaction != null && MANAGER.containsKey(symbol)) {
            synchronized (MANAGER) {
                StockImpl stockImpl = MANAGER.get(symbol);
                if (stockImpl != null) {
                    stockImpl.addTransaction(transaction);
                    if (MANAGER.replace(symbol, stockImpl) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Set the last annual dividend of the stock specified by {@code symbol}
     * 
     * @param symbol
     *            symbol of a stock
     * @param lastAnnualDividend
     *            last annual dividend that will be put in the stock
     * @return Return {@code true} if {@code symbol} and
     *         {@code lastAnnualDividend} are not null and the operation is
     *         successful, return {@code false} otherwise
     */
    public final boolean setStockLastAnnualDividend(String symbol, BigDecimal lastAnnualDividend) {
        if (symbol != null && lastAnnualDividend != null && MANAGER.containsKey(symbol)) {
            synchronized (MANAGER) {
                StockImpl stockImpl = MANAGER.get(symbol);
                if (stockImpl != null) {
                    stockImpl.setLastAnnualDividend(lastAnnualDividend);
                    if (MANAGER.replace(symbol, stockImpl) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Set the divided period per year of the stock specified by {@code symbol}
     * 
     * @param symbol
     *            symbol of a stock
     * @param periodPerYear
     *            divided period per year that will be put in the stock
     * @return Return {@code true} if {@code symbol} and {@code periodPerYear}
     *         are not null and the operation is successful, return
     *         {@code false} otherwise
     */
    public final boolean setStockDividendPeriodPerYear(String symbol, BigInteger periodPerYear) {
        if (symbol != null && periodPerYear != null && MANAGER.containsKey(symbol)) {
            synchronized (MANAGER) {
                StockImpl stockImpl = MANAGER.get(symbol);
                if (stockImpl != null) {
                    stockImpl.setPeriodPerYear(periodPerYear);
                    if (MANAGER.replace(symbol, stockImpl) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Set the divided amount per period of the stock specified by
     * {@code symbol}
     * 
     * @param symbol
     *            symbol of a stock
     * @param dividendPerPeriod
     *            divided amount per period that will be put in the stock
     * @return Return {@code true} if {@code symbol} and
     *         {@code dividendPerPeriod} are not null and the operation is
     *         successful, return {@code false} otherwise
     */
    public final boolean setDividend(String symbol, BigDecimal dividendPerPeriod) {
        if (symbol != null && dividendPerPeriod != null && MANAGER.containsKey(symbol)) {
            synchronized (MANAGER) {
                StockImpl stockImpl = MANAGER.get(symbol);
                if (stockImpl != null) {
                    stockImpl.setDividend(dividendPerPeriod);
                    if (MANAGER.replace(symbol, stockImpl) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Get the GBCE All Shared Index with geometric mean in the whole storage.
     * 
     * @return Return {@code Optional<BigDecimal>} if there exists at least one
     *         valid transaction in the whole storage. Return
     *         {@codeOptional.empty()} otherwise
     */
    public final Optional<BigDecimal> getGBCEAllShareIndex() {
        BigDecimal numberOfTransactions = BigDecimal.ZERO;
        BigDecimal priceMultiplicationTotal = BigDecimal.ONE;
        for (StockImpl stockImpl : MANAGER.values()) {
            if (stockImpl != null) {
                for (Transaction transaction : stockImpl.getTransactions()) {
                    if (transaction != null && transaction.getPrice().signum() == 1) {
                        priceMultiplicationTotal = priceMultiplicationTotal.multiply(transaction
                                .getPrice());
                        numberOfTransactions = numberOfTransactions.add(BigDecimal.ONE);
                    }
                }
            }
        }
        if (numberOfTransactions.signum() == 0 || priceMultiplicationTotal.signum() != 1) {
            return Optional.empty();
        }
        return Optional.of(nthRoot(numberOfTransactions, priceMultiplicationTotal,
                BigDecimal.ONE.divide(BigDecimal.TEN.pow(BIG_DECIMAL_SCALE))));

    }

    /**
     * Clear all entry in the storage.
     * 
     * @return Return {@code true} if the map is cleared up. Return
     *         {@code false} otherwise
     */
    public final boolean clearAll() {
        MANAGER.clear();
        return MANAGER.isEmpty();
    }

    // Internal function used to calculate the nth root of a BigDecimal number
    // with specified precision
    private final BigDecimal nthRoot(BigDecimal n, BigDecimal base, BigDecimal precision) {
        BigDecimal prev = base;
        BigDecimal curr = base.divide(n, BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();

        while (curr.subtract(prev).abs().compareTo(precision) > 0) {
            prev = curr;
            BigDecimal nMinusOne = n.subtract(BigDecimal.ONE);
            curr = nMinusOne
                    .multiply(curr)
                    .add(base.divide(curr.pow(nMinusOne.intValueExact()), BIG_DECIMAL_SCALE,
                            ROUNDING_MODE)).divide(n, BIG_DECIMAL_SCALE, ROUNDING_MODE)
                    .stripTrailingZeros();
        }
        return curr.stripTrailingZeros();
    }
}