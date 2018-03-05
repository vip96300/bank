package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.exception.IllegalBusinessStateException;
import org.cloud.bank.client.model.Archive;
import org.springframework.transaction.annotation.Transactional;

public interface ArchiveService {
	
	@Transactional(rollbackFor=Exception.class)
	public void add(Archive archive);
	
	@Transactional(rollbackFor=Exception.class)
	public void add_batch(String archives);
	
	@Transactional(rollbackFor=Exception.class)
	public void add_batch_step1 (String archives) throws IllegalBusinessStateException ;
	
	@Transactional(rollbackFor=Exception.class)
	public void add_batch_step3(String archives);
	
	public List<Archive> listByBusidStep(long busid,int step);
	
	public List<Archive> listByBusid(long busid);
	
}
