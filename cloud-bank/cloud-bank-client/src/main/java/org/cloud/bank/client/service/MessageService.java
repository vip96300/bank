package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.Message;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService {

	@Transactional(rollbackFor=Exception.class)
	public void add(Message message,String token);
	
	public Message getByMesid(long mesid);
	
	public long countByUseid(long useid);
	
	public List<Message> listByUseid(long useid,int page,int size);
	
	public void delByMesid(long mesid);
	
	public long countByParid(long parid);
	
	public List<Message> listByParid(long parid,int page,int size);
	
	public List<Message> listByOpeid(long opeid,int page,int size);
}
