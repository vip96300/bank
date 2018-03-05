package org.cloud.bank.client.service.impl;

import java.util.List;

import org.cloud.bank.client.exception.IllegalBusinessStateException;
import org.cloud.bank.client.model.Archive;
import org.cloud.bank.client.model.Business;
import org.cloud.bank.client.model.Classify;
import org.cloud.bank.client.model.Partner;
import org.cloud.bank.client.repository.ArchiveRepository;
import org.cloud.bank.client.repository.BusinessRepository;
import org.cloud.bank.client.repository.ClassifyRepository;
import org.cloud.bank.client.repository.PartnerRepository;
import org.cloud.bank.client.service.ArchiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class ArchiveServiceImpl implements ArchiveService{

	@Autowired
	private ArchiveRepository archiveRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private ClassifyRepository classifyRepository;
	@Autowired
	private PartnerRepository partnerRepository;
	
	private static final Logger log=LoggerFactory.getLogger(ArchiveServiceImpl.class);
	
	@Override
	public void add(Archive archive) {
		archive.setPosition("unknown");
		archive.setTime(System.currentTimeMillis());
		archiveRepository.save(archive);
	}
	@Override
	public void add_batch(String archivesJSON) {
		List<Archive> archives =new Gson().fromJson(archivesJSON,new TypeToken<List<Archive>>(){}.getType()); 
		if(archives.isEmpty()){
			throw new RuntimeException("archives can't be null");
		}
		Business business=businessRepository.findOne(archives.get(0).getBusid());
		if(StringUtils.isEmpty(business)){
			throw new RuntimeException("business cant be null");
		}
		if(business.getState()!=Business.STATE_1&&business.getState()!=Business.STATE_4){
			//如果不是待提交或驳回修改状态，不可修改资料
			throw new RuntimeException("the business state cant update archives");
		}
		//删除该业务的所有资料
		Sort sort=new Sort(Direction.ASC,"time");
		List<Archive> oldArchives=archiveRepository.findByBusidAndStep(business.getBusid(),Classify.STEP0,sort);
		if(!oldArchives.isEmpty()){
			archiveRepository.delete(oldArchives);
		}
		archives.forEach(archive -> {
			archive.setStep(Classify.STEP0);
			archive.setTime(System.currentTimeMillis());
		});
		business.setState(Business.STATE_2);
		business.setPstate(Business.STATE_2);
		archiveRepository.save(archives);
		businessRepository.saveAndFlush(business);
	}
	@Override
	public void add_batch_step1(String archivesJSON) throws IllegalBusinessStateException {
		log.debug("archivesJSON={}",archivesJSON);
		List<Archive> archives = new Gson().fromJson(archivesJSON,new TypeToken<List<Archive>>(){}.getType()); 
		if(archives.isEmpty()){
			throw new RuntimeException("archives can't be null");
		}
		Business business=businessRepository.findOne(archives.get(0).getBusid());
		if(business.getState()!=Business.STATE_3&&business.getState()!=Business.STATE_STEP1_8){
			//如果业务状态不是初审通过（面签待提交），并且也不是面签驳回状态，
			throw new RuntimeException("the state cant update archives");
		}
		//删除该业务的所有资料
		Sort sort=new Sort(Direction.ASC,"time");
		List<Archive> oldArchives=archiveRepository.findByBusidAndStep(business.getBusid(),Classify.STEP1,sort);
		if(!oldArchives.isEmpty()){
			archiveRepository.delete(oldArchives);
		}
		archives.forEach(archive -> {
			archive.setStep(Classify.STEP1);
			archive.setTime(System.currentTimeMillis());
		});
		business.setState(Business.STATE_STEP1_6);
		archiveRepository.save(archives);
		businessRepository.saveAndFlush(business);
	}
	@Override
	public void add_batch_step3(String archivesJSON) {
		log.debug("archivesJSON={}",archivesJSON);
		List<Archive> archives = new Gson().fromJson(archivesJSON,new TypeToken<List<Archive>>(){}.getType()); 
		if(archives.isEmpty()){
			throw new RuntimeException("archives can't be null");
		}
		Business business=businessRepository.findOne(archives.get(0).getBusid());
		if(business.getIsimgerr()!=1){//如果不是影像驳回
			throw new RuntimeException("the business cant add archive,isimgerr <> 1");
		}
		//删除该业务的所有资料
		Sort sort=new Sort(Direction.ASC,"time");
		List<Archive> oldArchives=archiveRepository.findByBusidAndStep(business.getBusid(),Classify.STEP3,sort);
		if(!oldArchives.isEmpty()){
			archiveRepository.delete(oldArchives);
		}
		archives.forEach(archive -> {
			archive.setStep(Classify.STEP3);
			archive.setTime(System.currentTimeMillis());
		});
		business.setIsimgerr(0);
		business.setState(Business.STATE_STEP1_7);
		archiveRepository.save(archives);
		businessRepository.saveAndFlush(business);
	}
	
	@Override
	public List<Archive> listByBusidStep(long busid, int step) {
		Sort sort=new Sort(Direction.ASC,"time");
		List<Archive> archives=archiveRepository.findByBusidAndStep(busid,step,sort);
		return archives;
	}
	
	@Override
	public List<Archive> listByBusid(long busid) {
		Sort sort=new Sort(Direction.ASC,"time");
		List<Archive> archives=archiveRepository.findByBusid(busid,sort);
		return archives;
	}
	

}
