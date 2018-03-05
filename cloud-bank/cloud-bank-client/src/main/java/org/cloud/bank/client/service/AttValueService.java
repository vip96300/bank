package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.AttValue;

public interface AttValueService {

	public List<AttValue> listByNamid(long namid);
	
	public void add(AttValue attValue);
	
	public void delByValid(long valid);
	
	public List<AttValue> list();
	
	public List<AttValue> listByFkvalue(String fkvalue);
}
