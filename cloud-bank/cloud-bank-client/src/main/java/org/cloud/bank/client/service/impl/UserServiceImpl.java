package org.cloud.bank.client.service.impl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.bank.client.model.Bank;
import org.cloud.bank.client.model.User;
import org.cloud.bank.client.repository.BankRepository;
import org.cloud.bank.client.repository.UserRepository;
import org.cloud.bank.client.service.UserService;
import org.cloud.bank.client.util.JwtUtil;
import org.cloud.bank.client.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BankRepository bankRepository;
	
	private static final String PASSWORD_DEFAULT="123456";

	@Override
	public String login(String username, String password) {
		User user=userRepository.findByUsernameAndPassword(username, Md5Util.md5(password));
		if(StringUtils.isEmpty(user)){
			return null;
		}
		if(user.getIsenable()==0){//账户禁用
			return null;
		}
		Map<String,Object> headerParams=new HashMap<String,Object>();
		headerParams.put("alg", "HS256");
		headerParams.put("type", "jwt");
		Map<String,Object> bodyParams=new HashMap<String,Object>();
		bodyParams.put("userid",user.getUseid());
		String token =JwtUtil.tokenGenerator(headerParams, bodyParams);
		return token;
	}

	@Override
	public List<User> list(int page, int size) {
		List<User> users = userRepository.findAll(new PageRequest(page, size)).getContent();
		return users;
	}

	@Override
	public User getByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}

	@Override
	public long count() {
		long count=userRepository.count();
		return count;
	}

	@Override
	public void updByUseidIsenable(long useid) {
		User user=userRepository.findOne(useid);
		if(StringUtils.isEmpty(user)){
			return;
		}
		int isenable=user.getIsenable();
		if(isenable==0){
			user.setIsenable(1);
		}
		if(isenable==1){
			user.setIsenable(0);
		}
		userRepository.saveAndFlush(user);
	}

	@Override
	public void updByUseidPassword(long useid) {
		User user=userRepository.findOne(useid);
		if(StringUtils.isEmpty(user)){
			return;
		}
		user.setPassword(Md5Util.md5(PASSWORD_DEFAULT));
		userRepository.saveAndFlush(user);
	}

	@Override
	public void delByUseid(long useid) {
		userRepository.delete(useid);
	}

	@Override
	public void add(User user) {
		Bank bank=bankRepository.findOne(user.getBanid());
		if(StringUtils.isEmpty(bank)){
			return;
		}
		user.setPassword(Md5Util.md5(PASSWORD_DEFAULT));
		user.setIsenable(1);
		String maxCode=userRepository.findByCodeMax(bank.getCode());
		user.setCode(bank.getCode()+genCode(maxCode));
		user.setBankname(bank.getName());
		user.setLevel(1);
		userRepository.save(user);
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
			maxCode=maxCode.substring(3,maxCode.length());
			if(StringUtils.isEmpty(maxCode)){//前期的code只有3位，下标3开始可能会空
				maxCode="0";
			}
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
	public void updByUseid(User user) throws SQLIntegrityConstraintViolationException{
		User userPO=userRepository.findOne(user.getUseid());
		if(StringUtils.isEmpty(userPO)){
			return;
		}
		userPO.setSurname(user.getSurname());
		userPO.setUsername(user.getUsername());
		try {
			userRepository.saveAndFlush(userPO);
		} catch (Exception e) {
			throw new SQLIntegrityConstraintViolationException();
		}
	}

	@Override
	public User getByUsernamePassword(String username, String password) {
		password=Md5Util.md5(password);
		User user=userRepository.findByUsernameAndPassword(username,password);
		return user;
	}

	@Override
	public void updByUsernamePassword(String username, String password) {
		User user=userRepository.findByUsername(username);
		if(StringUtils.isEmpty(user)){
			throw new RuntimeException("user not exist");
		}
		user.setPassword(Md5Util.md5(password));
		userRepository.saveAndFlush(user);
	}

	@Override
	public User getByUseid(long useid) {
		User user=userRepository.findOne(useid);
		return user;
	}

}
