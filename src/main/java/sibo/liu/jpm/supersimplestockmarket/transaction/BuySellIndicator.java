package sibo.liu.jpm.supersimplestockmarket.transaction;

import java.util.Optional;

/**
 * The indicator used to indicate if a transaction type is buy or sell.
 * 
 * @author sibliu
 *
 */
public enum BuySellIndicator {
    BUY("B"), SELL("S");
    private String indicator;

    private BuySellIndicator(String indicator) {
        this.indicator = indicator;
    }

    /**
     * Get the {@code String} symbol of the indicator. "B" for buy and "S" for
     * sell
     * 
     * @return String symbol of this {@code BuySellIndicator} object
     */
    public String getIndicatorString() {
        return this.indicator;
    }

    /**
     * Parse the input {@code indicator} and returns a {@code BuySellIndicator}
     * object wrapped in {@code Optional} if {@code indicator} is "B" or "S".
     * Return {@code Optional.empty()} otherwise
     * 
     * @param indicator
     *            the symbol to be parsed.
     * @return Return {@code Optional<BuySellIndicator.BUY>} if
     *         {@code indicator} is "B", {@code Optional<BuySellIndicator.SELL>}
     *         if {@code indicator} is "S". Return {@code Optional.empty()}
     *         otherwise
     */
    public static final Optional<BuySellIndicator> getBuySellIndicator(String indicator) {
        if ("S".equals(indicator)) {
            return Optional.of(BuySellIndicator.SELL);
        } else if ("B".equals(indicator)) {
            return Optional.of(BuySellIndicator.BUY);
        } else {
            return Optional.empty();
        }
    }
}