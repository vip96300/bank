package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.Verify;

public interface VerifyService {
	
	public List<Verify> listByBusid(long busid);
}
