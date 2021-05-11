package com.my_store.watch.service;

import com.google.common.annotations.VisibleForTesting;
import com.my_store.watch.dto.PriceDto;
import com.my_store.watch.service.model.Discount;
import com.my_store.watch.service.model.Watch;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutService {

  private final Map<String, Watch> watchesCatalogue;

  /**
   * Calculates the total price for all the watches included in the checkout process.
   *
   * @param watchesList containing the watches to the checkout.
   * @return an instance of {@link PriceDto} representing the total price for the checkout.
   */
  public PriceDto calculateTotalCost(final List<String> watchesList) {
    final Map<String, Integer> watchesCount = new HashMap<>();

    for(String watchId : watchesList) {
      watchesCount.put(watchId, watchesCount.getOrDefault(watchId, 0) + 1);
    }

    int totalPrice = 0;
    for(Map.Entry<String, Integer> watchEntry : watchesCount.entrySet()) {
      totalPrice += calculateTotalCostPerWatch(watchEntry);
    }

    return PriceDto.builder().price(totalPrice).build();
  }

  /**
   * Calculates the total price for all the watches of only one type.
   * @param watchCountEntry A map entry of the watch id, and the number of times it is to be purchased.
   * @return an integer representing the total cost of all watches of the same type.
   */
  @VisibleForTesting
  int calculateTotalCostPerWatch(final Map.Entry<String, Integer> watchCountEntry) {
      final String watchId = watchCountEntry.getKey();
      final Integer watchCount = watchCountEntry.getValue();
      final int watchUnitPrice = watchesCatalogue.get(watchId).getUnitPrice();

      final Optional<Discount> maybeDiscount =
          Optional.ofNullable(watchesCatalogue.get(watchId).getDiscount());
      final int currentWatchCost;

      currentWatchCost = maybeDiscount
          .map(discount -> calculateTotalPriceForWatchOnDiscount(watchCount, watchUnitPrice, discount))
          .orElseGet(() -> watchCount * watchUnitPrice);

    return currentWatchCost;
  }

  /**
   * Calculates the total cost for all watches of the same type that have an applicable discount.
   * @param watchCount number of times that watches is about to be purchased.
   * @param watchUnitPrice unit price of the watch.
   * @param discount discount to apply.
   * @return the total cost for all watches of the same type with the available discount applied.
   */
  @VisibleForTesting
  int calculateTotalPriceForWatchOnDiscount(
      final int watchCount,
      final int watchUnitPrice,
      final Discount discount
  ) {
    final int unitsInDiscount = discount.getDiscountUnits();
    final int discountAmountForOneSet = discount.getDiscountAmount();

    final int watchSetsIncludedInDiscount = watchCount / unitsInDiscount;
    final int watchesNotIncludedInDiscount = watchCount % unitsInDiscount;

    return (watchSetsIncludedInDiscount * discountAmountForOneSet)
        + (watchesNotIncludedInDiscount * watchUnitPrice);
  }
}
