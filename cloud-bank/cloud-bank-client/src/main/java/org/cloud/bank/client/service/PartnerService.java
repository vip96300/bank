package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.Partner;

public interface PartnerService {
	
	public long count();
	
	public List<Partner> list(int page,int size);
	
	public List<Partner> listByBanid(long banid);
	
	public void add(Partner partner);

	public void updByParidIsenable(long parid);
	
	public void updByParidPassword(long parid);
	
	public void updByParid(Partner partner);
	
	public void delByParid(long parid);
	
	public String login(String username,String password);
	
	public Partner getByUsernamePassword(String username,String password);
	
	public void updByUsernamePassword(String username,String password);
	
	public Partner getByParid(long parid);
}
