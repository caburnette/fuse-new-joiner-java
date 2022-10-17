package org.galatea.starter.entrypoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

  /**
   * Regex to match valid dates, see documentation: https://iexcloud.io/docs/api/#historical-prices
   */
  private final String VALID_DATES = "^max$|"
      + "^ytd$|"
      + "^[1-5]y$|"
      + "^(0?[1-9]|1[0-1])mm?$|"
      + "^0[1-9]dm?$|"
      + "^[1-2][1-9]?dm?$";


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
   * @param range Optional Parameter specifying the Range of time that the price should be
   *     measured over
   * @param date Optional Parameter specifying the exact date the price should be measured on
   * @return a IexLastTradedPrice object for the given symbol.
   */
  @GetMapping(value = "${mvc.iex.getHistoricalPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public IexHistoricalPrice getHistoricalPrice(
      @RequestParam(value = "symbol") @NotNull final String symbols,
      @RequestParam(value = "range", required = false) final Optional<@Pattern.List(
          {@Pattern(regexp = ".*"), @Pattern(regexp = VALID_DATES)}) String> range,
      @RequestParam(value = "date", required = false) final Optional<Integer> date) {
    IexHistoricalPrice price;
//@Pattern.List({@Pattern(regexp =".*"), @Pattern (regexp = VALID_DATES)})
    //3 Parameters: Symbol is REQUIRED, Date/Range are optional and cannot both be present

    if (date.isPresent()) {
      int dateVal = date.get();

      //Max validation depends on current time:
      if (dateVal >= System.currentTimeMillis()) {
        throw new IllegalArgumentException("Cannot get a price for a time in the future");
      }

      //Min validation depends on current time:
      long fiveYearsAgo = Long.parseLong(LocalDate.now().toString().replace("-", ""));
      fiveYearsAgo -= 500000;
      if (dateVal <= fiveYearsAgo) {
        throw new IllegalArgumentException(
            "Cannot go back more than 5 years: " + "Expected > " + fiveYearsAgo + " was: "
                + dateVal);
      }

      //Only Date or Range may be present.
      if (range.isPresent()) {
        throw new IllegalArgumentException("Cannot specify both range and date.");
      }
    }

    price = iexService.getHistoricalPriceForSymbol(symbols, range, date);

    return price;
  }

}
