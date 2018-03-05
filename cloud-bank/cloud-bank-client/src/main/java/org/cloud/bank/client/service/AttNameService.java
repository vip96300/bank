package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.AttName;

public interface AttNameService {
	
	public List<AttName> listByMenid(long menid);
	
	public void add(AttName attName);
	
	public AttName getByNamid(long namid);
	
	public void upd(AttName attName);
	
	public void delByNamid(long namid);
	
	public List<AttName> listByIsget();
	
}
