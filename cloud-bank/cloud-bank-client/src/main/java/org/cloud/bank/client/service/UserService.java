package org.cloud.bank.client.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.cloud.bank.client.model.User;

public interface UserService {
	
	public String login(String username,String password);
	
	public User getByUsername(String username);
	
	public long count();
	
	public List<User> list(int page,int size);
	
	public void updByUseidIsenable(long useid);
	
	public void updByUseidPassword(long useid);
	
	public void delByUseid(long useid);
	
	public void add(User user);
	
	public void updByUseid(User user) throws SQLIntegrityConstraintViolationException;
	
	public User getByUsernamePassword(String username,String password);
	
	public void updByUsernamePassword(String username,String password);
	
	public User getByUseid(long useid);
}
