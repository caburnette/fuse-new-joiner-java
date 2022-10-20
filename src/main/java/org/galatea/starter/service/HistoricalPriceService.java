package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexHistoricalPriceRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="spring.second-datasource")
@Component
public interface HistoricalPriceService extends JpaRepository<IexHistoricalPrice, Long> {
}
