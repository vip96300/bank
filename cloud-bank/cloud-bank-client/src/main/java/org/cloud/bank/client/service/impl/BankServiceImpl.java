package org.cloud.bank.client.service.impl;

import java.util.List;

import org.cloud.bank.client.model.Bank;
import org.cloud.bank.client.repository.BankRepository;
import org.cloud.bank.client.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BankServiceImpl implements BankService{
	
	@Autowired
	private BankRepository bankRepository;
	
	@Override
	public List<Bank> list(int page, int size) {
		List<Bank> banks=bankRepository.findByIsdel(0, new PageRequest(page, size)).getContent();
		return banks;
	}

	@Override
	public void add(Bank bank) {
		String maxCode=bankRepository.findByCodeMax();
		bank.setCode(genCode(maxCode));
		bank.setIsdel(0);
		bankRepository.save(bank);
	}

	@Override
	public void delByBanid(long banid) {
		Bank bank=bankRepository.findOne(banid);
		if(StringUtils.isEmpty(bank)){
			return;
		}
		bank.setIsdel(1);
		bankRepository.saveAndFlush(bank);
	}
	/**
	 * 新编码生成器
	 * @param maxCode
	 * @return
	 */
	private String genCode(String maxCode){
		String code="";
		if(StringUtils.isEmpty(maxCode)){
			code="001";
		}else{
			int icode=Integer.parseInt(maxCode)+1;
			if(icode/1000<1){
				code=""+icode;
			}
			if(icode/100<1){
				code="0"+icode;
			}
			if(icode/10<1){
				code="00"+icode;
			}
		}
		return code;
	}
}
