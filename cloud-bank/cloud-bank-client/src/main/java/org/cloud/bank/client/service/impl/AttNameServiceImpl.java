package org.cloud.bank.client.service.impl;

import java.util.List;

import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.repository.AttNameRepository;
import org.cloud.bank.client.service.AttNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;


@Service
public class AttNameServiceImpl implements AttNameService{

	@Autowired
	private AttNameRepository attNameRepository;
	
	@Override
	public List<AttName> listByMenid(long menid) {
		Sort sort=new Sort(Direction.ASC,"time");
		List<AttName> attNames=attNameRepository.findByMenidAndIsdelOrderBySort(menid,0,sort);
		return attNames;
	}

	@Override
	public void add(AttName attName) {
		attName.setTime(System.currentTimeMillis());
		attName.setIsdel(0);
		attName.setIsexport(1);
		attName.setIscover(0);
		if(attName.getDefvalue().getBytes().length>attName.getLength()){
			throw new RuntimeException("defvalue.bytes.length exceed attname.length");
		}
		attName.setSort(System.nanoTime());
		attNameRepository.save(attName);
	}

	@Override
	public AttName getByNamid(long namid) {
		AttName attName=attNameRepository.findOne(namid);
		return attName;
	}

	@Override
	public void upd(AttName attName) {
		attNameRepository.saveAndFlush(attName);
	}

	@Override
	public void delByNamid(long namid) {
		AttName attName=attNameRepository.findOne(namid);
		attName.setIsdel(1);
		attNameRepository.saveAndFlush(attName);
	}

	@Override
	public List<AttName> listByIsget() {
		List<AttName> attNames=attNameRepository.findByIsgetAndIsdel( 1, 0);
		return attNames;
	}


}
