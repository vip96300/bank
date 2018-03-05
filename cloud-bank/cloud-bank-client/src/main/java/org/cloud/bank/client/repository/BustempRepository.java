package org.cloud.bank.client.repository;

import org.cloud.bank.client.model.Bustemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BustempRepository extends JpaRepository<Bustemp, Long> {
	
	
	public Bustemp findByCode(String code);
}
