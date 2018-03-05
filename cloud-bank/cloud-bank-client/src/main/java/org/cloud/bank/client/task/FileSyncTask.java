package org.cloud.bank.client.task;

import java.io.File;
import java.util.List;











import org.cloud.bank.client.model.Archive;
import org.cloud.bank.client.model.Business;
import org.cloud.bank.client.model.Bustemp;
import org.cloud.bank.client.model.Classify;
import org.cloud.bank.client.repository.ArchiveRepository;
import org.cloud.bank.client.repository.BusinessRepository;
import org.cloud.bank.client.repository.BustempRepository;
import org.cloud.bank.client.repository.ClassifyRepository;
import org.cloud.bank.client.util.FileUtils;
import org.cloud.bank.client.util.PinyinUtils;
import org.cloud.bank.client.util.RequestUtil;
import org.cloud.bank.client.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * 
* @ClassName: FileSyncTask
* @Description: 
* @author huanghongfei
* @date 2017年9月15日 上午11:04:34
*
 */
@Component
@EnableScheduling
public class FileSyncTask {
	
	private static final Logger log=LoggerFactory.getLogger(FileSyncTask.class);
	
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private BustempRepository bustempRepository;
	@Autowired
	private ClassifyRepository classifyRepository;
	@Autowired
	private ArchiveRepository archiveRepository;
	/**
	 * 从合作商同步业务状态为放款资料为已提交的业务文件
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void fileSync(){
		this.sync();	
	}
	
	public void sync(){
		int page=0;
		int size=100;
		List<Classify> classifys=classifyRepository.findByStepAndIsdelOrderByClaid(Classify.STEP2,0);
		if(classifys.isEmpty()){
			throw new RuntimeException("no classify");
		}
		while(true){
			//不管文件是否同步，为防止签约通过而没有导入txt就已经点击文件同步的问题
			List<Business> businesss=businessRepository.findByStateAndIssyncAndIsdel(Business.STATE_STEP1_7,0,0,new PageRequest(page, size)).getContent();
			for(Business business:businesss){
				business.setIssync(1);
				Bustemp bustemp=bustempRepository.findByCode(business.getCode());
				if(StringUtils.isEmpty(bustemp)){
					continue;
				}
				if(StringUtils.isEmpty(bustemp.getUrls())){
					continue;
				}
				String []urls=bustemp.getUrls().split(",");
				for(String url:urls){
					String localpath="file/"+business.getCode()+"/"+PinyinUtils.getPingYin(classifys.get(0).getName())+"/";
					this.mkdirs(File.listRoots()[0].getPath()+localpath);
					String fileName=UuidUtil.uuidBuilder();
					RequestUtil.download(url, new File(File.listRoots()[0].getPath()+localpath+fileName));
					//创建文档保存数据库
					Archive archive=Archive.getInstance();
					archive.setBusid(business.getBusid());
					archive.setClaid(classifys.get(0).getClaid());
					archive.setContent(localpath+fileName);
					archive.setPosition(fileName);
					archive.setStep(classifys.get(0).getStep());
					archive.setTime(System.currentTimeMillis());
					archive.setType(0);
					archiveRepository.save(archive);
				}
				log.info("{} sync filish",bustemp.getCode());
				bustemp.setUrls("");//消掉urls
				bustempRepository.saveAndFlush(bustemp);
			}
			businessRepository.save(businesss);
			if(businesss.size()<size){
				break;
			}
			page++;
		}
	}
	
	/**
	 * 创建文件夹
	 * @param path
	 */
	private void mkdirs(String path){
		File file=new File(path);
		file.mkdirs();
	}

}
