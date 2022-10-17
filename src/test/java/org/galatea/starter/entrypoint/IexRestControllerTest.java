package org.galatea.starter.entrypoint;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import junitparams.JUnitParamsRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.ASpringTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@RequiredArgsConstructor
@Slf4j
// We need to do a full application start up for this one, since we want the feign clients to be instantiated.
// It's possible we could do a narrower slice of beans, but it wouldn't save that much test run time.
@SpringBootTest
// this gives us the MockMvc variable
@AutoConfigureMockMvc
// we previously used WireMockClassRule for consistency with ASpringTest, but when moving to a dynamic port
// to prevent test failures in concurrent builds, the wiremock server was created too late and feign was
// already expecting it to be running somewhere else, resulting in a connection refused
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock")
// Use this runner since we want to parameterize certain tests.
// See runner's javadoc for more usage.
@RunWith(JUnitParamsRunner.class)
public class IexRestControllerTest extends ASpringTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void testGetSymbolsEndpoint() throws Exception {
    MvcResult result = this.mvc.perform(
            // note that we were are testing the fuse REST end point here, not the IEX end point.
            // the fuse end point in turn calls the IEX end point, which is WireMocked for this test.
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/iex/symbols")
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        // some simple validations, in practice I would expect these to be much more comprehensive.
        .andExpect(jsonPath("$[0].symbol", is("A")))
        .andExpect(jsonPath("$[1].symbol", is("AA")))
        .andExpect(jsonPath("$[2].symbol", is("AAAU")))
        .andReturn();
  }

  @Test
  public void testGetLastTradedPrice() throws Exception {

    MvcResult result = this.mvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/iex/lastTradedPrice?symbols=FB")
                // This URL will be hit by the MockMvc client. The result is configured in the file
                // src/test/resources/wiremock/mappings/mapping-lastTradedPrice.json
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].symbol", is("FB")))
        .andExpect(jsonPath("$[0].price").value(new BigDecimal("186.34")))
        .andReturn();
  }

  @Test
  public void testGetLastTradedPriceEmpty() throws Exception {

    MvcResult result = this.mvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/iex/lastTradedPrice?symbols=")
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(Collections.emptyList())))
        .andReturn();
  }


  @Test
  public void testGetHistoricalPriceNoRangeNoDate() throws Exception {
    //Tests functionality with No Range or Date
    MvcResult result = this.mvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/iex/historicalPrice?symbol=FB")
                // This URL will be hit by the MockMvc client. The result is configured in the file
                // src/test/resources/wiremock/mappings/mapping-lastTradedPrice.json
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.symbol", is("FB")))
        .andExpect(jsonPath("$.close").value(new BigDecimal("261.86")))
        .andExpect(jsonPath("$.high").value(new BigDecimal("264.81")))
        .andExpect(jsonPath("$.low").value(new BigDecimal("262.03")))
        .andExpect(jsonPath("$.open").value(new BigDecimal("265.07")))
        .andExpect(jsonPath("$.volume").value(new BigInteger("17624513")))
        .andExpect(jsonPath("$.date").value("2022-10-11"))
        .andReturn();
  }

  @Test
  public void testGetHistoricalPriceWithRangeNoDate() throws Exception {
    //Tests functionality with Range specified
    MvcResult result = this.mvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/iex/historicalPrice?symbol=FB&range=3m")
                // This URL will be hit by the MockMvc client. The result is configured in the file
                // src/test/resources/wiremock/mappings/mapping-lastTradedPrice.json
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.symbol", is("FB")))
        .andExpect(jsonPath("$.close").value(new BigDecimal("280.2")))
        .andExpect(jsonPath("$.high").value(new BigDecimal("281.3")))
        .andExpect(jsonPath("$.low").value(new BigDecimal("279.3")))
        .andExpect(jsonPath("$.open").value(new BigDecimal("280.1")))
        .andExpect(jsonPath("$.volume").value(new BigInteger("17624520")))
        .andExpect(jsonPath("$.date").value("2022-10-11"))
        .andReturn();
  }

  @Test
  public void testGetHistoricalPriceNoRangeWithDate() throws Exception {
    //Tests functionality with Date specified
    MvcResult result = this.mvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/iex/historicalPrice?symbol=FB&date=20190220")
                // This URL will be hit by the MockMvc client. The result is configured in the file
                // src/test/resources/wiremock/mappings/mapping-lastTradedPrice.json
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.symbol", is("FB")))
        .andExpect(jsonPath("$.close").value(new BigDecimal("270.2")))
        .andExpect(jsonPath("$.high").value(new BigDecimal("271.34")))
        .andExpect(jsonPath("$.low").value(new BigDecimal("272.31")))
        .andExpect(jsonPath("$.open").value(new BigDecimal("270.34")))
        .andExpect(jsonPath("$.volume").value(new BigInteger("17624356")))
        .andExpect(jsonPath("$.date").value("2022-11-11"))
        .andReturn();
  }

  @Test
  public void testGetHistoricalPriceEmpty() throws Exception {
    //Tests functionality with No symbol specified
    // Note that this is the same as testing with no Symbol but adding a date|range
    MvcResult result = this.mvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/iex/historicalPrice?symbol=")
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist())
        .andReturn();
  }
}
