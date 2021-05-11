package com.my_store.watch.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
public class PriceDtoDeserializationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void shouldDeserializePriceDto() throws JsonProcessingException {
    // given
    final String jsonPriceDto = "{\n"
        + "    \"price\": 360\n"
        + "}";
    final PriceDto expected = PriceDto.builder().price(360).build();

    // when
    final PriceDto actual = objectMapper.readValue(jsonPriceDto,PriceDto.class);

    // then
    assertThat(actual).isInstanceOf(PriceDto.class);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void shouldDeserializePriceDto_empty_dto() throws JsonProcessingException {
    // given
    final String jsonPriceDto = "{}";
    final PriceDto expected = PriceDto.builder().build();

    // when
    final PriceDto actual = objectMapper.readValue(jsonPriceDto, PriceDto.class);

    // then
    assertThat(actual).isInstanceOf(PriceDto.class);
    assertThat(actual).isEqualTo(expected);
  }
}
