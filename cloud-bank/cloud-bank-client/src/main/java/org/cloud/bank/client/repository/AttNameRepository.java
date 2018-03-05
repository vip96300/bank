package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.AttName;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttNameRepository extends JpaRepository<AttName, Long> {
	
	public List<AttName> findByIsdelOrderBySort(int isdel);
	
	public List<AttName> findByMenidAndIsdelOrderBySort(long menid,int isdel,Sort sort);
	
	public AttName findByMenidAndName(long menid,String name);
	
	public List<AttName> findByIsgetAndIsdel(int isget,int isdel);
	
	public List<AttName> findByNameAndIsdel(String name,int isdel);
	
	public List<AttName> findByMenidAndIsdelOrderBySort(long menid,int isdel);
}
