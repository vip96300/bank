package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.AttValue;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttValueRepository extends JpaRepository<AttValue, Long> {

	public List<AttValue> findByNamidAndIsdel(long namid,int isdel,Sort sort);

	public List<AttValue> findByNamidInAndIsdelOrderByValue(List<Long> namids,int isdel);

	public List<AttValue> findByIsdel(int isdel);
	
	public List<AttValue> findByFkvalueAndIsdel(String fkvalue,int isdel);
}
