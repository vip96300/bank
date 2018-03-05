package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	public List<Menu> findByPidAndIsdelAndMenidNotOrderByTime(long pid,int isdel,long menid);
	
	public List<Menu> findByIsskuAndIsdelOrderByMenid(int issku,int isdel);
	
}
