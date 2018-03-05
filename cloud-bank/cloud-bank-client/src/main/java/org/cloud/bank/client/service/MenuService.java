package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.model.Menu;

public interface MenuService {

	public List<Menu> listByPid(long pid);
	
	public Menu getByMenid(long menid);
	
	public void add(Menu menu);
	
	public void upd(Menu menu);
	
	public void delByMenid(long menid);
	
	public List<Menu> listByIssku();
}
