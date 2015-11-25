package sibo.liu.jpm.supersimplestockmarket.stock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class CommonStock extends StockImpl {
    private BigDecimal dividendPerPeriod;

    // Constant error message
    private static final String DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE = "The current dividend of a stock can only be larger than or equal to zero";

    /**
     * Construct a CommonStock object.
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
     * @param dividendPerPeriod
     *            amount of dividend paid out per period. Need to be zero or
     *            positive
     * @throws IllegalArgumentException
     *             if any input is null or does not conform to the requirement
     *             of each input
     */
    public CommonStock(String symbol, BigDecimal parValue, BigDecimal lastAnnualDividend,
            BigInteger periodPerYear, BigDecimal dividendPerPeriod) {
        super(symbol, parValue, lastAnnualDividend, periodPerYear);
        if (dividendPerPeriod != null) {
            if (dividendPerPeriod.signum() == -1) {
                throw new IllegalArgumentException(DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE);
            } else {
                setDividend(dividendPerPeriod);
            }
        } else {
            throw new IllegalArgumentException(NULL_INPUT_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public BigDecimal getDividend() {
        return dividendPerPeriod;
    }

    /**
     * @param dividendPerPeriod
     *            amount of dividend paid out per period
     */
    @Override
    public void setDividend(BigDecimal dividendPerPeriod) {
        if (dividendPerPeriod != null && dividendPerPeriod.signum() != -1) {
            if (dividendPerPeriod.scale() > BIG_DECIMAL_SCALE) {
                this.dividendPerPeriod = dividendPerPeriod.setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE);
            } else {
                this.dividendPerPeriod = dividendPerPeriod;
            }
        }
    }

    @Override
    public Optional<BigDecimal> getDividendYield(BigDecimal price) {
        if (price != null) {
            BigDecimal lastAnnualDividend = getLastAnnualDividend();
            if (price.signum() > 0) {
                BigDecimal yield = lastAnnualDividend.divide(price, BIG_DECIMAL_SCALE,
                        ROUNDING_MODE).stripTrailingZeros();
                return Optional.of(yield);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> getPERatio(BigDecimal price) {
        if (price != null) {
            BigDecimal currentDividend = dividendPerPeriod.multiply(new BigDecimal(
                    getPeriodPerYear()));
            if (currentDividend.signum() > 0) {
                BigDecimal peRatio = price
                        .divide(currentDividend, BIG_DECIMAL_SCALE, ROUNDING_MODE)
                        .stripTrailingZeros();
                return Optional.of(peRatio);
            }
        }
        return Optional.empty();
    }
}