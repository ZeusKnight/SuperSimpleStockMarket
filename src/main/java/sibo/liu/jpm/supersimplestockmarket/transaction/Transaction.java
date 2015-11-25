package sibo.liu.jpm.supersimplestockmarket.transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.joda.time.DateTime;

/**
 * The transaction object that can be included in {@code Stock} object to record
 * a trade on that stock. Once initialized, no value can be changed.
 * 
 * @author sibliu
 *
 */
public class Transaction {
    private final String symbol;
    private final DateTime timeStamp;
    private final BigInteger quantity;
    private final BuySellIndicator indicator;
    private final BigDecimal price;

    private static final String NULL_INPUT_EXCEPTION_MESSAGE = "No input can be null";
    private static final String NON_POSITIVE_EXCEPTION_MESSAGE = "Transaction quantity and price can only be larger than zero";
    private static final int BIG_DECIMAL_SCALE = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Construct a {@code Transaction} object.
     * <p>
     * <b>Note:</b>The scale of {@code price} will be converted to 8 decimal
     * point round half up if fraction part is too large
     * 
     * @param symbol
     *            the symbol of the stock. Can not be null
     * @param timeStamp
     *            the transaction time. Can not be null
     * @param quantity
     *            the quantity of this transaction. Can not be null and can only
     *            be positive
     * @param indicator
     *            the buy/sell indicator. Can not be null
     * @param price
     *            the price of this transaction. Can not be null and can only be
     *            positive
     * @throws IllegalArgumentException
     *             if any input is null or does not conform to the requirement
     *             of each input
     */
    public Transaction(String symbol, DateTime timeStamp, BigInteger quantity,
            BuySellIndicator indicator, BigDecimal price) {
        if (symbol != null && timeStamp != null && quantity != null && indicator != null
                && price != null) {
            if (quantity.signum() != 1 || price.signum() != 1) {
                throw new IllegalArgumentException(NON_POSITIVE_EXCEPTION_MESSAGE);
            }
            this.symbol = symbol;
            this.timeStamp = timeStamp;
            this.quantity = quantity;
            this.indicator = indicator;
            this.price = price.setScale(BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();
        } else {
            throw new IllegalArgumentException(NULL_INPUT_EXCEPTION_MESSAGE);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public BuySellIndicator getIndicator() {
        return indicator;
    }

    public BigDecimal getPrice() {
        return price;
    }
}