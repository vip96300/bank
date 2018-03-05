package org.cloud.bank.client.service;

import java.util.List;

import org.cloud.bank.client.exception.IllegalBusinessStateException;
import org.cloud.bank.client.model.Business;
import org.springframework.transaction.annotation.Transactional;

public interface BusinessService {
	
	@Transactional(rollbackFor=Exception.class)
	public Business add(Business business);
	
	public void updByBusid(Business business);
	
	/**
	 * 根据身份证号和当前日期获取业务
	 * @param idcard
	 * @return
	 */
	public List<Business> listByIdcardAndCurdate(String idcard);
	
	public void delByBusid(long busid);
	
	public long countByBanidState(long banid,int state);
	
	public List<Business> listByBanidState(long banid,int state,int page,int size);
	
	public List<Business> listByOpeidState(long opeid,int state,int page,int size);
	
	public List<Business> listByOpeidCodeSurname(long opeid,String code,String surname,int page,int size);
	
	public List<Business> listByOpeidIdcardSurname(long opeid,String idcard,String surname,int page,int size);
	
	public Business getByBusid(long busid);
	
	@Transactional(rollbackFor=Exception.class)
	public void updAcodeByBusid(long busid,String acode);
	
	@Transactional(rollbackFor=Exception.class)
	public void updByBusidState(long busid,int state,String remark,String token);

	public long countByParidState(long parid,int state);
	
	public List<Business> listByParidState(long parid,int state,int page,int size);
	
	@Transactional(rollbackFor=Exception.class)
	public void updByCodePstate(String code,int pstate,String depict) throws IllegalBusinessStateException;
	
	public long countByState(int state);
	
	public List<Business> listByState(int state,int page,int size);
	
	public long countByParidAndStateAndSurnameAndIdcard(long parid,int state,String surname,String idcard);
	
	public List<Business> listByParidAndStateAndSurnameAndIdcard(long parid,int state,String surname,String idcard,int page,int size);
	
	public long countByStateAndSurnameAndIdcard(int state,String surname,String idcard);
	
	public List<Business> listByStateAndSurnameAndIdcard(int state,String surname,String idcard,int page,int size);
	
	public long countByBanidAndStateAndSurnameAndIdcard(long banid,int state,String surname,String idcard);
	
	public List<Business> listByBanidAndStateAndSurnameAndIdcard(long banid,int state,String surname,String idcard,int page,int size);
	
	public void updByBusidAndIsimgerr(long busid,String remark);
	
	public List<Business> exports();
	
	public List<Business> listByAcode(String acode);
	
}
