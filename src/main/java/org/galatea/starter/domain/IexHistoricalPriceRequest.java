
package org.galatea.starter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="requests")
public class IexHistoricalPriceRequest {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @Getter
  @Setter
  private IexHistoricalPrice price;

  @Column
  @Getter
  @Setter
  private String option;
}