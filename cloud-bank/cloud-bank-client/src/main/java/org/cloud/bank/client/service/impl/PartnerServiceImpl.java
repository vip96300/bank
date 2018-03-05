package org.cloud.bank.client.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.bank.client.model.Bank;
import org.cloud.bank.client.model.Partner;
import org.cloud.bank.client.repository.BankRepository;
import org.cloud.bank.client.repository.PartnerRepository;
import org.cloud.bank.client.service.PartnerService;
import org.cloud.bank.client.util.JwtUtil;
import org.cloud.bank.client.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PartnerServiceImpl implements PartnerService{

	@Autowired
	private PartnerRepository partnerRepository;
	@Autowired
	private BankRepository bankRepository;
	
	private static final String PASSWORD_DEFAULT="123456";
	
	@Override
	public long count() {
		long count=partnerRepository.countByIsdel(0);
		return count;
	}
	
	@Override
	public List<Partner> list(int page, int size) {
		List<Partner> partners=partnerRepository.findByIsdel(0,new PageRequest(page, size)).getContent();
		return partners;
	}
	
	@Override
	public List<Partner> listByBanid(long banid) {
		List<Partner> partners=partnerRepository.findByBanidAndIsdel(banid,0);
		return partners;
	}

	@Override
	public void add(Partner partner) {
		Bank bank=bankRepository.findOne(partner.getBanid());
		if(StringUtils.isEmpty(bank)){
			return;
		}
		partner.setPassword(Md5Util.md5(PASSWORD_DEFAULT));
		partner.setBankname(bank.getName());
		partner.setIsenable(1);
		partner.setIsdel(0);
		String maxCode=partnerRepository.findByCodeMax(bank.getCode());
		partner.setCode(bank.getCode()+genCode(maxCode));
		partnerRepository.save(partner);
		
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
			maxCode=maxCode.substring(3,6);
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

	@Override
	public void updByParidIsenable(long parid) {
		Partner partner=partnerRepository.findOne(parid);
		if(StringUtils.isEmpty(partner)){
			return;
		}
		int isenable=partner.getIsenable();
		if(isenable==0){
			partner.setIsenable(1);
		}
		if(isenable==1){
			partner.setIsenable(0);
		}
		partnerRepository.saveAndFlush(partner);
	}

	@Override
	public void updByParidPassword(long parid) {
		Partner partner=partnerRepository.findOne(parid);
		if(StringUtils.isEmpty(partner)){
			return;
		}
		partner.setPassword(Md5Util.md5(PASSWORD_DEFAULT));
		partnerRepository.saveAndFlush(partner);
	}

	@Override
	public void updByParid(Partner partner) {
		Partner partnerPO=partnerRepository.findOne(partner.getParid());
		if(StringUtils.isEmpty(partnerPO)){
			return;
		}
		partnerPO.setSurname(partner.getSurname());
		partnerPO.setHostname(partner.getHostname());
		partnerRepository.saveAndFlush(partnerPO);
	}

	@Override
	public void delByParid(long parid) {
		Partner partner=partnerRepository.findOne(parid);
		if(StringUtils.isEmpty(partner)){
			return;
		}
		partner.setIsdel(1);
		partnerRepository.saveAndFlush(partner);
	}

	@Override
	public String login(String username, String password) {
		Partner partner=partnerRepository.findByUsernameAndPassword(username, Md5Util.md5(password));
		if(StringUtils.isEmpty(partner)){
			return null;
		}
		if(partner.getIsenable()==0){//账户禁用
			return null;
		}
		Map<String,Object> headerParams=new HashMap<String,Object>();
		headerParams.put("alg", "HS256");
		headerParams.put("type", "jwt");
		Map<String,Object> bodyParams=new HashMap<String,Object>();
		bodyParams.put("userid",partner.getParid());
		String token =JwtUtil.tokenGenerator(headerParams, bodyParams);
		return token;
	}

	@Override
	public void updByUsernamePassword(String username, String password) {
		Partner partner=partnerRepository.findByUsername(username);
		if(StringUtils.isEmpty(partner)){
			return;
		}
		partner.setPassword(Md5Util.md5(password));
		partnerRepository.saveAndFlush(partner);
	}

	@Override
	public Partner getByUsernamePassword(String username, String password) {
		Partner partner=partnerRepository.findByUsernameAndPassword(username, Md5Util.md5(password));
		return partner;
	}

	@Override
	public Partner getByParid(long parid) {
		Partner partner=partnerRepository.findOne(parid);
		return partner;
	}

	


}
