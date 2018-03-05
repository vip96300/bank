package org.cloud.bank.client.service.impl;

import java.util.List;

import org.cloud.bank.client.model.Classify;
import org.cloud.bank.client.repository.ClassifyRepository;
import org.cloud.bank.client.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ClassifyServiceImpl implements ClassifyService{
	
	@Autowired
	private ClassifyRepository classifyRepository;
	@Override
	public void add(Classify classify) {
		classify.setIsdel(0);
		classify.setTime(System.currentTimeMillis());
		classifyRepository.save(classify);
	}

	@Override
	public void updByClaid(Classify classify) {
		Classify classifyPO=classifyRepository.findOne(classify.getClaid());
		if(StringUtils.isEmpty(classifyPO)){
			return;
		}
		classifyPO.setName(classify.getName());
		classifyPO.setStep(classify.getStep());
		classifyPO.setIsrequired(classify.getIsrequired());
		classifyRepository.saveAndFlush(classifyPO);
	}
	
	@Override
	public List<Classify> list() {
		Sort sort=new Sort(Direction.ASC,"step");
		List<Classify> classifys=classifyRepository.findAll(sort);
		return classifys;
	}
	
	@Override
	public List<Classify> listByStep(int step) {
		List<Classify> classifys=classifyRepository.findByStepAndIsdelOrderByClaid(step,0);
		return classifys;
	}

	@Override
	public void updIsdelByClaid(long claid) {
		Classify classify=classifyRepository.findOne(claid);
		if(StringUtils.isEmpty(classify)){
			throw new RuntimeException("classify is null");
		}
		if(classify.getIsdel().intValue()==0){
			classify.setIsdel(1);
		}else{
			classify.setIsdel(0);
		}
		classifyRepository.saveAndFlush(classify);
	}

	
	
}
