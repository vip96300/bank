package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Verify;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyRepository extends JpaRepository<Verify, Long> {
	
	public List<Verify> findByBusid(long busid,Sort sort);
}
