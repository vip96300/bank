package org.cloud.bank.client.service.impl;

import java.util.List;

import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.model.AttValue;
import org.cloud.bank.client.repository.AttNameRepository;
import org.cloud.bank.client.repository.AttValueRepository;
import org.cloud.bank.client.service.AttValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;


@Service
public class AttValueServiceImpl implements AttValueService{

	@Autowired
	private AttValueRepository attValueRepository;
	@Autowired
	private AttNameRepository attNameRepository;
	
	@Override
	public List<AttValue> listByNamid(long namid) {
		Sort sort=new Sort(Direction.ASC,"time");
		List<AttValue> attValues=attValueRepository.findByNamidAndIsdel(namid, 0,sort);
		return attValues;
	}

	@Override
	public void add(AttValue attValue) {
		AttName attName=attNameRepository.findOne(attValue.getNamid());
		if(attValue.getValue().getBytes().length>attName.getLength()){
			throw new RuntimeException("attvalue.bytes.length exceed attname.length");
		}
		attValue.setTime(System.currentTimeMillis());
		attValue.setIsdel(0);
		attValueRepository.save(attValue);
	}

	@Override
	public void delByValid(long valid) {
		AttValue attValue=attValueRepository.findOne(valid);
		attValue.setIsdel(1);
		attValueRepository.saveAndFlush(attValue);
	}

	@Override
	public List<AttValue> list() {
		List<AttValue> attValues=attValueRepository.findByIsdel(0);
		return attValues;
	}

	@Override
	public List<AttValue> listByFkvalue(String fkvalue) {
		List<AttValue> attValues=attValueRepository.findByFkvalueAndIsdel(fkvalue, 0);
		return attValues;
	}


}
