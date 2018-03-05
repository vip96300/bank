package org.cloud.bank.client.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.bank.client.model.Bank;
import org.cloud.bank.client.model.Listen;
import org.cloud.bank.client.model.Operator;
import org.cloud.bank.client.model.Partner;
import org.cloud.bank.client.repository.BankRepository;
import org.cloud.bank.client.repository.ListenRepository;
import org.cloud.bank.client.repository.OperatorRepository;
import org.cloud.bank.client.repository.PartnerRepository;
import org.cloud.bank.client.service.OperatorService;
import org.cloud.bank.client.util.JwtUtil;
import org.cloud.bank.client.util.Md5Util;
import org.cloud.bank.client.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OperatorServiceImpl implements OperatorService {
	/**
	 * 默认操作员密码
	 */
	private static final String DEFAULT_PASSWORD="123456";
	/**
	 * 默认注册的聊天用户密码
	 */
	private static final String DEFAULT_CHAT_PASSWORD="123456";
	
	@Autowired
	private OperatorRepository operatorRepository;
	@Autowired
	private BankRepository bankRepository;
	@Autowired
	private PartnerRepository partnerRepository;
	@Autowired
	private ListenRepository listenRepository;

	@Override
	public void add(Operator operator,String token) {
		Bank bank=bankRepository.findOne(operator.getBanid());
		Partner partner=partnerRepository.findByBanidAndParid(operator.getBanid(),operator.getParid());
		operator.setBankname(bank.getName());
		operator.setPartnername(partner.getSurname());
		operator.setIsenable(1);
		operator.setIslisten(1);
		operator.setIsdel(0);
		operator.setPassword(Md5Util.md5(DEFAULT_PASSWORD));
		String maxCode=operatorRepository.findByCodeMax(partner.getCode());
		operator.setCode(partner.getCode()+OperatorServiceImpl.genCode(maxCode));
		Map<String,String> params=new HashMap<String,String>();
		params.put("username",operator.getCode());
		params.put("password", DEFAULT_CHAT_PASSWORD);
		params.put("token",token);
		String json=RequestUtil.request(partner.getHostname()+"/partner/bank/bank/partner/operator/reg_chat", params);
		operatorRepository.save(operator);
	}
	private class RegChatResult{
		private int code;
		private String depict;
		private Object data;
	}
	/**
	 * 新编码生成器
	 * @param maxCode
	 * @return
	 */
	private static String genCode(String maxCode){
		String code="";
		if(StringUtils.isEmpty(maxCode)){
			code="0001";
		}else{
			maxCode=maxCode.substring(6,maxCode.length());
			int icode=Integer.parseInt(maxCode)+1;
			if(icode/10000<1){
				code=""+icode;
			}
			if(icode/1000<1){
				code="0"+icode;
			}
			if(icode/100<1){
				code="00"+icode;
			}
			if(icode/10<1){
				code="000"+icode;
			}
		}
		return code;
	}
	@Override
	public long count() {
		long count=operatorRepository.countByIsdel(0);
		return count;
	}
	
	@Override
	public List<Operator> list(int page, int size) {
		List<Operator> operators=operatorRepository.findByIsdel(0,new PageRequest(page, size)).getContent();
		return operators;
	}
	
	@Override
	public List<Operator> listByParid(long parid) {
		List<Operator> operators=operatorRepository.findByParidAndIsdel(parid,0);
		return operators;
	}
	
	@Override
	public void updByOpeidIsenable(long opeid) {
		Operator operator=operatorRepository.findOne(opeid);
		if(StringUtils.isEmpty(operator)){
			return;
		}
		int isenable=operator.getIsenable();
		if(isenable==0){
			operator.setIsenable(1);
		}
		if(isenable==1){
			operator.setIsenable(0);
		}
		operatorRepository.saveAndFlush(operator);
	}

	@Override
	public void updByOpeidPassword(long opeid) {
		Operator operator=operatorRepository.findOne(opeid);
		if(StringUtils.isEmpty(operator)){
			return;
		}
		operator.setPassword(Md5Util.md5(DEFAULT_PASSWORD));
		operatorRepository.saveAndFlush(operator);
	}

	@Override
	public void delByOpeid(long opeid) {
		Operator operator=operatorRepository.findOne(opeid);
		if(StringUtils.isEmpty(operator)){
			return;
		}
		operator.setIsdel(1);
		operatorRepository.saveAndFlush(operator);
	}

	@Override
	public void updByOpeid(Operator operator) {
		Operator operatorPO=operatorRepository.findOne(operator.getOpeid());
		if(StringUtils.isEmpty(operatorPO)){
			return;
		}
		operatorPO.setSurname(operator.getSurname());
		operatorPO.setIsadmin(operator.getIsadmin());
		operatorRepository.saveAndFlush(operatorPO);
	}
	
	@Override
	public String login(String username, String password) {
		Operator operator=operatorRepository.findByUsernameAndPassword(username,Md5Util.md5(password));
		if(StringUtils.isEmpty(operator)){
			return null;
		}
		if(operator.getIsenable()==0){//账户禁用
			return null;
		}
		Map<String,Object> headerParams=new HashMap<String,Object>();
		headerParams.put("alg", "HS256");
		headerParams.put("type", "jwt");
		Map<String,Object> bodyParams=new HashMap<String,Object>();
		bodyParams.put("userid",operator.getOpeid());
		String token =JwtUtil.tokenGenerator(headerParams, bodyParams);
		return token;
	}
	
	@Override
	public void updByUsernamePassword(String username, String password) {
		Operator operator=operatorRepository.findByUsername(username);
		if(StringUtils.isEmpty(operator)){
			return;
		}
		operator.setPassword(Md5Util.md5(password));
		operatorRepository.saveAndFlush(operator);
		
	}
	
	@Override
	public Operator getByUsernamePassword(String username, String password) {
		Operator operator=operatorRepository.findByUsernameAndPassword(username, Md5Util.md5(password));
		return operator;
	}
	@Override
	public Operator getByOpeid(long opeid) {
		Operator operator=operatorRepository.findOne(opeid);
		return operator;
	}

	@Override
	public List<Operator> listByBanidAndIsadmin(long banid, int isadmin) {
		List<Operator> operators=operatorRepository.findByBanidAndIsadmin(banid, isadmin);
		return operators;
	}
	
	@Override
	public void updIslistenByOpeid(long opeid) {
		Operator operator=operatorRepository.findOne(opeid);
		if(operator.getIsadmin()==0){
			return;
		}
		if(operator.getIslisten()==0){
			operator.setIslisten(1);
		}else{
			operator.setIslisten(0);
		}
		Listen listen=new Listen();
		listen.setOpeid(opeid);
		listen.setIslisten(operator.getIslisten());
		listen.setTime(System.currentTimeMillis());
		listenRepository.save(listen);
		operatorRepository.saveAndFlush(operator);
	}
}
