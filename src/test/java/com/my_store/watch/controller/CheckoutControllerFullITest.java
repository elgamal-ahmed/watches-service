package com.my_store.watch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.my_store.watch.config.WatchesCatalogueConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testing that the integration between different application components is working fine, and the
 * beans are getting loaded, especially the {@link WatchesCatalogueConfig#watchesCatalogue()} bean.
 * All beans in this testing class are the real ones (not mocked), covering the whole flow from end
 * to end.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CheckoutControllerFullITest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testHappyPath_multipleWatches() throws Exception {
    // given
    final String requestBodyInJson = "[\n"
        + "\"001\",\n"
        + "\"002\",\n"
        + "\"001\",\n"
        + "\"004\",\n"
        + "\"003\"\n"
        + "]";
    final int expectedTotalCost = 360;

    // when
    mockMvc.perform(
        post(CheckoutController.CHECKOUT_PATH)
            .content(requestBodyInJson)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", "application/json"))
        .andExpect(content().string("{\"price\":" + expectedTotalCost + "}"))
        .andReturn();
  }

  @Test
  public void testHappyPath_onlyOneWatch() throws Exception {
    // given
    final String requestBodyInJson = "[\n"
        + "\"001\"\n"
        + "]";
    final int expectedTotalCost = 100;

    // when
    mockMvc.perform(
        post(CheckoutController.CHECKOUT_PATH)
            .content(requestBodyInJson)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", "application/json"))
        .andExpect(content().string("{\"price\":" + expectedTotalCost + "}"))
        .andReturn();
  }
}
