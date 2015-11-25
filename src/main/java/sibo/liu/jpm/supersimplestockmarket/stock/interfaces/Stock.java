package sibo.liu.jpm.supersimplestockmarket.stock.interfaces;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The interface of stock that specifies the operation can be performed on a
 * {@code Stock} object as to client
 * 
 * @author sibliu
 *
 */
public interface Stock {
    /**
     * Get the dividend yield of this stock if {@code price} is not null and non
     * negative
     * 
     * @param price
     *            the price of the stock
     * @return return {@code Optional<BigDecimal>} if input is not null or
     *         non-negative, return {@code Optional.empty()} otherwise
     * 
     */
    public Optional<BigDecimal> getDividendYield(BigDecimal price);

    /**
     * Get the P/E ratio of this stock if {@code price} is not null and the
     * dividend of this stock is positive
     * 
     * @param price
     *            the price of the stock
     * @return return {@code Optional<BigDecimal>} if input is not null or
     *         dividend of this {@code Stock} object is positive, return
     *         {@code Optional.empty()} otherwise
     * 
     */
    public Optional<BigDecimal> getPERatio(BigDecimal price);

    /**
     * Return the volume weighted stock price in {@code Optional<BigDecimal>} in
     * the past {@code pastMinutes} minutes, if there exists at least one
     * transaction in this period. return {@code Optional.empty()} otherwise
     * 
     * @param pastMinutes
     *            past minutes to be used to include transactions
     * @return {@code Optional<BigDecimal>} if there is at least one transaction
     *         in the period specified, return {@code Optional.empty()}
     *         otherwise
     */
    public Optional<BigDecimal> getVolumeWeightedStockPrice(int pastMinutes);
}