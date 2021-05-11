package com.my_store.watch.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An entity representing the the discount that might be applied on a watch.
 * Usage example: if we have a discount that is 2 for 200, the {@link Discount#getDiscountUnits()}
 * should be equal to 2, and {@link Discount#getDiscountAmount()} should be equal to 200.
 */
@Getter
@AllArgsConstructor
public class Discount {
  private int discountUnits;
  private int discountAmount;
}
