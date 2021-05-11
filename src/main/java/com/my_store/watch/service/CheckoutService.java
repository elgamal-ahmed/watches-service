package com.my_store.watch.service;

import com.my_store.watch.dto.PriceDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

  public PriceDto calculateTotalCost(final List<String> watchesList) {
    // TODO: Fill-in the business logic in WATCHES-6
    return PriceDto.builder().price(0).build();
  }
}
