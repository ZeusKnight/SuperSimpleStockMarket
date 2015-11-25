package sibo.liu.jpm.supersimplestockmarket;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

import org.joda.time.DateTime;

import sibo.liu.jpm.supersimplestockmarket.stock.CommonStock;
import sibo.liu.jpm.supersimplestockmarket.stock.PreferredStock;
import sibo.liu.jpm.supersimplestockmarket.stock.StockImpl;
import sibo.liu.jpm.supersimplestockmarket.transaction.BuySellIndicator;
import sibo.liu.jpm.supersimplestockmarket.transaction.StockTransactionManager;
import sibo.liu.jpm.supersimplestockmarket.transaction.Transaction;

/**
 * A live console interaction program of this module.
 * <p>
 * Enter <b>EMPTY_STRING</b> to get GBCE All Share Index
 * <p>
 * For example:
 * <p>
 * Input:
 * <p>
 * Output: GBCE Index of All Stock: 108.70071831
 * <p>
 * Enter <b>StockSymbol</b> to get volume weighted price of a stock
 * <p>
 * For example:
 * <p>
 * Input: MSFT
 * <p>
 * Output: Volume Weighted Stock Price of MSFT: 111.75066667
 * <p>
 * Enter <b>StockSymbol Price</b> to get P/E Ratio and Divided Yield
 * <p>
 * For example:
 * <p>
 * Input: MSFT 3.23
 * <p>
 * Output: P/E Ratio: 0.000323 Dividend Yield: 3095.9752322
 * <p>
 * Enter <b>StockSymbol TimeStamp(in format yyyy-MM-ddTHH:mm:ss) Quantity
 * Indicator(B or S) Price</b> to record a transaction
 * <p>
 * For example:
 * <p>
 * Input: MSFT 2015-11-24T16:36:31 1000 B 117.23
 * <p>
 * Output: Success.
 * <p>
 * Input: MSFT 2015-11-24T16:35:20 500 S 100.792
 * <p>
 * Output: Success.
 * <p>
 * Enter <b>StockType(P or C) StockSymbol ParValue LastAnnualDividend
 * PeriodPerYear DividendPerPeriod</b> to add a stock
 * <p>
 * For example:
 * <p>
 * Input: P MSFT 1000 10 2 5
 * <p>
 * Output: Success.
 * 
 * @author sibliu
 *
 */
public class SuperSimpleStockMarket {

    private SuperSimpleStockMarket() {
    }

    private static final StockTransactionManager MANAGER = StockTransactionManager.INSTANCE;

    public static void main(String... args) {
        CommonStock stock1 = new CommonStock("TEA", new BigDecimal(100), BigDecimal.ZERO,
                BigInteger.ONE, BigDecimal.ZERO);
        CommonStock stock2 = new CommonStock("POP", new BigDecimal(100), new BigDecimal(8),
                BigInteger.ONE, new BigDecimal(8));
        CommonStock stock3 = new CommonStock("ALE", new BigDecimal(60), new BigDecimal(23),
                BigInteger.ONE, new BigDecimal(23));
        PreferredStock stock4 = new PreferredStock("GIN", new BigDecimal(100), new BigDecimal(8),
                BigInteger.valueOf(4), BigDecimal.valueOf(0.02));
        CommonStock stock5 = new CommonStock("JOE", new BigDecimal(250), new BigDecimal(13),
                BigInteger.ONE, new BigDecimal(13));
        MANAGER.addStock(stock1);
        MANAGER.addStock(stock2);
        MANAGER.addStock(stock3);
        MANAGER.addStock(stock4);
        MANAGER.addStock(stock5);
        Scanner scanner = new Scanner(System.in);
        String next = null;
        while (!"EXIT".equals(next = scanner.nextLine())) {
            try {
                String[] param = next.split(" ");
                switch (param.length) {
                case 1:
                    if (param[0].isEmpty())
                        getGBCEIndex();
                    else
                        getVolumeWeightedStockPrice(param);
                    break;
                case 2:
                    getPERatioAndDividendYield(param);
                    break;
                case 5:
                    addTransaction(param);
                    break;
                case 6:
                    addStock(param);
                    break;
                default:
                    System.out.println("Unrecognized Operation.");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        scanner.close();
    }

    private static final void addStock(String... args) {
        String symbol = args[1];
        try {
            BigDecimal parValue = new BigDecimal(args[2]);
            BigDecimal lastAnnualDividend = new BigDecimal(args[3]);
            BigInteger periodPerYear = new BigInteger(args[4]);
            BigDecimal dividendPerPeriod = new BigDecimal(args[5]);
            StockImpl stockImpl = null;
            if ("C".equals(args[0])) {
                stockImpl = new CommonStock(symbol, parValue, lastAnnualDividend, periodPerYear,
                        dividendPerPeriod);

            } else if ("P".equals(args[0])) {
                stockImpl = new PreferredStock(symbol, parValue, lastAnnualDividend, periodPerYear,
                        dividendPerPeriod);
            }
            if (MANAGER.addStock(stockImpl)) {
                System.out.println("Success.");
            } else {
                System.out.println("Fail.");
            }
        } catch (NumberFormatException e) {
            System.out
                    .println("Invalid Par Value, Last Annual Dividend, Period Per Year or Dividend Per Period");
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    private static final void addTransaction(String... args) {
        String symbol = args[0];
        try {
            DateTime timeStamp = new DateTime(args[1]);
            BigInteger quantity = new BigInteger(args[2]);
            BuySellIndicator indicator = BuySellIndicator.getBuySellIndicator(args[3]).orElseThrow(
                    () -> new IllegalArgumentException(
                            "Invalid Buy/Sell Indicator or Transaction Time"));
            BigDecimal price = new BigDecimal(args[4]);

            Transaction transaction = new Transaction(symbol, timeStamp, quantity, indicator, price);
            if (MANAGER.addTransaction(symbol, transaction)) {
                System.out.println("Success.");
            } else {
                System.out.println("Fail.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Quantity or Price");
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    private static final void getPERatioAndDividendYield(String... args) {
        String symbol = args[0];
        try {
            BigDecimal price = new BigDecimal(args[1]);
            if (MANAGER.getStock(symbol).isPresent()) {
                System.out.println("P/E Ratio: "
                        + MANAGER.getStock(symbol).get().getPERatio(price).orElse(BigDecimal.ZERO));
                System.out.println("Dividend Yield: "
                        + MANAGER.getStock(symbol).get().getDividendYield(price)
                                .orElse(BigDecimal.ZERO));
            } else {
                System.out.println("P/E Ratio: " + 0);
                System.out.println("Dividend Yield: " + 0);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Price");
        }
    }

    private static final void getVolumeWeightedStockPrice(String... args) {
        String symbol = args[0];
        System.out.print("Volume Weighted Stock Price of " + symbol + ": ");
        if (MANAGER.getStock(symbol).isPresent()) {
            System.out.println(MANAGER.getStock(symbol).get().getVolumeWeightedStockPrice(15)
                    .orElse(BigDecimal.ZERO));
        } else {
            System.out.println(0);
        }
    }

    private static final void getGBCEIndex() {
        System.out.print("GBCE Index of All Stock: ");
        System.out.println(MANAGER.getGBCEAllShareIndex().isPresent() ? MANAGER
                .getGBCEAllShareIndex().get() : 0);
    }
}