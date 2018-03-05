package org.cloud.bank.client.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.bank.client.exception.IllegalBusinessStateException;
import org.cloud.bank.client.model.AttName;
import org.cloud.bank.client.model.Business;
import org.cloud.bank.client.model.Datum;
import org.cloud.bank.client.model.Operator;
import org.cloud.bank.client.model.Partner;
import org.cloud.bank.client.model.User;
import org.cloud.bank.client.model.Verify;
import org.cloud.bank.client.repository.AttNameRepository;
import org.cloud.bank.client.repository.BusinessRepository;
import org.cloud.bank.client.repository.DatumRepository;
import org.cloud.bank.client.repository.OperatorRepository;
import org.cloud.bank.client.repository.PartnerRepository;
import org.cloud.bank.client.repository.UserRepository;
import org.cloud.bank.client.repository.VerifyRepository;
import org.cloud.bank.client.service.BusinessService;
import org.cloud.bank.client.thread.ThreadPool;
import org.cloud.bank.client.util.DesUtil;
import org.cloud.bank.client.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

@Service
public class BusinessServiceImpl implements BusinessService{

	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private OperatorRepository operatorRepository;
	@Autowired
	private PartnerRepository partnerRepository;
	@Autowired
	private VerifyRepository verifyRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AttNameRepository attNameRepository;
	@Autowired
	private DatumRepository datumRepository;
	
	private static final Logger log=LoggerFactory.getLogger(BusinessServiceImpl.class);
	/**
	 * 添加业务时默认的申请编号(acode)
	 */
	public static final String INIT_ACODE="0";
	
	@Override
	public Business add(Business business) {
		Operator operator=operatorRepository.findOne(business.getOpeid());
		String maxCode=businessRepository.findByCodeMax(operator.getCode());
		business.setCode(operator.getCode()+BusinessServiceImpl.genCode(maxCode));
		business.setParid(operator.getParid());
		business.setState(Business.STATE_1);
		business.setPstate(Business.STATE_2);
		business.setBanid(operator.getBanid());
		business.setBanname(operator.getBankname());
		business.setParname(operator.getPartnername());
		business.setRemark(" ");
		business.setAcode(INIT_ACODE);
		business.setIssync(0);
		business.setIsdel(0);
		business.setTime(System.currentTimeMillis());
		businessRepository.save(business);
		return business;
	}
	@Override
	public void updByBusid(Business business) {
		businessRepository.saveAndFlush(business);
	}
	/**
	 * 不允许当日重复身份证号，但业务初审被拒绝的可以
	 */
	@Override
	public List<Business> listByIdcardAndCurdate(String idcard) {
		List<Business> businesss=businessRepository.findByIdcardAndStateNotAndTimeGreaterThan(idcard,Business.STATE_5,System.currentTimeMillis()-24*60*60*1000);
		return businesss;
	}


	@Override
	public void delByBusid(long busid) {
		Business business=businessRepository.findOne(busid);
		if(StringUtils.isEmpty(business)){
			return;
		}
		business.setIsdel(1);
		businessRepository.saveAndFlush(business);
	}
	
	/**
	 * 新编码生成器
	 * @param maxCode
	 * @return
	 */
	private static String genCode(String maxCode){
		String code="";
		if(StringUtils.isEmpty(maxCode)){
			code="1";
		}else{
			maxCode=maxCode.substring(10,maxCode.length());
			int icode=Integer.parseInt(maxCode)+1;
			code=""+icode;
		}
		return code;
	}
	
	@Override
	public long countByBanidState(long banid,int state) {
		long count=0;
		if(state==0){
			count=businessRepository.countByBanidAndIsdel(banid,0);
		}else{
			count=businessRepository.countByBanidAndStateAndIsdel(banid,state,0);
		}
		return count;
	}
	
	@Override
	public List<Business> listByBanidState(long banid,int state, int page, int size) {
		List<Business> business=null;
		Sort sort=new Sort(Direction.DESC,"time");
		if(state==0){
			business=businessRepository.findByBanidAndIsdel(banid,0,new PageRequest(page, size,sort)).getContent();
		}else{
			business=businessRepository.findByBanidAndStateAndIsdel(banid,state,0,new PageRequest(page, size,sort)).getContent();
		}
		return business;
	}
	
	@Override
	public List<Business> listByOpeidState(long opeid, int state, int page,int size) {
		List<Business> businesss=new ArrayList<Business>();
		Sort sort=new Sort(Direction.DESC,"time");
		if(state==0){
			businesss=businessRepository.findByOpeidAndIsdel(opeid,0,new PageRequest(page, size,sort)).getContent();
		}else{
			businesss=businessRepository.findByOpeidAndStateAndIsdel(opeid, state,0, new PageRequest(page, size,sort)).getContent();
		}
		return businesss;
	}

	@Override
	public List<Business> listByOpeidCodeSurname(long opeid, String code,String surname,int page,int size) {
		List<Business> businesss=new ArrayList<Business>();
		if("0".equals(code)){
			businesss=businessRepository.findByOpeidAndSurnameLikeAndIsdel(opeid, surname,0,new PageRequest(page, size)).getContent();
		}
		if("0".equals(surname)){
			businesss=businessRepository.findByOpeidAndCodeLikeAndIsdel(opeid, code, 0,new PageRequest(page, size)).getContent();
		}
		return businesss;
	}
	
	@Override
	public List<Business> listByOpeidIdcardSurname(long opeid, String idcard,String surname, int page, int size) {
		List<Business> businesss=new ArrayList<Business>();
		if("0".equals(idcard)){
			businesss=businessRepository.findByOpeidAndSurnameLikeAndIsdel(opeid, surname,0,new PageRequest(page, size)).getContent();
		}
		if("0".equals(surname)){
			businesss=businessRepository.findByOpeidAndIdcardLikeAndIsdel(opeid, idcard,0, new PageRequest(page, size)).getContent();
		}
		return businesss;
	}
	
	@Override
	public Business getByBusid(long busid) {
		Business business=businessRepository.findOne(busid);
		return business;
	}

	@Override
	public void updByBusidState(long busid, int state,String remark,String token) {
		Business business=businessRepository.findOne(busid);
		if(StringUtils.isEmpty(business)){
			log.error("business is null,busid={}",busid);
			return;
		}
		switch(business.getState()){
		case Business.STATE_1:
			return;
		case Business.STATE_2://初审待审核
			if(state<Business.STATE_3||state>Business.STATE_5){
				//如果状态不是初审通过和初审拒绝之间
				log.error("business current state={},after state={}",business.getState(),state);
				return;
			}
			break;
		case Business.STATE_3:
			return;
		case Business.STATE_4:
			return;
		case Business.STATE_5:
			return;
		case Business.STATE_STEP1_6://面签待审核
			if(state<Business.STATE_STEP1_7||state>Business.STATE_STEP1_9){
				//如果状态不是面签通过和面签拒绝之间
				log.error("business current state={},after state={}",business.getState(),state);
				return;
			}
			break;
		case Business.STATE_STEP1_7:
			return;
		case Business.STATE_STEP1_8:
			return;
		case Business.STATE_STEP1_9:
			return;
		case Business.STATE_STEP2_10://放款待审核
			if(state<Business.STATE_STEP2_11||state>Business.STATE_STEP2_13){
				//如果状态不是放款通过和放款拒绝之间
				log.error("business current state={},after state={}",business.getState(),state);
				return;
			}
			break;
		case Business.STATE_STEP2_11://上传开户中
			break;
		case Business.STATE_STEP2_12://放款驳回
			break;
		case Business.STATE_STEP2_13://放款拒绝
			break;
		}
		business.setState(state);
		business.setRemark(remark);
		Partner partner=partnerRepository.findOne(business.getParid());
		if(business.getState()==Business.STATE_3&&partner.getIsproxy()==0){
			//如果状态初是审通过并且是普通合作商,默认合作商第三方系统的审核状态为面签已通过
			business.setPstate(Business.STATE_STEP1_7);
		}
		//推送到APP
		Map<String,String> params=new HashMap<String,String>();
		params.put("busid",String.valueOf(business.getBusid()));
		params.put("opeid",String.valueOf(business.getOpeid()));
		params.put("code",business.getCode());
		params.put("surname",business.getSurname());
		params.put("idcard",business.getIdcard());
		params.put("telephone",business.getTelephone());
		params.put("state",String.valueOf(state));
		params.put("remark",business.getRemark());
		params.put("token",token);
		String json=RequestUtil.request(partner.getHostname()+"/partner/bank/business/push", params);
		if(new Gson().fromJson(json, PushResult.class).code!=200){
			//将消息推送到APP，将用户资料推送到代理合作商
			throw new RuntimeException("the push failed");
		}
		Jws<Claims> claims = Jwts.parser().setSigningKey(DesUtil.DEFAULT_PASSWORD_CRYPT_KEY).parseClaimsJws(token);
    	long userid=Long.parseLong(claims.getBody().get("userid").toString());
    	String username="0";//@See Verify username
    	if(state>=Business.STATE_3&&state<=Business.STATE_5){
    		//bank admin verify
    		User user=userRepository.findOne(userid);
    		username=user.getSurname();
    	}
    	if(state>=Business.STATE_STEP1_7&&state<=Business.STATE_STEP1_9){
    		//partner admin verify
    		Partner vpartner=partnerRepository.findOne(userid);
    		username=vpartner.getSurname();
    	}
    	if(state>=Business.STATE_STEP2_11&&state<=Business.STATE_STEP2_13){
    		//bank admin verify
    		User user=userRepository.findOne(userid);
    		username=user.getSurname();
    	}
    	//创建审核记录
		verifyRepository.save(new Verify(business.getBusid(), username, progress(state), remark, System.currentTimeMillis()));
		businessRepository.saveAndFlush(business);
		//如果面签通过审核，开始创建客户资料信息
		if(state==Business.STATE_STEP1_7){
			log.info("init datum thread begin");
			ThreadPool.getThreadPoolExecutor().execute(new InitDatumThread(business.getBusid()));
		}
	}
	/**
	 * 初始化客户资料线程
	 * @author admin
	 *
	 */
	class InitDatumThread implements Runnable{
		private long busid;
		public InitDatumThread(long busid){
			this.busid=busid;
		}
		@Override
		public void run(){
			List<AttName> attNames=attNameRepository.findByIsdelOrderBySort(0);
			log.info("attnames.size:{}",attNames.size());
			List<Datum> datums=new ArrayList<Datum>();
			attNames.forEach(attName ->{
				Datum datum=new DatumServiceImpl().InitByAttName(busid, attName);
				if(attName.getMenid().longValue()==DatumServiceImpl.MENID_51){//客户信息
					if(attName.getName().equals("CUSTCODE")){//身份证，同步business中身份证
						Business business=businessRepository.findOne(busid);
						datum.setValue(business.getIdcard());
					}
					if(attName.getName().equals("CHNSNAME")){//姓名
						Business business=businessRepository.findOne(busid);
						datum.setValue(business.getSurname());
					}
				}
				datums.add(datum);
			});
			log.info("datums.size:{}",datums.size());
			datumRepository.save(datums);
		}
	}
	
	/**
	 * 推送回掉
	 * @author admin
	 *
	 */
	private class PushResult{
		private int code;
		private String depict;
		private Object data;
	}
	/**
	 * 根据state返回进度
	 * @param state
	 * @return
	 */
	public static String progress(int state){
		String progress=null;
		switch(state){
		case Business.STATE_1:
			progress="初审资料待提交";
			break;
		case Business.STATE_2:
			progress="初审资料待审核";
			break;
		case Business.STATE_3:
			progress="初审资料通过";
			break;
		case Business.STATE_4:
			progress="初审资料驳回修改";
			break;
		case Business.STATE_5:
			progress="初审资料拒绝";
			break;
		case Business.STATE_STEP1_6:
			progress="签约资料已提交";
			break;
		case Business.STATE_STEP1_7:
			progress="签约资料通过";
			break;
		case Business.STATE_STEP1_8:
			progress="签约资料驳回修改";
			break;
		case Business.STATE_STEP1_9:
			progress="签约资料拒绝";
			break;
		case Business.STATE_STEP2_10:
			progress="放款资料已提交";
			break;
		case Business.STATE_STEP2_11:
			progress="放款资料通过";
			break;
		case Business.STATE_STEP2_12:
			progress="放款资料驳回修改";
			break;
		case Business.STATE_STEP2_13:
			progress="放款资料拒绝";
			break;
		}
		return progress;
	}
	@Override
	public long countByParidState(long parid, int state) {
		long count=0;
		if(state==0){
			count=businessRepository.countByParidAndIsdel(parid,0);
		}else{
			count=businessRepository.countByParidAndStateAndIsdel(parid, state,0);
		}
		return count;
	}

	@Override
	public List<Business> listByParidState(long parid, int state, int page,int size) {
		Sort sort=new Sort(Direction.DESC,"time");
		List<Business> businesss=null;
		if(state==0){
			businesss=businessRepository.findByParidAndIsdel(parid,0, new PageRequest(page, size, sort)).getContent();
		}else{
			businesss=businessRepository.findByParidAndStateAndIsdel(parid, state,0, new PageRequest(page, size, sort)).getContent();
		}
		return businesss;
	}
	/**
	 * 代理合作商系统修改pstate
	 * 合作商系统审核面签流程
	 * @throws IllegalBusinessStateException 
	 */
	@Override
	public void updByCodePstate(String code, int pstate,String depict) throws IllegalBusinessStateException {
		Business business=businessRepository.findByCode(code);
		if(business.getState().intValue()!=Business.STATE_3&&business.getState().intValue()!=Business.STATE_STEP1_6&&business.getState().intValue()!=Business.STATE_STEP1_8){
			//如果当前状态非初审通过，或者非待审核，或者非面签驳回状态，不可修改面签状态
			throw new IllegalBusinessStateException("illegal business state");
		}
		if(pstate==Business.STATE_STEP1_7){//如果面签通过，初始化数据
			ThreadPool.getThreadPoolExecutor().execute(new InitDatumThread(business.getBusid()));
		}
		business.setState(pstate);
		business.setPstate(pstate);
		business.setRemark(depict);
		//创建审核记录
		verifyRepository.save(new Verify(business.getBusid(), "ProxySystem", BusinessServiceImpl.progress(pstate), depict, System.currentTimeMillis()));
		businessRepository.saveAndFlush(business);
	}

	@Override
	public long countByState(int state) {
		long count=0;
		if(state==0){
			count=businessRepository.countByIsdel(0);
		}else{
			count=businessRepository.countByStateAndIsdel(state,0);
		}
		return count;
	}

	@Override
	public List<Business> listByState(int state, int page, int size) {
		Sort sort=new Sort(Direction.DESC,"time");
		List<Business> businesss=null;
		if(state==0){
			businesss=businessRepository.findByIsdel(0,new PageRequest(page, size, sort)).getContent();
		}else{
			businesss=businessRepository.findByStateAndIsdel(state,0, new PageRequest(page, size, sort)).getContent();
		}
		return businesss;
	}

	@Override
	public long countByParidAndStateAndSurnameAndIdcard(long parid, int state,String surname, String idcard) {
		long count=0;
		if(state==0){
			count=businessRepository.countByParidAndSurnameLikeOrIdcardLike(parid, surname, idcard);
		}else{
			count=businessRepository.countByParidAndStateAndSurnameLikeOrIdcardLike(parid, state, surname, idcard);
		}
		return count;
	}

	@Override
	public List<Business> listByParidAndStateAndSurnameAndIdcard(long parid,int state, String surname, String idcard, int page, int size) {
		int begin=page*size;
		int end=(page+1)*size;
		List<Business> businesss=null;
		if(state==0){
			businesss=businessRepository.findByParidAndSurnameLikeOrIdcardLikeOrderByTimeDesc(parid, surname, idcard);
		}else{
			businesss=businessRepository.findByParidAndStateAndSurnameLikeOrIdcardLikeOrderByTimeDesc(parid, state, surname, idcard);
		}
		return businesss;
	}

	@Override
	public long countByStateAndSurnameAndIdcard(int state, String surname,String idcard) {
		long count=0;
		if(state==0){
			count=businessRepository.countBySurnameLikeOrIdcardLike(surname, idcard);
		}else{
			count=businessRepository.countByStateAndSurnameLikeOrIdcardLike(state, surname, idcard);
		}
		return count;
	}

	@Override
	public List<Business> listByStateAndSurnameAndIdcard(int state,String surname, String idcard, int page, int size) {
		int begin=page*size;
		int end=(page+1)*size;
		List<Business> businesss=null;
		if(state==0){
			businesss=businessRepository.findBySurnameLikeOrIdcardLikeOrderByTimeDesc(surname, idcard);
		}else{
			businesss=businessRepository.findByStateAndSurnameLikeOrIdcardLikeOrderByTimeDesc(state, surname, idcard);
		}
		return businesss;
	}

	@Override
	public long countByBanidAndStateAndSurnameAndIdcard(long banid, int state,String surname, String idcard) {
		long count=0;
		if(state==0){
			count=businessRepository.countByBanidAndSurnameLikeOrIdcardLike(banid, surname, idcard);
		}else{
			count=businessRepository.countByBanidAndStateAndSurnameLikeOrIdcardLike(banid, state, surname, idcard);
		}
		return count;
	}

	@Override
	public List<Business> listByBanidAndStateAndSurnameAndIdcard(long banid,int state, String surname, String idcard, int page, int size) {
		int begin=page*size;
		int end=(page+1)*size;
		List<Business> businesss=null;
		if(state==0){
			businesss=businessRepository.findByBanidAndSurnameLikeOrIdcardLikeOrderByTimeDesc(banid, surname, idcard);
		}else{
			businesss=businessRepository.findByBanidAndStateAndSurnameLikeOrIdcardLikeOrderByTimeDesc(banid, state, surname, idcard);
		}
		return businesss;
	}

	@Override
	public void updAcodeByBusid(long busid, String acode) {
		Business business=businessRepository.findOne(busid);
		if(StringUtils.isEmpty(business)){
			log.error("business is null");
			return;
		}
		business.setAcode(acode);
		businessRepository.saveAndFlush(business);
	}
	@Override
	public void updByBusidAndIsimgerr(long busid, String remark) {
		Business business=businessRepository.findOne(busid);
		business.setIsimgerr(1);
		business.setRemark(remark);
		businessRepository.saveAndFlush(business);
		
	}
	@Override
	public List<Business> exports() {
		List<Business> businesss=businessRepository.findByStateAndIsdel(Business.STATE_STEP1_7,0);
		for(Business business:businesss){
			business.setRemark("ok");
			List<Datum> datums=datumRepository.findByBusid(business.getBusid());
			Datum RELCUSTNUM=DatumServiceImpl.findDatumByName(datums,"RELCUSTNUM");//关联客户信息条数
			for(Datum datum:datums){
				if(Integer.valueOf(RELCUSTNUM.getValue())==0&&datum.getMenid().longValue()==DatumServiceImpl.MENID_100){//如果关联信息条数为0并且这个数据属于关联信息，跳过
					continue;
				}
				if(StringUtils.isEmpty(datum.getValue())&&datum.getIsrequired()==1){//值为空并且这个值是必须的
					business.setRemark(datum.getNamedepict());
				}
			}
		}
		return businesss;
	}
	@Override
	public List<Business> listByAcode(String acode) {
		List<Business> businesss=businessRepository.findByAcode(acode);
		return businesss;
	}
	

}
