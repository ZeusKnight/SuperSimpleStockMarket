package sibo.liu.jpm.supersimplestockmarket.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BuySellIndicatorTest {

	private static final String BUY_SYMBOL = "B";
	private static final String SELL_SYMBOL = "S";

	@Test
	public void testGetIndicatorString() {
		BuySellIndicator indicator = BuySellIndicator.BUY;
		assertTrue(BUY_SYMBOL.equals(indicator.getIndicatorString()));

		indicator = BuySellIndicator.SELL;
		assertTrue(SELL_SYMBOL.equals(indicator.getIndicatorString()));
	}

	@Test
	public void testGetBuySellIndicator() {
		assertTrue(BuySellIndicator.getBuySellIndicator(BUY_SYMBOL).isPresent());
		assertEquals(BUY_SYMBOL, BuySellIndicator.getBuySellIndicator(BUY_SYMBOL).get()
				.getIndicatorString());

		assertTrue(BuySellIndicator.getBuySellIndicator(SELL_SYMBOL).isPresent());
		assertEquals(SELL_SYMBOL, BuySellIndicator.getBuySellIndicator(SELL_SYMBOL).get()
				.getIndicatorString());

		assertFalse(BuySellIndicator.getBuySellIndicator(null).isPresent());
		assertFalse(BuySellIndicator.getBuySellIndicator("A").isPresent());
		assertFalse(BuySellIndicator.getBuySellIndicator("BS").isPresent());
	}
}