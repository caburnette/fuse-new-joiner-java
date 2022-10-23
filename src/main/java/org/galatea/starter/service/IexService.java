package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexService {

  @NonNull
  private IexClient iexClient;


  /**
   * Get all stock symbols from IEX.
   *
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols() {
    return iexClient.getAllSymbols();
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]));
    }
  }

  /**
   * Get the historical price for the Symbol that is passed in, with an optional parameter
   * representing either range or date
   *
   * @param symbol the list of symbols to get a historical traded price for.
   * @param date Optional Parameter: The date the price should be measured on
   * @param range Optional Parameter: Time range to measure price over
   * @return the historical traded price objects for the Symbol that is passed in.
   */
  public IexHistoricalPrice getHistoricalPriceForSymbol(String symbol, Optional<String> date,
      Optional<String> range) {

    if (date.isPresent()) {
      //Date has been specified,
      String dateVal = date.get();

      return iexClient.getHistoricalPriceTradedForSymbolWithDate(symbol, dateVal);
    }

    if (range.isPresent()) {
      //Range has been specified
      String rangeVal = range.get();
      return iexClient.getHistoricalPriceTradedForSymbol(symbol, rangeVal);
    }
    return iexClient.getHistoricalPriceTradedForSymbol(symbol);


  }


}
