package com.raiink.order.hmily.repository;

import com.raiink.order.hmily.domain.TryLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface TryRepository extends CrudRepository<TryLog, String> {
  // Integer countByTxNoAnd(String txNo);

  Boolean existsByTxNo(String txNo);
}
