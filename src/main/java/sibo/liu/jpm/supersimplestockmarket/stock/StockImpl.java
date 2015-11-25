package sibo.liu.jpm.supersimplestockmarket.stock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;

import sibo.liu.jpm.supersimplestockmarket.stock.interfaces.Stock;
import sibo.liu.jpm.supersimplestockmarket.transaction.Transaction;

/**
 * Abstract super class that specify the behavior and common features of a
 * stock. Implements {@code Stock} interface
 * 
 * @author sibliu
 *
 */
public abstract class StockImpl implements Stock {
    private static final String VALID_SYMBOL_REGULAR_EXPRESSION = "[a-zA-Z]+";
    private static final int MAX_SYMBOL_LENGTH = 4;
    protected static final int BIG_DECIMAL_SCALE = 8;
    protected static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    protected static final String NULL_INPUT_EXCEPTION_MESSAGE = "No input can be null";
    private static final String INVALID_SYMBOL_EXCEPTION_MESSAGE = "The symbol of a stock can have 1 to 4 characters and alphabetic characters only";
    private static final String NEGATIVE_EXCEPTION_MESSAGE = "The par value and last dividend of a stock can only be larger than or equal to zero";
    private static final String PERIOD_PER_YEAR_NEGATIVE_OR_ZERO_EXCEPTION_MESSAGE = "The dividend period of a stock can only be larger than zero";

    private String symbol;
    private BigDecimal parValue;
    private BigDecimal lastAnnualDividend;
    private BigInteger periodPerYear;
    private final List<Transaction> transactions;

    /**
     * Super constructor for StockImpl object
     * 
     * @param symbol
     *            the symbol of the stock. Need to be 1 to 4 characters long and
     *            can only contain alphabet
     * @param parValue
     *            par value of the stock. Need to be zero or positive
     * @param lastAnnualDividend
     *            last annual dividend of the stock. Set to 0 if none. Need to
     *            be zero or positive
     * @param periodPerYear
     *            period per year. Need to be larger than or equal to zero
     * @throws IllegalArgumentException
     *             if any input is null or does not conform to the requirement
     *             of each input
     */
    protected StockImpl(String symbol, BigDecimal parValue, BigDecimal lastAnnualDividend,
            BigInteger periodPerYear) {
        if (symbol != null && parValue != null && lastAnnualDividend != null
                && periodPerYear != null) {
            if (symbol.isEmpty() || symbol.length() > MAX_SYMBOL_LENGTH
                    || !symbol.matches(VALID_SYMBOL_REGULAR_EXPRESSION)) {
                throw new IllegalArgumentException(INVALID_SYMBOL_EXCEPTION_MESSAGE);
            }
            if (parValue.signum() == -1 || lastAnnualDividend.signum() == -1) {
                throw new IllegalArgumentException(NEGATIVE_EXCEPTION_MESSAGE);
            }
            if (periodPerYear.signum() != 1) {
                throw new IllegalArgumentException(
                        PERIOD_PER_YEAR_NEGATIVE_OR_ZERO_EXCEPTION_MESSAGE);
            }
            setSymbol(symbol);
            setParValue(parValue);
            setLastAnnualDividend(lastAnnualDividend);
            setPeriodPerYear(periodPerYear);
            this.transactions = new ArrayList<Transaction>();
        } else {
            throw new IllegalArgumentException(NULL_INPUT_EXCEPTION_MESSAGE);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Set symbol of the stock if input is not null, has a length between 1 to 4
     * and contains alphabets only. Do nothing otherwise
     * 
     * @param symbol
     */
    public void setSymbol(String symbol) {
        if (!symbol.isEmpty() && symbol.length() <= MAX_SYMBOL_LENGTH
                && symbol.matches(VALID_SYMBOL_REGULAR_EXPRESSION)) {
            this.symbol = symbol;
        }
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    /**
     * Set the par value of the stock if the input is not null and non-negative.
     * Do nothing otherwise.
     * <p>
     * <b>Note:</b>The scale will be converted to 8 decimal point round half up
     * if fraction part is too large.
     * 
     * @param parValue
     */
    public void setParValue(BigDecimal parValue) {
        if (parValue != null && parValue.signum() != -1) {
            if (parValue.scale() > BIG_DECIMAL_SCALE) {
                this.parValue = parValue.setScale(BIG_DECIMAL_SCALE, ROUNDING_MODE);
            } else {
                this.parValue = parValue;
            }
        }
    }

    public BigDecimal getLastAnnualDividend() {
        return lastAnnualDividend;
    }

    /**
     * Set the last annual dividend of the stock if the input is not null and
     * non-negative. Do nothing otherwise.
     * <p>
     * <b>Note:</b>The scale will be converted to 8 decimal point round half up
     * if fraction part is too large.
     * 
     * @param lastAnnualDividend
     */
    public void setLastAnnualDividend(BigDecimal lastAnnualDividend) {
        if (lastAnnualDividend != null && lastAnnualDividend.signum() != -1) {
            if (lastAnnualDividend.scale() > BIG_DECIMAL_SCALE) {
                this.lastAnnualDividend = lastAnnualDividend.setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE);
            } else {
                this.lastAnnualDividend = lastAnnualDividend;
            }
        }
    }

    public BigInteger getPeriodPerYear() {
        return periodPerYear;
    }

    /**
     * Set the period per year of the stock if the input is not null and
     * positive. Do nothing otherwise.
     * 
     * @param parValue
     */
    public void setPeriodPerYear(BigInteger periodPerYear) {
        if (periodPerYear != null && periodPerYear.signum() > 0) {
            this.periodPerYear = periodPerYear;
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Add a new transaction to this {@code Stock} object if not null. DO
     * nothing otherwise
     * 
     * @param transaction
     */
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    @Override
    public Optional<BigDecimal> getVolumeWeightedStockPrice(int pastMinutes) {
        DateTime current = DateTime.now();
        DateTime pastTime = current.minusMinutes(pastMinutes);
        BigDecimal totalPriceQuantity = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getTimeStamp().isBefore(current)
                    && transaction.getTimeStamp().isAfter(pastTime)) {
                BigDecimal quantity = new BigDecimal(transaction.getQuantity());
                totalPriceQuantity = totalPriceQuantity.add(
                        transaction.getPrice().multiply(quantity)).setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE);
                totalQuantity = totalQuantity.add(quantity);
            }
        }
        if (totalPriceQuantity.signum() != 0 && totalQuantity.signum() != 0) {
            return Optional.of(totalPriceQuantity.divide(totalQuantity, BIG_DECIMAL_SCALE,
                    ROUNDING_MODE));
        }
        return Optional.empty();
    }

    public abstract BigDecimal getDividend();

    /**
     * Set the dividend amount of one period if input is not null and
     * non-negative. Do nothing otherwise
     * <p>
     * <b>Note:</b>The scale will be converted to 8 decimal point round half up
     * if fraction part is too large.
     */
    public abstract void setDividend(BigDecimal dividend);
}