package org.cloud.bank.client.repository;

import org.cloud.bank.client.model.OutQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OutQueueRepository extends JpaRepository<OutQueue, Long> {
	
	@Query(value="select * from bank_business_outqueue o where o.outid =(select max(out.outid) from bank_business_outqueue out where out.curdate=?1)",nativeQuery=true)
	public OutQueue findByCurdateAndSeqMax(String curdate);
	
	public OutQueue findByCurdateAndSeq(String curdate,long seq);

}
