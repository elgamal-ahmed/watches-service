package com.my_store.watch.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An entity encapsulating data about a watch.
 */
@Getter
@AllArgsConstructor
public class Watch {
  private String id;
  private String name;
  private int unitPrice;
  private Discount discount;
}
