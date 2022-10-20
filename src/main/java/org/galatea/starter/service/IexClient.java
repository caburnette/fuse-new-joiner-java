package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * A Feign Declarative REST Client to access endpoints from the Free and Open IEX API to get market
 * data. See https://iextrading.com/developer/docs/
 */
@FeignClient(name = "IEX", url = "${spring.rest.iexBasePath}")
public interface IexClient {

  String TOKEN = "${spring.rest.token}";

  /**
   * Get a list of all stocks supported by IEX. See https://iextrading.com/developer/docs/#symbols.
   * As of July 2019 this returns almost 9,000 symbols, so maybe don't call it in a loop.
   *
   * @return a list of all of the stock symbols supported by IEX.
   */
  @GetMapping("/ref-data/symbols?token=" + TOKEN)
  List<IexSymbol> getAllSymbols();

  /**
   * Get the last traded price for each stock symbol passed in. See https://iextrading.com/developer/docs/#last.
   *
   * @param symbols stock symbols to get last traded price for.
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping("/tops/last?symbols={symbols}&token=" + TOKEN)
  List<IexLastTradedPrice> getLastTradedPriceForSymbols(@PathVariable String[] symbols);


  /**
   * Get the historical price for each stock symbol passed in. See https://iextrading.com/developer/docs/#historical-prices.
   *
   * @param symbol stock symbols to get last traded price for.
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping(path = "/stock/{symbol}/chart?token=" + TOKEN, params = {"symbol"})
  IexHistoricalPrice getHistoricalPriceTradedForSymbol(@PathVariable String symbol);

  /**
   * Get the historical price for each stock symbol passed in. See https://iextrading.com/developer/docs/#historical-prices.
   *
   * @param symbol stock symbols to get last traded price for.
   * @param range how far back the historical prices should be measured
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping(path = "/stock/{symbol}/chart/{range}?token=" + TOKEN, params = {"symbol", "range"})
  IexHistoricalPrice getHistoricalPriceTradedForSymbol(@PathVariable String symbol,
      @PathVariable String range);


  /**
   * Get the historical price for each stock symbol passed in. See https://iextrading.com/developer/docs/#historical-prices.
   *
   * @param symbol stock symbols to get last traded price for.
   * @param date the date these historical prices were measured as a timestamp
   * @return a list of the last traded price for each of the symbols passed in.
   */
  @GetMapping(path = "/stock/{symbol}/chart/date/{date}?token=" + TOKEN,
      params = {"symbol", "date"})
  IexHistoricalPrice getHistoricalPriceTradedForSymbolWithDate(@PathVariable String symbol,
      @PathVariable String date);


}
