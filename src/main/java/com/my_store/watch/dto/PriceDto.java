package com.my_store.watch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
@JsonDeserialize(builder = PriceDto.PriceDtoBuilder.class)
@JsonInclude(Include.NON_NULL)
public class PriceDto {

  @JsonProperty("price")
  private int price;
}
