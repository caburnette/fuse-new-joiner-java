package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
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
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;
}
