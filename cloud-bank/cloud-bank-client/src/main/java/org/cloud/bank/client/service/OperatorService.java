package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.Operator;
import org.springframework.transaction.annotation.Transactional;

public interface OperatorService {
	
	@Transactional(rollbackFor=Exception.class)
	public void add(Operator operator,String token);
	
	public long count();
	
	public List<Operator> list(int page,int size);
	
	public List<Operator> listByParid(long parid);
	
	public void updByOpeidIsenable(long opeid);
	
	public void updByOpeidPassword(long opeid);

	public void delByOpeid(long opeid);

	public void updByOpeid(Operator operator);
	
	public String login(String username,String password);
	
	public Operator getByUsernamePassword(String username,String password);
	
	public void updByUsernamePassword(String username,String password);
	
	public Operator getByOpeid(long opeid);
	
	public List<Operator> listByBanidAndIsadmin(long banid,int isadmin);
	
	public void updIslistenByOpeid(long opeid);
}
