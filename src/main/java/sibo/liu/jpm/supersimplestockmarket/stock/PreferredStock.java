package sibo.liu.jpm.supersimplestockmarket.stock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class PreferredStock extends StockImpl {
    private BigDecimal fixedDividendPerPeriod;

    private static final String FIXED_DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE = "The fixed dividend of a stock can only be larger than zero";

    /**
     * Construct a PreferredStock object.
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
     * @param fixedDividendPerPeriod
     *            percentage of fixed dividend presented in fraction, need to be
     *            positive
     * @throws IllegalArgumentException
     *             if any input is null or does not conform to the requirement
     *             of each input
     */
    public PreferredStock(String symbol, BigDecimal parValue, BigDecimal lastAnnualDividend,
            BigInteger periodPerYear, BigDecimal fixedDividendPerPeriod) {
        super(symbol, parValue, lastAnnualDividend, periodPerYear);
        if (fixedDividendPerPeriod != null) {
            if (fixedDividendPerPeriod.signum() != 1) {
                throw new IllegalArgumentException(
                        FIXED_DIVIDEND_PER_PERIOD_NEGATIVE_EXCEPTION_MESSAGE);
            } else {
                setDividend(fixedDividendPerPeriod);
            }
        } else {
            throw new IllegalArgumentException(NULL_INPUT_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public BigDecimal getDividend() {
        return fixedDividendPerPeriod;
    }

    /**
     * @param fixedDividendPerPeriod
     *            fixed dividend percentage in fraction
     */
    @Override
    public void setDividend(BigDecimal fixedDividendPerPeriod) {
        if (fixedDividendPerPeriod != null) {
            if (fixedDividendPerPeriod.scale() > BIG_DECIMAL_SCALE) {
                this.fixedDividendPerPeriod = fixedDividendPerPeriod.setScale(BIG_DECIMAL_SCALE,
                        ROUNDING_MODE);
            } else {
                this.fixedDividendPerPeriod = fixedDividendPerPeriod;
            }
        }
    }

    @Override
    public Optional<BigDecimal> getDividendYield(BigDecimal price) {
        if (price != null) {
            BigDecimal lastAnnualDividend = fixedDividendPerPeriod.multiply(getParValue())
                    .multiply(new BigDecimal(getPeriodPerYear()));
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
            BigDecimal currentDividend = fixedDividendPerPeriod.multiply(getParValue()).multiply(
                    new BigDecimal(getPeriodPerYear()));
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