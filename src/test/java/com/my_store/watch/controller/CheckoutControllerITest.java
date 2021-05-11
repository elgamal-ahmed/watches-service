package com.my_store.watch.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.my_store.watch.dto.PriceDto;
import com.my_store.watch.service.CheckoutService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testing the controller class in isolation from the rest of the application components.
 */
@WebMvcTest
@ContextConfiguration(classes = {
    CheckoutController.class,
})
public class CheckoutControllerITest {

  private static final String REQUEST_BODY_IN_JSON = "[\n"
      + "\"001\",\n"
      + "\"002\",\n"
      + "\"001\",\n"
      + "\"004\",\n"
      + "\"003\"\n"
      + "]";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CheckoutService checkoutService;

  @Test
  public void testHappyPath() throws Exception {
    // given
    final int totalPrice = 250;
    final List<String> expectedWatchesParameter = List.of("001", "002", "001", "004", "003");
    doReturn(PriceDto.builder().price(totalPrice).build())
        .when(checkoutService).calculateTotalCost(anyList());

    // when
    mockMvc.perform(
        post(CheckoutController.CHECKOUT_PATH)
            .content(REQUEST_BODY_IN_JSON)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", "application/json"))
        .andExpect(content().string("{\"price\":" + totalPrice + "}"))
        .andReturn();

    final ArgumentCaptor<List<String>> argumentCaptor = ArgumentCaptor.forClass(List.class);
    verify(checkoutService).calculateTotalCost(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(expectedWatchesParameter);
    verifyNoMoreInteractions(checkoutService);
  }

  @Test
  public void testHappyPath_emptyInput() throws Exception {
    // given
    final int totalPrice = 0;

    doReturn(PriceDto.builder().price(totalPrice).build())
        .when(checkoutService).calculateTotalCost(anyList());

    // when
    mockMvc.perform(
        post(CheckoutController.CHECKOUT_PATH)
            .content("[]")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", "application/json"))
        .andExpect(content().string("{\"price\":" + totalPrice + "}"))
        .andReturn();

    final ArgumentCaptor<List<String>> argumentCaptor = ArgumentCaptor.forClass(List.class);
    verify(checkoutService).calculateTotalCost(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(Collections.EMPTY_LIST);
    verifyNoMoreInteractions(checkoutService);
  }

  @Test
  public void checkoutEndpoint_returns_406_when_accept_header_is_incorrect() throws Exception {
    // given
    // when
    this.mockMvc.perform(
        post(CheckoutController.CHECKOUT_PATH)
            .accept(MediaType.TEXT_HTML)
            .content(REQUEST_BODY_IN_JSON)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotAcceptable());

    // then
    verifyNoInteractions(checkoutService);
  }

  @Test
  public void checkoutEndpoint_returns_415_when_content_type_header_is_incorrect() throws Exception {
    // given
    // when
    this.mockMvc.perform(
        post(CheckoutController.CHECKOUT_PATH)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(REQUEST_BODY_IN_JSON)
            .contentType(MediaType.TEXT_PLAIN))
        .andExpect(status().isUnsupportedMediaType());

    // then
    verifyNoInteractions(checkoutService);
  }
}
