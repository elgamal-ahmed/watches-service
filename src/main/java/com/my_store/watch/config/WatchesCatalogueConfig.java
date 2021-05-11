package com.my_store.watch.config;

import com.my_store.watch.service.model.Discount;
import com.my_store.watch.service.model.Watch;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WatchesCatalogueConfig {

  final Map<String, Watch> map = new HashMap<>();

  /**
   * Loading the provided watch catalogue.
   */
  @Bean
  public Map<String, Watch> watchesCatalogue() {
    map.put("001", new Watch("001", "Rolex", 100, new Discount(3, 200)));
    map.put("002", new Watch("002", "Michael Kors", 80, new Discount(3, 200)));
    map.put("003", new Watch("003", "Swatch", 50, null));
    map.put("004", new Watch("004", "Casio", 30, null));
    return map;
  }
}
