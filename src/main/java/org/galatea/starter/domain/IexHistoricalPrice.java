package org.galatea.starter.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IexHistoricalPrice {
  private String symbol;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;
  private BigInteger volume;
  private Date date;
}
