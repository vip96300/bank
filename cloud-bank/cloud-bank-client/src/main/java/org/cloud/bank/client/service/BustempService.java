package org.cloud.bank.client.service;

import org.cloud.bank.client.model.Bustemp;

public interface BustempService {

	public void add(Bustemp bustemp);
	
	public Bustemp getByCode(String code);
}
