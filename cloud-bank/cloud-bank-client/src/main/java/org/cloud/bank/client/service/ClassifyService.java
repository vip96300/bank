package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.Classify;

public interface ClassifyService {
	
	public void add(Classify classify);
	
	public void updByClaid(Classify classify);
	
	public void updIsdelByClaid(long claid);
	
	public List<Classify> list();
	
	public List<Classify> listByStep(int step);
	
}
