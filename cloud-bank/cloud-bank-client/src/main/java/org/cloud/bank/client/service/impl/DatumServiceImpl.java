package org.cloud.bank.client.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;








import org.cloud.bank.client.config.CharSet;
import org.cloud.bank.client.exception.IllegalDatumValueException;
import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.model.AttValue;
import org.cloud.bank.client.model.Bank;
import org.cloud.bank.client.model.Business;
import org.cloud.bank.client.model.Datum;
import org.cloud.bank.client.model.Menu;
import org.cloud.bank.client.model.OutQueue;
import org.cloud.bank.client.model.Partner;
import org.cloud.bank.client.model.Verify;
import org.cloud.bank.client.repository.AttNameRepository;
import org.cloud.bank.client.repository.AttValueRepository;
import org.cloud.bank.client.repository.BankRepository;
import org.cloud.bank.client.repository.BusinessRepository;
import org.cloud.bank.client.repository.DatumRepository;
import org.cloud.bank.client.repository.MenuRepository;
import org.cloud.bank.client.repository.OutQueueRepository;
import org.cloud.bank.client.repository.PartnerRepository;
import org.cloud.bank.client.repository.UserRepository;
import org.cloud.bank.client.repository.VerifyRepository;
import org.cloud.bank.client.service.DatumService;
import org.cloud.bank.client.thread.ThreadPool;
import org.cloud.bank.client.util.ErrorinfoUtils;
import org.cloud.bank.client.util.PinyinUtils;
import org.cloud.bank.client.util.RegUtil;
import org.cloud.bank.client.util.RequestUtil;
import org.cloud.bank.client.util.TxtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;








import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Service
public class DatumServiceImpl implements DatumService{
	
	private static final Logger log=LoggerFactory.getLogger(DatumServiceImpl.class);

	@Autowired
	private DatumRepository datumRepository;
	@Autowired
	private AttNameRepository attNameRepository;
	@Autowired
	private AttValueRepository attValueRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private PartnerRepository partnerRepository;
	@Autowired
	private OutQueueRepository outQueueRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerifyRepository verifyRepository;
	@Autowired
	private BankRepository bankRepository;
	
	/**
	 * 卡信息
	 */
	public static final long MENID_50=50;
	/**
	 * 客户信息
	 */
	public static final long MENID_51=51;
	/**
	 * 影像信息
	 */
	public static final long MENID_53=53;
	/**
	 * 关联信息
	 */
	public static final long MENID_100=100;
	/**
	 * 分期信息
	 */
	public static final long MENID_101=101;
	
	
	@Override
	public Map<String,List> mapByBusidAndMenid(long busid, long menid) {
		List<Datum> datums=datumRepository.findByBusidAndMenidOrderBySort(busid, menid);
		List<Long> namids=new ArrayList<Long>();
		datums.forEach(datum->{
			if(datum.getIntype()!=AttName.INTYPE_INPUT){
				namids.add(datum.getNamid());
			}
		});
		List<AttValue> attValues=null;
		if(!namids.isEmpty()){
			attValues=attValueRepository.findByNamidInAndIsdelOrderByValue(namids,0);
		}
		Map<String,List> map=new LinkedHashMap<String, List>();
		for(Datum datum:datums){
			List<AttValue> attValuesByDatum=new ArrayList<AttValue>();
			attValues.forEach(attValue->{
				if(attValue.getNamid().longValue()==datum.getNamid().longValue()){
					attValuesByDatum.add(attValue);
				}
			});
			String datumJSON=new GsonBuilder().serializeNulls().create().toJson(datum);
			map.put(datumJSON, attValuesByDatum);
		}
		return map;
	}

	@Override
	public Map<String, List> listByBusidAndMenidAndIsget(long busid,long menid, int isget) {
		List<Datum> datums=datumRepository.findByBusidAndMenidAndIsgetOrderBySort(busid, menid,isget);
		List<Long> namids=new ArrayList<Long>();
		datums.forEach(datum->{
			if(datum.getIntype()!=AttName.INTYPE_INPUT){
				namids.add(datum.getNamid());
			}
		});
		List<AttValue> attValues=null;
		if(!namids.isEmpty()){
			attValues=attValueRepository.findByNamidInAndIsdelOrderByValue(namids,0);
		}
		Map<String,List> map=new LinkedHashMap<String, List>();
		for(Datum datum:datums){
			List<AttValue> attValuesByDatum=new ArrayList<AttValue>();
			if(menid==DatumServiceImpl.MENID_51&&datum.getName().equals("AGNBRNO")){
				Business business=businessRepository.findOne(busid);
				Bank bank1=bankRepository.findOne(business.getBanid());
				List<Bank> banks=bankRepository.findByBcodeAndIsdel(bank1.getBcode(),0);//获取同支行的网点号
				attValues.forEach(attValue->{
					for(Bank bank:banks){
						if(attValue.getNamid().longValue()==datum.getNamid().longValue()&&attValue.getValue().equals(bank.getWcode())){
							attValuesByDatum.add(attValue);
						}
					}
				});
			}else{
				attValues.forEach(attValue->{
					if(attValue.getNamid().longValue()==datum.getNamid().longValue()){
						attValuesByDatum.add(attValue);
					}
				});
			}
			String datumJSON=new GsonBuilder().serializeNulls().create().toJson(datum);
			map.put(datumJSON, attValuesByDatum);
		}
		if(menid==DatumServiceImpl.MENID_100){//关联信息排序
			map=new LinkedHashMap<String, List>();
			int RELCUSTNUM=Integer.valueOf(datumRepository.findByBusidAndName(busid,"RELCUSTNUM").getValue());//关联信息条数
			for(int i=0;i<RELCUSTNUM;i++){
				for(Datum datum:datums){
					int suffix=Integer.valueOf(datum.getName().split("_")[1]);
					if(i!=suffix){
						continue;
					}
					List<AttValue> attValuesByDatum=new ArrayList<AttValue>();
					attValues.forEach(attValue->{
						if(attValue.getNamid().longValue()==datum.getNamid().longValue()){
							attValuesByDatum.add(attValue);
						}
					});
					String datumJSON=new GsonBuilder().serializeNulls().create().toJson(datum);
					map.put(datumJSON, attValuesByDatum);
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, List> listByBusidAndMenidAndIsrequired(long busid,long menid, int isrequired) {
		Map<String,List> map=new LinkedHashMap<String, List>();
		List<Datum> datums=datumRepository.findByBusidAndMenidAndIsrequiredOrderBySort(busid, menid,isrequired);
		List<Long> namids=new ArrayList<Long>();
		datums.forEach(datum->{
			if(datum.getIntype()!=AttName.INTYPE_INPUT){
				namids.add(datum.getNamid());
			}
		});
		List<AttValue> attValues=new ArrayList<AttValue>();
		if(!namids.isEmpty()){
			attValues=attValueRepository.findByNamidInAndIsdelOrderByValue(new ArrayList<Long>(new HashSet<Long>(namids)),0);
		}
		for(Datum datum:datums){
			List<AttValue> attValuesByDatum=new ArrayList<AttValue>();
			if(menid==DatumServiceImpl.MENID_51&&datum.getName().equals("AGNBRNO")){
				Business business=businessRepository.findOne(busid);
				Bank bank1=bankRepository.findOne(business.getBanid());
				List<Bank> banks=bankRepository.findByBcodeAndIsdel(bank1.getBcode(),0);//获取同支行的网点号
				attValues.forEach(attValue->{
					for(Bank bank:banks){
						if(attValue.getNamid().longValue()==datum.getNamid().longValue()&&attValue.getValue().equals(bank.getWcode())){
							attValuesByDatum.add(attValue);
						}
					}
				});
			}else{
				attValues.forEach(attValue->{
					if(attValue.getNamid().longValue()==datum.getNamid().longValue()){
						attValuesByDatum.add(attValue);
					}
				});	
			}
			String datumJSON=new GsonBuilder().serializeNulls().create().toJson(datum);
			map.put(datumJSON, attValuesByDatum);
		}
		if(menid==DatumServiceImpl.MENID_100){//关联信息排序
			map=new LinkedHashMap<String, List>();
			int RELCUSTNUM=Integer.valueOf(datumRepository.findByBusidAndName(busid,"RELCUSTNUM").getValue());//关联信息条数
			for(int i=0;i<RELCUSTNUM;i++){
				for(Datum datum:datums){
					int suffix=Integer.valueOf(datum.getName().split("_")[1]);
					if(i!=suffix){
						continue;
					}
					List<AttValue> attValuesByDatum=new ArrayList<AttValue>();
					attValues.forEach(attValue->{
						if(attValue.getNamid().longValue()==datum.getNamid().longValue()){
							attValuesByDatum.add(attValue);
						}
					});
					String datumJSON=new GsonBuilder().serializeNulls().create().toJson(datum);
					map.put(datumJSON, attValuesByDatum);
				}
			}
		}
		return map;
	}
	
	@Override
	public void updByBatch(long busid, long menid, List<Datum> datums) throws IllegalDatumValueException{
		Business business=businessRepository.getOne(busid);
		if(business.getState()!=Business.STATE_STEP1_7&&business.getState()!=Business.STATE_STEP2_12){
			return;
		}
		List<Datum> datumsPO=datumRepository.findByBusidAndMenidAndIsgetOrderBySort(busid, menid, 1);
		for(Datum datumPO:datumsPO){
			for(Datum datum:datums){
				if(datumPO.getDatid().longValue()==datum.getDatid().longValue()){
					if(StringUtils.isEmpty(datum.getValue().trim())){
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
					int length=0;
					try {
						length=datum.getValue().getBytes(CharSet.GBK).length;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(length>datumPO.getLength()){
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
					if(!StringUtils.isEmpty(datum.getValue())&&!StringUtils.isEmpty(datumPO.getReg())){
						if(!RegUtil.match(datumPO.getReg(),datum.getValue())){//值非空，左补位，正则非空
							throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
						}
					}
					datumPO.setValue(datum.getValue().trim());
					datumPO.setValuedepict(datum.getValuedepict().trim());
				}
			}
		}
		datumRepository.save(datumsPO);
		//business.setState(Business.STATE_STEP2_10);//不能设置状态，下次将不能再保存
		businessRepository.saveAndFlush(business);
		if(menid!=DatumServiceImpl.MENID_101){
			return;
		}
		ThreadPool.getThreadPoolExecutor().execute(new IniRelInfo(business.getBusid()));
	}
	
	@Override
	public void updByBatchBank(long busid, long menid, List<Datum> datums) throws IllegalDatumValueException{
		Business business=businessRepository.findOne(busid);
		List<Datum> datumsPO=datumRepository.findByBusidAndMenidOrderBySort(busid, menid);
		for(Datum datumPO:datumsPO){
			for(Datum datum:datums){
				if(datumPO.getDatid().longValue()==datum.getDatid().longValue()){
					if(datumPO.getIsrequired()!=0&&StringUtils.isEmpty(datum.getValue())){
						throw new IllegalDatumValueException(datumPO.getNamedepict()+"_"+datum.getDatid());
					}
					int length=0;
					try {
						length=datum.getValue().getBytes(CharSet.GBK).length;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(length>datumPO.getLength()){
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
					if(!StringUtils.isEmpty(datum.getValue())&&!StringUtils.isEmpty(datumPO.getReg())){
						if(!RegUtil.match(datumPO.getReg(),datum.getValue())){//值非空，左补位，正则非空
							throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
						}
					}
					datumPO.setValue(StringUtils.isEmpty(datum.getValue())?"":datum.getValue());
					datumPO.setValuedepict(datum.getValuedepict().trim());
				}
			}
		}
		//逻辑校验
		for(Datum datum:datumsPO){
			//-----------------------------------------------卡信息-----------------------------------------------------------
			if(datum.getName().equals("BRHNO")&&datum.getMenid().longValue()==MENID_50){//联行行号
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"CARDKIND");//申请卡种
				if("1".equals(datum1.getValue())){//准贷记卡必输，贷记卡不允许输入
					if(StringUtils.isEmpty(datum.getValue())){
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
				}
				if("7".equals(datum1.getValue())){
					datum.setValue("");
				}
			}
			/*if(datum.getName().equals("PWDRECEV")&&datum.getMenid().longValue()==MENID_50){//密码领取方式
				Datum datum1=this.findDatumByName(datumsPO,"PSWSETM");//密码设置方式
				if("0".equals(datum1.getValue())){//若密码设置方式(PSWSETM)是0-客户自留密码，则只允许输入2-自取,
					datum.setValue("2");
					datum1=this.findDatumByName(datumsPO,"PSWADDRF");//密码寄送地址类型
					//datum1.setValue("");//并且讲密码寄送地址类型设为空//此处不制空的原因是，如果导出文件时必须有值的字段为空不能导出，留到导出文件时设为空
					datumRepository.saveAndFlush(datum1);
				}
			}
			if(datum.getName().equals("PSWADDRF")&&datum.getMenid().longValue()==MENID_50){//密码寄送地址类型
				Datum datum1=this.findDatumByName(datumsPO,"PWDRECEV");//密码领取方式
				if("3".equals(datum1.getValue())){//若PWDRECEV取值为3(寄送)，则此项必须非空，且不允许指向地址内容为空；否则此项必须是空的
					datum.setValue("");
				}
			}*/
			if(datum.getName().equals("PSWLAMT")&&datum.getMenid().longValue()==MENID_50){//自定义消费输密限额
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"PSWFLAG");//自定义消费输密标志
				if("0".equals(datum1.getValue())){//“自定义消费输密标志”为0-输密时必输，否则不输。
					if(StringUtils.isEmpty(datum.getValue())){
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
				}else{
					datum.setValue("");
				}
			}
			if(datum.getName().equals("ACCADDRF")&&datum.getMenid().longValue()==MENID_50){//对帐单寄送地址
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "ACCGETM");//对帐单寄送方式
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){//寄送方式选择寄送时为必输项，不允许地址内容为空
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("DRAWMODE")&&datum.getMenid().longValue()==MENID_50){//卡片领取方式
				if("2".equals(datum.getValue())){//如果是寄送，将住宅地址同步至通讯地址
					
				}
			}
			if(datum.getName().equals("DRAWADDR")&&datum.getMenid().longValue()==MENID_50){//卡片寄送地址类型
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"DRAWMODE");//卡片领取方式
				if("1".equals(datum1.getValue())){//若DRAWMODE取值为1，则此项必须是空。否则，此项不能是空，且不允许指向地址内容为空。
					datum.setIsrequired(0);
					List<Datum> datums1=datumRepository.findByBusidAndNameLike(busid,"COMM");//通讯地址
					datums1.forEach(d->{
						d.setIsrequired(0);
						d.setIsget(0);
						d.setValue("");
					});
					datumRepository.save(datums1);
				}
				if("2".equals(datum1.getValue())){//如若DRAWMODE选2，则值填3，不要1、2，如若选1则字段为空		
					//datum.setValue("3");//导出时设为3
					datum.setIsrequired(1);
					List<Datum> datums1=datumRepository.findByBusidAndNameLike(busid,"COMM");
					datums1.forEach(d->{
						d.setIsrequired(1);
						d.setIsget(1);
					});
					datumRepository.save(datums1);
				}
			}
			if(datum.getName().equals("DEPSWLT1")&&datum.getMenid().longValue()==MENID_50){//副卡一自定义消费输密限额
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "DEPSWF1");//副卡一自定义消费输密标志
				if("0".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){//自定义消费输密标志为“输密”时是必输项。 
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("DEPSWLT2")&&datum.getMenid().longValue()==MENID_50){//副卡二自定义消费输密限额
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"DEPSWF2");//副卡二自定义消费输密标志
				if("0".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){//自定义消费输密标志为“输密”时是必输项。 
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("SCDMDU")&&datum.getMenid().longValue()==MENID_50){//发附属卡标志
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1-是，录入必须为2-否
					datum.setValue("2");
				}
			}
			if(datum.getName().equals("AUTHTYPE")&&datum.getMenid().longValue()==MENID_50){//授信类型,关系标志改为层级标志
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
				if("0".equals(datum.getValue())){//授信类型为0，关系标志必须要为2或者4，质押品编号，以及专项分期相关字段都必须为空。
					datum1=datumRepository.findByBusidAndName(busid,"RLTFLANG");//层级标志
					if(!datum1.getValue().equals("2")&&!datum1.getValue().equals("4")){
						datum1=datumRepository.findByBusidAndName(busid,"IMPNO");//质押品编号
						if(!StringUtils.isEmpty(datum1.getValue())){
							datum1.setValue("");
							datumRepository.saveAndFlush(datum1);
						}
					}
				}
				if(datum.getValue().equals("1")){//授信类型为1，关系标志必须要为1，质押品编号必须不为空，经销商信息，担保公司信息二者输其中一项以上。
					datum1=datumRepository.findByBusidAndName(busid,"RLTFLANG");//层级标志
					if(!datum1.getValue().equals("1")){
						datum1.setValue("1");
						datumRepository.saveAndFlush(datum1);
					}
				}
				if(datum.getValue().equals("2")){//授信类型为2，关系标志必须要为1，质押品编号必须为空，经销商信息必输
					datum1=datumRepository.findByBusidAndName(busid,"RLTFLANG");//层级标志
					if(!datum1.getValue().equals("1")){
						datum1.setValue("1");
						datumRepository.saveAndFlush(datum1);
					}
					datum1=datumRepository.findByBusidAndName(busid,"IMPNO");//质押品编号
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
					}
					datum1=DatumServiceImpl.findDatumByName(datumsPO,"INFO1");//经销商信息
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
					}
				}
				if(datum.getValue().equals("3")){//授信类型为3，关系标志必须要为1，质押品编号必须为空，担保公司信息必输
					datum1=datumRepository.findByBusidAndName(busid,"RLTFLANG");//层级标志
					if(!"1".equals(datum1.getValue())){
						datum1.setValue("1");
						datumRepository.saveAndFlush(datum1);
					}
					datum1=datumRepository.findByBusidAndName(busid,"IMPNO");//质押品编号
					if(!StringUtils.isEmpty(datum1.getValue())){
						datum1.setValue("");
						datumRepository.saveAndFlush(datum1);
					}
					datum1=DatumServiceImpl.findDatumByName(datumsPO, "INFO2");//担保公司信息
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
					}
				}
				if("4".equals(datum.getValue())){//授信类型为4，关系标志必须要为1，经销商信息，担保公司信息二者笔输其中一项以上
					datum1=datumRepository.findByBusidAndName(busid,"RLTFLANG");//层级标志
					if(!"1".equals(datum1.getValue())){
						datum1.setValue("1");
						datumRepository.saveAndFlush(datum1);
					}
					datum1=DatumServiceImpl.findDatumByName(datumsPO,"INFO1");//经销商信息
					if(StringUtils.isEmpty(datum1.getValue())){
						datum1=DatumServiceImpl.findDatumByName(datumsPO, "INFO2");//担保公司信息
						if(StringUtils.isEmpty(datum1.getValue())){
							throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
						}
					}
				}
				if("5".equals(datum.getValue())){//授信类型为5，关系标志必须要为1或者3，质押品编号必须为空
					datum1=datumRepository.findByBusidAndName(busid,"IMPNO");//质押品编号
					if(!StringUtils.isEmpty(datum1.getValue())){
						datum1.setValue("");
						datumRepository.saveAndFlush(datum1);
					}
					datum1=datumRepository.findByBusidAndName(busid,"RLTFLANG");//层级标志
					if(!"1".equals(datum1.getValue())&&!"3".equals(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
					}
				}
			}
			if(datum.getName().equals("SACCTYPE")&&datum.getMenid().longValue()==MENID_50){//特殊帐户控制标志
				/*Datum datum1=datumRepository.findByBusidAndName(busid,"CARDBIN");//申请卡BIN
				if(!StringUtils.isEmpty(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){//在卡BIN输入的时候，特殊帐户控制标志不允许为空。
					throw new IllegalDatumValueException(datum.getNamedepict());
				}*/
			}
			if(datum.getName().equals("INSTALNO")&&datum.getMenid().longValue()==MENID_50){//专项分期业务编号
				//注意:当专项分期业务编号不输入，担保类型，专项分期分类，专项分期期限，专项分期成数，押品金额必须输入，
				//当专项分期业务编号输入，忽略担保类型，专项分期分类，专项分期期限，专项分期成数，押品金额。
				/*if(StringUtils.isEmpty(datum.getValue())){
					Datum datum1=this.findDatumByName(datumsPO,"ASSURETYPE");//担保类型
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict());
					}
					datum1=this.findDatumByName(datumsPO,"CRTYPE");//专项分期分类
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict());
					}
					datum1=this.findDatumByName(datumsPO,"DEADLINE");//专项分期期限
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict());
					}
					datum1=this.findDatumByName(datumsPO,"PERCENT");//专项分期成数
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict());
					}
					datum1=this.findDatumByName(datumsPO,"IMPAMT");//押品金额
					if(StringUtils.isEmpty(datum1.getValue())){
						throw new IllegalDatumValueException(datum1.getNamedepict());
					}
				}*/
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//格式：ZXFQ(4位)+地区号(4位)+7位JULIA日期+顺序号(7位)
					//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
			}
			if(datum.getName().equals("ASSURETYPE")&&datum.getMenid().longValue()==MENID_50){//担保类型
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
				datum1=DatumServiceImpl.findDatumByName(datumsPO,"AUTHTYPE");//授权类型
				if("1".equals(datum1.getValue())){//授信类型为1-质押，担保类型必须为1-质押类
					datum.setValue("1");
				}
				if("2".equals(datum1.getValue())){//授信类型为2-抵押，担保类型必须为2-抵押类
					datum.setValue("2");
				}
				if("3".equals(datum1.getValue())){//授信类型为3-保证，担保类型必须为3-保证类
					datum.setValue("3");
				}
				if("4".equals(datum1.getValue())){//授信类型为4-组合及其他，担保类型必须为4-组合及其他
					datum.setValue("4");
				}
				if("5".equals(datum1.getValue())){//授信类型为5-不共享，担保类型必须为4-组合及其他
					datum.setValue("4");
				}
			}
			if(datum.getName().equals("CRTYPE")&&datum.getMenid().longValue()==MENID_50){//专项分期分类
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
			}
			if(datum.getName().equals("DEADLINE")&&datum.getMenid().longValue()==MENID_50){//专项分期期限
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
			}
			if(datum.getName().equals("PERCENT")&&datum.getMenid().longValue()==MENID_50){//专项分期成数
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
				if(!StringUtils.isEmpty(datum.getValue())){//当需要输入专项分期成数的时候，必须处于1至100之间
					try {
						if(Integer.valueOf(datum.getValue())<1||Integer.valueOf(datum.getValue())>100){
							throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
						}
					} catch (Exception e) {
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
				}
			}
			if(datum.getName().equals("INFO1")&&datum.getMenid().longValue()==MENID_50){//经销商信息
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
			}
			if(datum.getName().equals("INFO2")&&datum.getMenid().longValue()==MENID_50){//担保公司信息
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
			}
			if(datum.getName().equals("IMPAMT")&&datum.getMenid().longValue()==MENID_50){//押品金额
				Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"SIFLAG");//是否专项分期
				if("1".equals(datum1.getValue())){//当BFHUPPAC的是否专项分期为1是，不允许录入
					datum.setValue("");
				}
			}
			//---------------------------------------------------客户信息-------------------------------------------------------
			if(datum.getName().equals("AGNBRNO")&&datum.getMenid().longValue()==MENID_51){//受理网点号
				List<Datum> datums1=datumRepository.findByBusidAndNameLike(busid,"AGNBRNO");
				datums1.forEach(datum1->{
					datum1.setValue(datum.getValue());
				});
				datumRepository.save(datums1);
			}
			if(datum.getName().equals("RLTFLANG")&&datum.getMenid().longValue()==MENID_51){//层级标志
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"SIFLAG");//是否申请专项分期业务
				if("1".equals(datum1.getValue())){
					datum.setValue("1");
				}
			}
			if(datum.getName().equals("SEX")&&datum.getMenid().longValue()==MENID_51){//性别
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"CUSTSORT");//证件类型
				if("0".equals(datum1.getValue())){//当证件类型为身份证的时候，该字段可以不输
					datum.setValue("");
				}else{
					if(StringUtils.isEmpty(datum.getValue())){
						//如果不是身份证性别为空则报错
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
				}
			}
			if(datum.getName().equals("BIRTHDATE")&&datum.getMenid().longValue()==MENID_51){//出生日期
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"CUSTSORT");//证件类型
				if("0".equals(datum1.getValue())){//当证件类型为身份证的时候，该字段可以不输
					datum.setValue("");
				}else{
					if(StringUtils.isEmpty(datum.getValue())){
						//如果不是身份证为空则报错
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid()+"_"+datum.getDatid());
					}
				}
			}
			if(datum.getName().equals("HADDRESS")&&datum.getMenid().longValue()==MENID_51){//住宅地址
				if(!StringUtils.isEmpty(datum.getValue())){//当住宅地址输入时，住宅地址市和住宅地址县(区)必输入二者其一
					Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "HCITY");//住宅地址市
					if(StringUtils.isEmpty(datum1.getValue())){
						datum1=DatumServiceImpl.findDatumByName(datumsPO, "HCOUNTY");//住宅地址县
						if(StringUtils.isEmpty(datum1.getValue())){
							throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
						}
					}
				}
			}
			if(datum.getName().equals("MVBLNO")&&datum.getMenid().longValue()==MENID_51){//手机号码
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"RELTMOBL1");//联系人一手机
				if(datum.getValue().equals(datum1.getValue())){//手机号码不能与联系人号码重复
					throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
				}
			}
			if(datum.getName().equals("CADDRESS")&&datum.getMenid().longValue()==MENID_51){//工作单位地址
				if(!StringUtils.isEmpty(datum.getValue())){//当单位地址输入时，单位地址市和单位地址县(区)必输入二者其一
					Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "CCITY");//单位地址市
					if(StringUtils.isEmpty(datum1.getValue())){
						datum1=DatumServiceImpl.findDatumByName(datumsPO, "CCOUNTY");//单位地址县(区)
						if(StringUtils.isEmpty(datum1.getValue())){
							throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
						}
					}
				}
			}
			if(datum.getName().equals("COMMADDR")&&datum.getMenid().longValue()==MENID_51){//通讯地址
				/*Datum datum1=datumRepository.findByBusidAndName(busid,"DRAWADDR");//卡片寄送地址类型
				if("3".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){//若“收卡地址”取值是3-通讯地址，则该字段必输。
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
				if(!StringUtils.isEmpty(datum.getValue())){//当通讯地址输入时，通讯地址市和通讯地址县(区)必输入二者其一
					datum1=DatumServiceImpl.findDatumByName(datumsPO,"COMMCITY");//通讯地址市
					if(StringUtils.isEmpty(datum1.getValue())){
						datum1=DatumServiceImpl.findDatumByName(datumsPO,"COMMCOUNTY");//通讯地址县(区)
						if(StringUtils.isEmpty(datum1.getValue())){
							throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
						}
					}
				}*/
			}
			if(datum.getName().equals("COMMAZIP")&&datum.getMenid().longValue()==MENID_51){//通讯地址邮编
				/*Datum datum1=datumRepository.findByBusidAndName(busid,"ACCADDRF");//对帐单寄送地址
				if("3".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){//对帐单寄送地址为通讯地址时必输
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}*/
			}
			if(datum.getName().equals("RELTADDR1")&&datum.getMenid().longValue()==MENID_51){//联系人一住址
				//当是否专项分期为0-否，必输
				//当是否专项分期为1-是，无需上送
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"SIFLAG");//是否专项分期
				if("0".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("RELTMOBL1")&&datum.getMenid().longValue()==MENID_51){//联系人一手机
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"RELTMOBL2");//联系人二手机
				if(datum.getValue().equals(datum1.getValue())){//联系人一手机不能与联系人二手机重复
					throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
				}
			}
			if(datum.getName().equals("DEIDYPE1")&&datum.getMenid().longValue()==MENID_51){//副卡申请人一证件类型
				//副卡申请人一与副卡申请人二不可为同一个人，
				//当是否专项分期为1-是,可不输副卡申请人一的相关字段
				//Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"DEIDCODE1");
				//Datum datum2=DatumServiceImpl.findDatumByName(datumsPO,"DEIDCODE2");
				//TODO,判断副卡申请人是否同一人
			}
			if(datum.getName().equals("DESEX1")&&datum.getMenid().longValue()==MENID_51){//副卡申请人一性别
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"DEIDYPE1");//副卡申请人一证件类型
				if("1".equals(datum1.getValue())){//当证件类型为身份证的时候，该字段可以不输
					datum.setValue("");
				}
			}
			if(datum.getName().equals("DEBIRTH1")&&datum.getMenid().longValue()==MENID_51){//副卡申请人一出生日期
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"DEIDYPE1");//副卡申请人一证件类型
				if("1".equals(datum1.getValue())){//当证件类型为身份证的时候，该字段可以不输
					datum.setValue("");
				}
			}
			if(datum.getName().equals("DEIDYPE2")&&datum.getMenid().longValue()==MENID_51){//副卡申请人二证件类型
				//副卡申请人一与副卡申请人二不可为同一个人，
				//当是否专项分期为1-是,可不输副卡申请人一的相关字段
			}
			if(datum.getName().equals("DESEX2")&&datum.getMenid().longValue()==MENID_51){//副卡申请人一性别
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"DEIDYPE2");//副卡申请人二证件类型
				if("1".equals(datum1.getValue())){//当证件类型为身份证的时候，该字段可以不输
					datum.setValue("");
				}
			}
			if(datum.getName().equals("DEBIRTH2")&&datum.getMenid().longValue()==MENID_51){//副卡申请人二出生日期
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"DEIDYPE2");//副卡申请人二证件类型
				if("1".equals(datum1.getValue())){//当证件类型为身份证的时候，该字段可以不输
					datum.setValue("");
				}
			}
			if(datum.getName().equals("STATDATE")&&datum.getMenid().longValue()==MENID_51){//证件有效期
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "TIMELESS");//是否长期有效
				if("1".equals(datum1.getValue())){//当是否长期有效为1时，证件有效期必须为9999-12-30
					datum.setValue("9999-12-30");
				}
			}
			if(datum.getName().equals("PHONZONO")&&datum.getMenid().longValue()==MENID_51){//联系电话区号
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"PHONENO");
				if(!StringUtils.isEmpty(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){//当联系电话主体号码输入，必输
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("PHONEXT")&&datum.getMenid().longValue()==MENID_51){//联系电话分机号
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"PHONZONO");//联系电话区号
				Datum datum2=DatumServiceImpl.findDatumByName(datumsPO, "PHONENO");//联系电话主体号码
				//当输入时，联系电话区号，联系电话主体号码不能为空
				if(StringUtils.isEmpty(datum1.getValue())){
					throw new IllegalDatumValueException(datum1.getNamedepict()+"_"+datum1.getDatid());
				}
				if(StringUtils.isEmpty(datum2.getValue())){
					throw new IllegalDatumValueException(datum2.getNamedepict()+"_"+datum2.getDatid());
				}
			}
			//-----------------------------------------------------分期信息-----------------------------------
			if(datum.getName().equals("SCANBRNO")&&datum.getMenid().longValue()==MENID_101){//扫描网点号
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"AGNBRNO");//受理网点号
				datum.setValue(datum1.getValue());//扫描网点号同步于受理网点号
			}
			if(datum.getName().equals("DOWNPAYAMT")&&datum.getMenid().longValue()==MENID_101){//首付款金额
				if("0".equals(datum.getValue().trim())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"LOANAMT");//首付款金额不能为0 且 必须为首付款金额+贷款金额的20%以上
				if(Double.valueOf(datum.getValue())<(Double.valueOf(datum1.getValue())+Double.valueOf(datum.getValue()))*0.2){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("LOANAMT")&&datum.getMenid().longValue()==MENID_101){//贷款金额
				if(Double.valueOf(datum.getValue())<=0){//贷款金额不能为0
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("PAYEETYPE")&&datum.getMenid().longValue()==MENID_101){//收款对象类型
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"PAYMODE");//支付模式
				if("1".equals(datum1.getValue())){//当支付模式为1-受托支付时，收款对象类型上送2-合作单位
					datum.setValue("2");
				}
				if("2".equals(datum1.getValue())){//当支付模式为2-实体POS刷卡分期时，收款对象类型上送空
					datum.setValue("");
				}
			}
			if(datum.getName().equals("RECEIVEAMT1")&&datum.getMenid().longValue()==MENID_101){//合作单位收款金额1
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"PAYEETYPE");
				if("2".equals(datum1.getValue())){//当收款对象类型为2-合作单位时，必输，否则不用上送
					Datum datum2=DatumServiceImpl.findDatumByName(datumsPO,"LOANAMT");
					datum.setValue(datum2.getValue());//默认为贷款金额
				}
				datum1=DatumServiceImpl.findDatumByName(datumsPO,"PAYMODE");//支付模式
				if("2".equals(datum1.getValue())){//当支付模式为2-实体POS刷卡分期时，输入报错
					datum.setValue("");
				}
			}
			if(datum.getName().equals("INTERESTNO1")&&datum.getMenid().longValue()==MENID_101){//贴息商品编号1
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"INTERESTFLAG1");//合作单位是否贴息
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("RECEIVEAMT2")&&datum.getMenid().longValue()==MENID_101){//合作单位收款金额2
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"COCOMNO2");//合作单位编号2
				Datum datum2=DatumServiceImpl.findDatumByName(datumsPO,"PAYEETYPE");//收款对象类型
				if(!StringUtils.isEmpty(datum1.getValue())&&datum2.getValue().equals("2")&&StringUtils.isEmpty(datum.getValue())){//当合作单位编号2不为空，且收款对象类型为2-合作单位时，必输，否则不用上送
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
				datum1=DatumServiceImpl.findDatumByName(datumsPO,"PAYMODE");//支付模式
				if("2".equals(datum1.getValue())){//当支付模式为2-实体POS刷卡分期时，输入报错
					datum.setValue("");
				}
			}
			if(datum.getName().equals("INTERESTNO2")&&datum.getMenid().longValue()==MENID_101){//贴息商品编号2
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO,"INTERESTFLAG2");
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
			}
			if(datum.getName().equals("LOANRATIO")&&datum.getMenid().longValue()==MENID_101){//贷款成数（%）
				//贷款成数（%）=100-首付款比例
				//设置最高抵押率为贷款成数
				/*Datum datum1=this.findDatumByName(datumsPO,"MAXMORTRATE");//最高抵押率（%）
				datum1.setValue(datum.getValue());
				datumRepository.saveAndFlush(datum1);*/
			}
			if(datum.getName().equals("INSUREAMT")&&datum.getMenid().longValue()==MENID_101){
				datum.setValue("");//车损险投保金额输入时，损险投保金额须大于等于汽车销售价
			}
			if(datum.getName().equals("MORTNAME")&&datum.getMenid().longValue()==MENID_101){//抵押物名称
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
				}
				/*当业务模式为1-抵押，必输
				当业务模式为4-组合
				抵押物相关信息，质押品相关信息，保证人相关信息，至少录入两类信息
				录入某类信息则确保该类信息必输项都不为空，
				抵押物价值*最高抵押率+质押品金额+保证总金额需要大于等于贷款金额*/
			}
			if(datum.getName().equals("CHNSNAME")&&datum.getMenid().longValue()==MENID_101){//权属人姓名
				List<Datum> datum1=datumRepository.findByBusidAndMenidAndName(busid, MENID_51,"CHNSNAME");//找到客户信息中的姓名
				datum.setValue(datum1.get(0).getValue());
			}
			if(datum.getName().equals("CHNSNAME")&&datum.getMenid().longValue()==MENID_101){//权属人姓名
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为1-抵押，必输
				}
			}
			if(datum.getName().equals("MORTLTYPE")&&datum.getMenid().longValue()==MENID_101){//抵押物大类
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为1-抵押，必输
				}
			}
			if(datum.getName().equals("MORTMTYPE")&&datum.getMenid().longValue()==MENID_101){//抵押物中类
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为1-抵押，必输
				}
			}
			if(datum.getName().equals("MORTSTYPE")&&datum.getMenid().longValue()==MENID_101){//抵押物小类
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为1-抵押，必输
				}
			}
			if(datum.getName().equals("MORTVALUE")&&datum.getMenid().longValue()==MENID_101){//抵押物价值
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("1".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为1-抵押，必输
				}
				datum1=DatumServiceImpl.findDatumByName(datumsPO,"MAXMORTRATE");//最高抵押率（%）
				Datum datum2=DatumServiceImpl.findDatumByName(datumsPO, "LOANAMT");//贷款金额
				if(Double.valueOf(datum.getValue())*(Double.valueOf(datum1.getValue())/100)<Double.valueOf(datum2.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//抵押物价值*最高抵押率须大于等于贷款金额
				}
			}
			if(datum.getName().equals("MAXMORTRATE")&&datum.getMenid().longValue()==MENID_101){//最高抵押率（%）
				/*Datum datum1=this.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				datum1=this.findDatumByName(datumsPO,"LOANRATIO");//贷款成数//当业务模式为1-抵押，必输,同步于贷款成数
				datum.setValue(datum1.getValue());*/
			}
			if(datum.getName().equals("IMPNO")&&datum.getMenid().longValue()==MENID_101){//质押品编号
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("2".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为2-质押,必输
				}
			}
			if(datum.getName().equals("BAILTYPE")&&datum.getMenid().longValue()==MENID_101){//保证人性质
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("3".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为2-质押,必输
				}
			}
			if(datum.getName().equals("BAILNAME")&&datum.getMenid().longValue()==MENID_101){//保证人名称
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("3".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为3-保证，必输
				}
			}
			if(datum.getName().equals("BAILAMOUNT")&&datum.getMenid().longValue()==MENID_101){//保证总金额
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("3".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为3-保证，必输
				}
			}
			if(datum.getName().equals("ISHOLDPERBAIL")&&datum.getMenid().longValue()==MENID_101){//是否全程担保
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("3".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为3-保证，必输
				}
			}
			if(datum.getName().equals("BAILCONTNO")&&datum.getMenid().longValue()==MENID_101){//担保合同/担保承诺函编号
				Datum datum1=DatumServiceImpl.findDatumByName(datumsPO, "BUSIMODE");//业务模式
				if("3".equals(datum1.getValue())&&StringUtils.isEmpty(datum.getValue())){
					throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());//当业务模式为3-保证，必输
				}
			}
		}
		//-------------------------------------------------------影像信息---------------------------------------------
		
		
		//-------------------------------------------------------Empty----------------------------------------------
		
		
		//-------------------------------------------------------关联信息----------------------------------------------
		if(menid==MENID_100){
			Datum datum1=datumRepository.findByBusidAndName(busid,"RELCUSTNUM");//关联客户信息条数
			for(int i=0;i<Integer.valueOf(datum1.getValue());i++){
				for(Datum datum:datumsPO){
					if(datum.getIsrequired()!=0&&StringUtils.isEmpty(datum.getValue())){
						throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
					}
					if(datum.getName().equals("STATDATE_"+i)){//证件有效期
						if(!datum.getValue().equals("9999-12-30")){
							//如果证件有效期非9999-12-30，将证件是否长期有效设为0
							this.findDatumByName(datums,"TIMELESS_"+i).setValue("0");;
						}
					}
					if(datum.getName().equals("PHONZONO_"+i)){//联系电话区号
						Datum datum2=DatumServiceImpl.findDatumByName(datumsPO,"PHONENO_"+i);//联系电话主体号码
						if(!StringUtils.isEmpty(datum2.getValue())&&StringUtils.isEmpty(datum.getValue())){//当联系电话主体号码输入，必输
							throw new IllegalDatumValueException(datum.getNamedepict()+"_"+datum.getDatid());
						}
					}
					if(datum.getName().equals("PHONEXT_"+i)){//联系电话分机号
						//当输入时，联系电话区号，联系电话主体号码不能为空
						if(!StringUtils.isEmpty(datum.getValue())){
							Datum datum2=DatumServiceImpl.findDatumByName(datumsPO,"PHONZONO_"+i);//联系电话区号
							if(StringUtils.isEmpty(datum2.getValue())){
								throw new IllegalDatumValueException(datum2.getNamedepict()+"_"+datum2.getDatid());
							}
							Datum datum3=DatumServiceImpl.findDatumByName(datumsPO,"PHONENO_"+i);//联系电话主体号码
							if(StringUtils.isEmpty(datum3.getValue())){
								throw new IllegalDatumValueException(datum3.getNamedepict()+"_"+datum3.getDatid());
							}
						}
					}
					if(datum.getName().equals("MOBILENO_"+i)){//手机
						if(StringUtils.isEmpty(datum.getValue())){
							Datum datum2=DatumServiceImpl.findDatumByName(datumsPO,"PHONENO_"+i);//联系电话主体号码
							if(StringUtils.isEmpty(datum2.getValue())){
								throw new IllegalDatumValueException(datum2.getNamedepict()+"_"+datum2.getDatid());
							}
						}
					}
				}
				
			}
		}
		datumRepository.save(datumsPO);
		business.setState(Business.STATE_STEP1_7);
		businessRepository.saveAndFlush(business);
		if(menid!=DatumServiceImpl.MENID_101){
			return;
		}
		ThreadPool.getThreadPoolExecutor().execute(new IniRelInfo(business.getBusid()));
	}
	@Override
	public List<Datum> listByBusidAndMenid(long busid, long menid) {
		List<Datum> datums=datumRepository.findByBusidAndMenidOrderBySort(busid, menid);
		return datums;
	}

	@Override
	public List<Datum> listByBusidAndIsget(long busid) {
		List<Datum> datums=datumRepository.findByBusidAndIsgetOrderBySort(busid,1);
		return datums;
	}

	@Override
	public List<Datum> listByCodeAndIsget(String code) {
		List<Datum> datums=datumRepository.findByCodeAndIsgetOrderBySort(code,1);
		return datums;
	}

	@Override
	public void leadin(String datumsJSON) throws IllegalDatumValueException{
		List<Datum> datums=new Gson().fromJson(datumsJSON,new TypeToken<List<Datum>>(){}.getType());
		Set<Long> busids=new HashSet<Long>();
		for(Datum datum:datums){
			busids.add(datum.getBusid());
			if(!StringUtils.isEmpty(datum.getValue())){
				if(datum.getIsrequired()!=0&&StringUtils.isEmpty(datum.getValue().trim())){
					throw new IllegalDatumValueException(datum.getNamedepict());
				}
				int length=0;
				try {
					length=datum.getValue().getBytes(CharSet.GBK).length;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(length>datum.getLength()){
					throw new IllegalDatumValueException(datum.getNamedepict());
				}
				datum.setValue(datum.getValue().trim());//去空格
			}
		}
		List<Business> businesss=businessRepository.findByBusidInAndIsdel(new ArrayList<Long>(busids),0);
		for(Business business:businesss){
			if(business.getState()!=Business.STATE_STEP1_7&&business.getState()!=Business.STATE_STEP2_12){
				continue;
			}
			Partner partner=partnerRepository.findOne(business.getParid());
			if(partner.getIsproxy().intValue()==0){
				//如果是普通合作商，不需要同步文件
				business.setIssync(1);
			}
			List<Datum> datumsForBusid=new ArrayList<Datum>();
			datums.forEach(datum->{
				if(datum.getBusid().longValue()==business.getBusid().longValue()){
					datumsForBusid.add(datum);
				}
			});
			datumRepository.save(datumsForBusid);
			ThreadPool.getThreadPoolExecutor().execute(new IniRelInfo(business.getBusid()));
		}
		businessRepository.save(businesss);
	}
	
	@Override
	public String leadout(String busids,String token) {
		List<Long> busidList=new ArrayList<Long>();
		String[] busidsStr=busids.split(",");
		for(int i=0;i<busidsStr.length;i++){
			busidList.add(Long.valueOf(busidsStr[i]));
		}
		List<Business> businesss=businessRepository.findByBusidInAndStateAndIssyncAndAcodeNotAndIsdel(busidList,Business.STATE_STEP1_7,1,BusinessServiceImpl.INIT_ACODE,0);
		List<Menu> menus=menuRepository.findByIsskuAndIsdelOrderByMenid(1, 0);
		List<File> files=new ArrayList<File>();
		for(int i=0;i<menus.size();i++){
			System.err.println(menus.get(i).getName());
			String prefix="0310000203";//工行固定前缀
			OutQueue outqueue=outQueueRepository.findByCurdateAndSeqMax(this.dateFormat(new Date()));
			long seq=501L;
			if(!StringUtils.isEmpty(outqueue)){
				seq=outqueue.getSeq()+1;
			}
			outQueueRepository.save(new OutQueue(seq, this.dateFormat(new Date()), menus.get(i).getMenid(), System.currentTimeMillis()));
			//文件名问工行固定前缀+当前日期20170925+从501开始每日不重复
			String date=this.dateFormat(new Date());
			String fileName=prefix+date+seq+".txt";
			File file=new File(fileName);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int SEQNO=1;//申请卡序号
			for(Business business:businesss){
				List<Datum> datumsPO=datumRepository.findByMenidAndBusidAndIsexportOrderBySort(menus.get(i).getMenid(),business.getBusid(),1);
				List<Datum> datums=new ArrayList<Datum>();
				datumsPO.forEach(datum->{//克隆一份datumsPO,解决事物问题
					Datum datum1=Datum.getInstance();
					BeanUtils.copyProperties(datum, datum1);
					datums.add(datum1);
				});
				datums.forEach(datum->{//为金额数据，需要元转分
					if(datum.getIscover()!=0&&!StringUtils.isEmpty(datum.getValue())){
						datum.setValue(String.valueOf((int)(Double.valueOf(datum.getValue())*100)));
					}
				});
				StringBuffer line=new StringBuffer();
				if(menus.get(i).getMenid().longValue()==100L){//关联信息
					Datum datum1=datumRepository.findByBusidAndName(business.getBusid(),"RELCUSTNUM");//关联客户信息条数
					int SEQNO_0=1;//关联信息中，序号
					for(int d=0;d<Integer.valueOf(datum1.getValue());d++){//循环关联客户信息条数
						line=new StringBuffer();
						for(Datum datum:datums){
							int num=Integer.valueOf(datum.getName().split("_")[1]);//当前行数
							if(num==d){//当后缀为当前行数时
								if(datum.getName().equals("SEQNO_"+d)){//序号
									datum.setValue(String.valueOf(SEQNO_0));
									SEQNO_0++;
								}
								if(datum.getName().equals("WORKDATE_"+d)){//上传日期
									datum.setValue(date);
								}
								if(datum.getName().equals("SERIAL_"+d)){//序列号
									datum.setValue(String.valueOf(seq));
								}
								if(datum.getName().equals("APPNO_"+d)){//申请表编号
									datum.setValue(business.getAcode());
								}
								String field=this.cover(datum.getValue(),datum.getLength(),datum.getCover());
								//System.err.println("name:"+datum.getNamedepict()+",value:"+field);
								line.append(field);
							}
						}
						TxtUtils.append(file,line.toString());
					}
				}else{
					for(Datum datum:datums){
						switch (datum.getName()) {
						case "WORKDATE"://上传日期
							datum.setValue(date);
							break;
						case "SERIAL"://序列号
							datum.setValue(String.valueOf(seq));
							break;
						case "DRAWBRNO"://领卡网点号
							datum.setValue(DatumServiceImpl.findDatumByName(datums,"AGNBRNO").getValue());
							break;
						case "APPNO"://申请表编号
							datum.setValue(business.getAcode());
							break;
						case "SEQNO"://申请卡序号
							datum.setValue(String.valueOf(SEQNO));
							SEQNO++;//卡序号每行递增，每个业务只有一个卡序号
							break;
						case "DRAWADDR"://卡片寄送地址类型
							if("1".equals(DatumServiceImpl.findDatumByName(datums,"DRAWMODE").getValue())){//卡片领取方式
								datum.setValue("");//若DRAWMODE取值为1，则此项必须是空。否则，此项不能是空，且不允许指向地址内容为空。
							}
							if("2".equals(DatumServiceImpl.findDatumByName(datums,"DRAWMODE").getValue())){//卡片领取方式
								datum.setValue("3");//若DRAWMODE取值为2,卡片寄送地址类型为3
							}
							break;	
						case "ENGNAME"://姓名拼音或英文名
							datum.setValue(PinyinUtils.getPingYin(DatumServiceImpl.findDatumByName(datums,"CHNSNAME").getValue()));
							break;
						case "CHNSNAME"://权属人姓名
							datum.setValue(DatumServiceImpl.findDatumByName(datums, "CHNSNAME").getValue());
							break;
						case "ISRELCUST"://是否关联客户信息
							if(Integer.valueOf(DatumServiceImpl.findDatumByName(datums, "RELCUSTNUM").getValue())>0){
								//如果关联客户信息条数大于0,并且是否关联客户信息为0，需要将是否关联客户信息变为1
								datum.setValue("1");
							}
							break;
						case "ENCBNO"://他项权证号,身份证号
							datum.setValue(business.getIdcard());
							break;
						case "ENCBREGDATE"://他项权证登记日期,上传日期
							datum.setValue(this.dateFormat1(new Date()));
							break;
						case "ENCBPREDDATE"://预计可办理他项权证日期，上传日期加2个月
							Calendar curdate=Calendar.getInstance();
							curdate.setTime(new Date());
							curdate.add(Calendar.MONTH,2);
							datum.setValue(this.dateFormat1(curdate.getTime()));
							break;
						case "MORTBEGDATE"://抵押起始日，上传日期
							datum.setValue(this.dateFormat1(new Date()));
							break;
						case "MORTENDDATE"://抵押到期日，上传日期加3年
							Calendar curdate1=Calendar.getInstance();
							curdate1.setTime(new Date());
							curdate1.add(Calendar.YEAR,3);
							datum.setValue(this.dateFormat1(curdate1.getTime()));
							break;
						case "MORTCONTRNO"://抵押合同编号，申请表编号
							datum.setValue(business.getAcode());
							break;
						case "BAILCONTNO"://担保合同/担保承诺函编号，申请表编号
							datum.setValue(business.getAcode());
							break;
						}
						String field=this.cover(datum.getValue(),datum.getLength(),datum.getCover());
						//System.err.println("name:"+datum.getNamedepict()+",value:"+field);
						line.append(field);
					}
					TxtUtils.append(file,line.toString());
				}
				business.setState(Business.STATE_STEP2_11);
			}
			files.add(file);
		}
		businessRepository.save(businesss);
		//打包上传等待下载
		Map<String,String> params=new HashMap<String,String>();
		String json=RequestUtil.request(this.upload_leadout+"?token="+token, params,files);
		String url=new Gson().fromJson(json, UploadLeadoutResult.class).data;
		return url;
	}
	
	/**
	 * 根据名称查找
	 * @param attNames
	 * @param name
	 * @return
	 */
	public static Datum findDatumByName(List<Datum> datums,String name){
		for(Datum datum:datums){
			if(datum.getName().equals(name)){
				return datum;
			}
		}
		return null;
	}
	/**
	 * 将错误文件上传到文件服务器提供给合作上下载
	 */
	@Value("${cloud-file-client}/file/upload_leadout")
	private String upload_leadout;
	/**
	 * 上传文件返回结果对象
	 * @author admin
	 * whats your problem
	 */
	private class UploadLeadoutResult{
		private int code;
		private String depict;
		private String data;
	}
	/**
	 * 补位
	 * @param value
	 * @param length
	 * @return
	 */
	private String cover(String value,int length,String cover){
		if(StringUtils.isEmpty(value)){
			value="";
		}
		int dlength=0;
		try {
			dlength=StringUtils.isEmpty(value)?length-0:length-value.getBytes(CharSet.GBK).length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
		for(int i=0;i<dlength;i++){
			if(StringUtils.isEmpty(value.trim())){
				value+=String.format("%c", 0x20);
				continue;
			}
			if(AttName.L.equals(cover)){
				value="0"+value;
				continue;
			}
			if(AttName.R.equals(cover)){
				value+=String.format("%c", 0x20);
				continue;
			}
			
		}
		return value;
	}
	
	private String dateFormat(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	
	private String dateFormat1(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	@Override
	public void leadback(List<String> lines,String filename) {
		filename=filename.split("\\.")[0];//去掉文件后缀名
		String date=filename.substring(10, 18);//文件日期20170808
		long seq=Long.valueOf(filename.substring(18,filename.length()));//队列
		OutQueue outqueue=outQueueRepository.findByCurdateAndSeq(date, seq);
		if(StringUtils.isEmpty(outqueue)){
			log.error("outqueue is null");
			return;
		}
		Menu menu=menuRepository.findOne(outqueue.getMenid());
		if(StringUtils.isEmpty(menu)){
			log.error("menu is null");
			return;
		}
		List<AttName> attnames=attNameRepository.findByMenidAndIsdelOrderBySort(menu.getMenid(),0);
		for(String line:lines){
			int begin=0;
			int end=0;
			for(AttName attname:attnames){
				end+=attname.getLength().intValue();
				String field=this.getStrByByteBeginEnd(line, begin, end);
				if(attname.getName().contains("APPNO")){//获取申请编号
					log.info("APPNO:{}",field);
					List<Business> businesss=businessRepository.findByAcode(field);
					if(businesss.isEmpty()){
						log.error("business is null");
						break;
					}
					String errorcode="";
					try {
						errorcode = this.getStrByByteBeginEnd(line, line.getBytes(CharSet.GBK).length-3,line.getBytes(CharSet.GBK).length);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					String error=ErrorinfoUtils.getError(errorcode);
					businesss.get(0).setRemark(error);
					if(errorcode.equals("999")){//成功
						businesss.get(0).setState(Business.STATE_DONE);
					}else{
						businesss.get(0).setState(Business.STATE_STEP2_12);
					}
					businessRepository.saveAndFlush(businesss.get(0));
					verifyRepository.save(new Verify(businesss.get(0).getBusid(),"bank",BusinessServiceImpl.progress(businesss.get(0).getState()),error,System.currentTimeMillis()));
					break;
				}
				begin+=attname.getLength();
			}
		}
	}
	/**
	 * 初始化客户关联信息，因关联数会被修改
	 */
	class IniRelInfo implements Runnable{
		private long busid;
		public IniRelInfo(long busid){
			this.busid=busid;
		}
		@Override
		public void run() {
			Datum datum=datumRepository.findByBusidAndName(busid,"RELCUSTNUM");//已持久化的值
			//如果数量不一致需要初始化关联信息数据
			List<AttName> attnames=attNameRepository.findByMenidAndIsdelOrderBySort(100L,0);
			for(int i=0;i<10;i++){//关联客户信息最多9条
				for(AttName attname:attnames){
					//判断datum是否已经存在
					String name=attname.getName().split("_")[0]+"_"+i;//属性名称
					Datum existDatum=datumRepository.findByBusidAndName(busid,name);
					if(i+1>Integer.valueOf(datum.getValue())&&!StringUtils.isEmpty(existDatum)&&i!=0){
						//如果已保存的条数大于现在的条数需要删除
						datumRepository.delete(existDatum);
						continue;
					}
					if(i<Integer.valueOf(datum.getValue())&&StringUtils.isEmpty(existDatum)){//不存在，需要初始化
						Datum initDatum=InitByAttName(busid, attname);
						initDatum.setName(name);
						initDatum.setNamedepict(initDatum.getNamedepict()+i);
						datumRepository.save(initDatum);
					}
				}
			}
		}
	}
	/**
	 * 根据输入的字符串获取固定字节长度的字符串
	 * @param line
	 * @param length
	 * @return
	 */
	private String getStrByByteBeginEnd(String line,int begin,int end){
		try {
			return new String(Arrays.copyOfRange(line.getBytes(CharSet.GBK), begin, end),CharSet.GBK).trim();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 值域校验
	 * @return
	 */
	private void validScopeValue(List<Datum> datums)throws IllegalDatumValueException{
		List<AttValue> attValues=attValueRepository.findAll();
		for(Datum datum:datums){
			if(datum.getIntype().intValue()!=AttName.INTYPE_RADIO&&datum.getIntype().intValue()!=AttName.INTYPE_CHECKBOX){
				continue;
			}
			boolean isvalid=false;
			for(AttValue attValue:attValues){
				if(datum.getNamid().longValue()==attValue.getNamid().longValue()&&datum.getIsrequired()!=0&&attValue.getValue().equals(datum.getValue())){
					//namid相同&&是必选项&&值相同
					isvalid=true;
				}
			}
			if(!isvalid){
				throw new IllegalDatumValueException(datum.getNamedepict());
			}
		}
	}
	/**
	 * 使用attname初始化一个datum
	 * @param busid
	 * @param attName
	 * @return
	 */
	public Datum InitByAttName(long busid,AttName attName){
		Datum datum=Datum.getInstance();
		datum.setBusid(busid);
		datum.setNamid(attName.getNamid());
		datum.setName(attName.getName());
		datum.setNamedepict(attName.getDepict());
		datum.setValue(attName.getDefvalue());
		datum.setValuedepict(attName.getDefvalue());
		datum.setMenid(attName.getMenid());
		datum.setLength(attName.getLength());
		datum.setIsrequired(attName.getIsrequired());
		datum.setIntype(attName.getIntype());
		datum.setDefvalue(attName.getDefvalue());
		datum.setReg(attName.getReg());
		datum.setCover(attName.getCover());
		datum.setUnit(attName.getUnit());
		datum.setIschange(attName.getIschange());
		datum.setIscover(attName.getIscover());
		datum.setDatatype(attName.getDatatype());
		datum.setSort(attName.getSort());
		datum.setRemark(attName.getRemark());
		datum.setIsget(attName.getIsget());
		datum.setIsexport(attName.getIsexport());
		datum.setTime(System.currentTimeMillis());
		return datum;
	}
}
