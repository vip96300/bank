package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Classify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassifyRepository extends JpaRepository<Classify, Long> {
	
	public List<Classify> findByStepAndIsdelOrderByClaid(int step,int isdel);
}
