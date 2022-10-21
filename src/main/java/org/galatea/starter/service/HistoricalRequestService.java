package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.IexHistoricalPriceRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@ConfigurationProperties(prefix="spring.second-datasource")
@Component
@Service
public interface HistoricalRequestService extends JpaRepository<IexHistoricalPriceRequest, Long> {
    List<IexHistoricalPriceRequest> findByOption(String option);
}
