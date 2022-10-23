package org.galatea.starter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
@Builder
@Table(name = "prices")
public class IexHistoricalPrice {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @Column(nullable=false)
  private String symbol;
  @Column
  private BigDecimal close;
  @Column
  private BigDecimal high;
  @Column
  private BigDecimal low;
  @Column
  private BigDecimal open;
  @Column
  private BigInteger volume;
  @Column
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;


  public IexHistoricalPrice() {}

  //I am only including these constructors because It won't let me retrieve from the DB without a default constructor, and it won't let me have the default without the one below:

  public IexHistoricalPrice(Long id, String symbol, BigDecimal close, BigDecimal high, BigDecimal low, BigDecimal open, BigInteger volume, LocalDate date) {
    this.id = id;
    this.symbol = symbol;
    this.close = close;
    this.high = high;
    this.low = low;
    this.open = open;
    this.volume = volume;
    this.date = date;
  }

}
