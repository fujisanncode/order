package com.raiink.order.hmily.repository;

import com.raiink.order.hmily.domain.CancelLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CancelRepository extends CrudRepository<CancelLog, String> {
  Boolean existsByTxNo(String txNo);
}
