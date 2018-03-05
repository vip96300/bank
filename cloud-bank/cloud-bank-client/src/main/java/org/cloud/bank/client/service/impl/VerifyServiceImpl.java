package org.cloud.bank.client.service.impl;

import java.util.List;

import org.cloud.bank.client.model.Verify;
import org.cloud.bank.client.repository.VerifyRepository;
import org.cloud.bank.client.service.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class VerifyServiceImpl implements VerifyService{

	@Autowired
	private VerifyRepository verifyRepository;

	@Override
	public List<Verify> listByBusid(long busid) {
		Sort sort=new Sort(Direction.DESC,"time");
		List<Verify> verifys=verifyRepository.findByBusid(busid, sort);
		return verifys;
	}
	
	
}
