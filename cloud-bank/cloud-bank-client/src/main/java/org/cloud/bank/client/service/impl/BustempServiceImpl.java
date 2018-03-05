package org.cloud.bank.client.service.impl;

import org.cloud.bank.client.model.Bustemp;
import org.cloud.bank.client.repository.BustempRepository;
import org.cloud.bank.client.service.BustempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BustempServiceImpl implements BustempService{

	@Autowired
	private BustempRepository bustempRepository;
	
	@Override
	public void add(Bustemp bustemp) {
		bustemp.setTime(System.currentTimeMillis());
		bustempRepository.save(bustemp);
	}

	@Override
	public Bustemp getByCode(String code) {
		Bustemp bustemp=bustempRepository.findByCode(code);
		return bustemp;
	}


}
