package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.Bank;


public interface BankService {
	
	public List<Bank> list(int page, int size);
	
	public void add(Bank bank);
	
	public void delByBanid(long banid);
}
