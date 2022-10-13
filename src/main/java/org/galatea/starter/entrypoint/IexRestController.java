package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.service.IexService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@Validated
@RestController
@RequiredArgsConstructor
public class IexRestController {

  @NonNull
  private IexService iexService;

  /**
   * Exposes an endpoint to get all of the symbols available on IEX.
   *
   * @return a list of all IexStockSymbols.
   */
  @GetMapping(value = "${mvc.iex.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexSymbol> getAllStockSymbols() {
    return iexService.getAllSymbols();
  }

  /**
   * Get the last traded price for each of the symbols passed in.
   *
   * @param symbols list of symbols to get last traded price for.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.iex.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbols") final List<String> symbols) {
    return iexService.getLastTradedPriceForSymbols(symbols);
  }

  /**
   * Get the historical prices of specified symbols.
   *
   * @param symbols list of symbols to get historical price for.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.iex.getHistoricalPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public IexHistoricalPrice getHistoricalPrice(
      @RequestParam(value = "symbol") final String symbols,
      @RequestParam(value = "range", required = false) final String range,
      @RequestParam(value = "date",required = false) final Integer date) {
    IexHistoricalPrice price;

    //3 Parameters: Symbol is REQUIRED, Date/Range are optional and cannot both be present
    //If symbol == null, it will get passed back later

    if(range == null) {
      if(date == null) {
        //No optional parameters
        price = iexService.getHistoricalPriceForSymbol(symbols,null);
      } else {
        //Date is present
        price = iexService.getHistoricalPriceForSymbol(symbols,(Object)date);
      }
    } else {
      if(date == null) {
        //Range is present
        price = iexService.getHistoricalPriceForSymbol(symbols,(Object)range);
      } else {
        //Invalid because both date/range present
        price = null;
      }
    }
    return price;
  }

}
