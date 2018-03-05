package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Archive;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
	
	public List<Archive> findByBusidAndStep(long busid,int step,Sort sort);
	
	public List<Archive> findByBusid(long busid,Sort sort);
}
