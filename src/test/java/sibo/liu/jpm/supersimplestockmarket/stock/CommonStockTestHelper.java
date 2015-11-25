package sibo.liu.jpm.supersimplestockmarket.stock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class CommonStockTestHelper {
    public static final int BIG_DECIMAL_SCALE = 8;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final String NON_ALPHABETIC_SYMBOL_NAME = "A%2.";
    public static final String LONGER_THAN_FOUR_CHARACTER_SYMBOL_NAME = "ABCSD";
    public static final String EMPTY_SYMBOL = "";
    public static final String ONE_CHARACTER_ALPHABETIC_SYMBOL = "V";
    public static final String FOUR_CHARACTER_ALPHABETIC_SYMBOL = "MSFT";
    public static final BigInteger NEGATIVE_BIG_INTEGER = BigInteger.valueOf(-2);
    public static final BigDecimal NEGATIVE_NON_INTEGER_BIG_DECIMAL = new BigDecimal(-2.46)
            .setScale(BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();
    public static final BigDecimal POSITIVE_NON_INTEGER_BIG_DECIMAL = new BigDecimal(300.45)
            .setScale(BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();
    public static final BigDecimal STOCK_PRICE = new BigDecimal(3.2).setScale(BIG_DECIMAL_SCALE,
            ROUNDING_MODE).stripTrailingZeros();
    public static final BigDecimal POSITIVE_FRACTION_BIG_DECIMAL = new BigDecimal(0.32).setScale(
            BIG_DECIMAL_SCALE, ROUNDING_MODE).stripTrailingZeros();

    public static final String NULL_INPUT_EXCEPTION_MESSAGE = "No input can be null";
    public static final String INVALID_SYMBOL_EXCEPTION_MESSAGE = "The symbol of a stock can have 1 to 4 characters and alphabetic characters only";
    public static final String NEGATIVE_EXCEPTION_MESSAGE = "The par value and last dividend of a stock can only be larger than or equal to zero";
    public static final String PERIOD_PER_YEAR_NEGATIVE_OR_ZERO_EXCEPTION_MESSAGE = "The dividend period of a stock can only be larger than zero";
    public static final BigDecimal POSITIVE_NON_INTEGER_WITH_TEN_DIGITS_PRECISION = new BigDecimal(
            102.1726354736);
}