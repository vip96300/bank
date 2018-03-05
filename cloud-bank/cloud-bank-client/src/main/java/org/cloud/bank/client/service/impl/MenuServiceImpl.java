package org.cloud.bank.client.service.impl;

import java.util.List;

import org.cloud.bank.client.model.Menu;
import org.cloud.bank.client.repository.MenuRepository;
import org.cloud.bank.client.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Menu> listByPid(long pid) {
		List<Menu> menus=menuRepository.findByPidAndIsdelAndMenidNotOrderByTime(pid,0,DatumServiceImpl.MENID_53);
		return menus;
	}

	@Override
	public Menu getByMenid(long menid) {
		Menu menu=menuRepository.findOne(menid);
		return menu;
	}

	@Override
	public void add(Menu menu) {
		menu.setTime(System.currentTimeMillis());
		menu.setIsdel(0);
		menuRepository.save(menu);
	}

	@Override
	public void upd(Menu menu) {
		Menu menuPO=menuRepository.findOne(menu.getMenid());
		if(StringUtils.isEmpty(menuPO)){
			throw new RuntimeException("the menu is null");
		}
		menuPO.setName(menu.getName());
		menuRepository.saveAndFlush(menuPO);
	}

	@Override
	public void delByMenid(long menid) {
		Menu menu=menuRepository.findOne(menid);
		menu.setIsdel(1);
		menuRepository.saveAndFlush(menu);
	}

	@Override
	public List<Menu> listByIssku() {
		List<Menu> menus=menuRepository.findByIsskuAndIsdelOrderByMenid(1, 0);
		return menus;
	}


}
