package com.my_store.watch.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
public class PriceDtoSerializationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void shouldSerializePriceDto() throws JsonProcessingException {
    // given
    final PriceDto priceDto = PriceDto.builder().price(360).build();
    final String expected = "{\"price\":360}";

    // when
    final String actual = objectMapper.writeValueAsString(priceDto);

    // then
    assertThat(actual).isEqualTo(expected);
  }
}
