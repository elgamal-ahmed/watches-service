package com.my_store.watch.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.my_store.watch.dto.PriceDto;
import com.my_store.watch.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

  public static final String CHECKOUT_PATH = "/checkout";
  private static final String WATCHES_LIST_REQUEST_DTO = "watchesList";

  private final CheckoutService checkoutService;

  @PostMapping(
      path = CHECKOUT_PATH, consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
  @Operation(summary = "Calculates the total cost for the watches included in the checkout.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "If the total cost for the watches is calculated successfully."),
      @ApiResponse(
          responseCode = "406",
          description = "If the Accept header is not equal to application/json."),
      @ApiResponse(
          responseCode = "415",
          description = "If the Content-type header is not equal to application/json.")
  })
  public PriceDto calculateCost(
      @RequestBody
      @Parameter(name = WATCHES_LIST_REQUEST_DTO,
          description = "A list containing the watches to checkout.")
      final List<String> watchesList
  ) {
    return checkoutService.calculateTotalCost(watchesList);
  }
}
