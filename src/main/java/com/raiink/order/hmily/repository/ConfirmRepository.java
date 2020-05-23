package com.raiink.order.hmily.repository;

import com.raiink.order.hmily.domain.ConfirmLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ConfirmRepository extends CrudRepository<ConfirmLog, String> {
  Boolean existsByTxNo(String txNo);
}
