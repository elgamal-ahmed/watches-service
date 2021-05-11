package com.my_store.watch.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.my_store.watch.dto.PriceDto;
import com.my_store.watch.service.model.Discount;
import com.my_store.watch.service.model.Watch;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CheckoutServiceTest {

  private static final Map<String, Watch> map = Map.of(
      "001", new Watch("001", "Rolex", 100, new Discount(3, 200)),
      "002", new Watch("002", "Michael Kors", 80, new Discount(3, 200)),
      "003", new Watch("003", "Swatch", 50, null),
      "004", new Watch("004", "Casio", 30, null)
  );

  private CheckoutService checkoutService;

  @BeforeEach
  public void init() {
    this.checkoutService = new CheckoutService(map);
  }

  @Test
  public void testCalculateTotalCost() {
    // given
    final List<String> watchesList = List.of("001", "001", "001", "002", "002", "003", "004", "004");
    final int totalCostExpected = 470; // 200 + 2 * 80 + 50 + 2 * 30

    // when
    final PriceDto actual = checkoutService.calculateTotalCost(watchesList);

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getPrice()).isEqualTo(totalCostExpected);
  }

  @Test
  public void testCalculateTotalCost_onlyOneWatch() {
    // given
    final List<String> watchesList = List.of("001");
    final int totalCostExpected = 100;

    // when
    final PriceDto actual = checkoutService.calculateTotalCost(watchesList);

    // then
    assertThat(actual).isNotNull();
    assertThat(actual.getPrice()).isEqualTo(totalCostExpected);
  }

  @ParameterizedTest
  @MethodSource("testCalculateTotalCostPerWatchData")
  public void testCalculateTotalCostPerWatch(
      final Map.Entry<String, Integer> watchCountEntry,
      final int expectedTotalCostPerWatch
  ) {
    // given
    // when
    final int actual = checkoutService.calculateTotalCostPerWatch(watchCountEntry);

    // then
    assertThat(actual).isEqualTo(expectedTotalCostPerWatch);
  }

  private static Stream<Arguments> testCalculateTotalCostPerWatchData() {
    return Stream.of(
        // test watches that have discount.
        Arguments.of(new SimpleEntry<>("001", 3), 200),
        Arguments.of(new SimpleEntry<>("001", 5), 400),
        Arguments.of(new SimpleEntry<>("001", 7), 500),

        // test watches that don't have discount.
        Arguments.of(new SimpleEntry<>("003", 1), 50),
        Arguments.of(new SimpleEntry<>("003", 10), 500),
        Arguments.of(new SimpleEntry<>("004", 7), 210));
  }

  @ParameterizedTest
  @MethodSource("testGetPriceForWatchInDiscountData")
  public void testGetPriceForWatchInDiscount(
      final int watchCount,
      final int watchUnitPrice,
      final Discount watchDiscount,
      final int totalPrice
  ) {
    // given
    // when
    final int actual = checkoutService
        .calculateTotalPriceForWatchOnDiscount(watchCount, watchUnitPrice, watchDiscount);

    // then
    assertThat(actual).isEqualTo(totalPrice);
  }

  private static Stream<Arguments> testGetPriceForWatchInDiscountData() {
    return Stream.of(
        Arguments.of(5, 200, new Discount(2, 350), (2 * 350 + 200)),
        Arguments.of(7, 300, new Discount(3, 800), (2 * 800 + 300)),
        Arguments.of(3, 100, new Discount(2, 150), (1 * 150 + 100)),
        Arguments.of(7, 200, new Discount(4, 700), (1 * 700 + 3 * 200)));
  }
}
