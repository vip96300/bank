package org.cloud.bank.client.repository;

import org.cloud.bank.client.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
	
	public long countByUseid(long useid);
	
	public Page<Message> findByUseidOrderByTimeDesc(long useid,Pageable pageable);
	
	public long countByParidAndOpeid(long parid,long opeid);
	
	public Page<Message> findByParidAndOpeidOrderByTimeDesc(long parid,long opeid,Pageable pageable);
	
	public Page<Message> findByOpeidOrderByTimeDesc(long opeid,Pageable pageable);
}
