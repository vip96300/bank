package org.cloud.bank.client.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.exception.IllegalBusinessStateException;
import org.cloud.bank.client.model.Business;
import org.cloud.bank.client.model.Operator;
import org.cloud.bank.client.model.User;
import org.cloud.bank.client.service.BusinessService;
import org.cloud.bank.client.service.OperatorService;
import org.cloud.bank.client.service.UserService;
import org.cloud.bank.client.service.VerifyService;
import org.cloud.bank.client.task.FileSyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/business")
public class BusinessController {
	
	private static final Logger logger = LoggerFactory.getLogger(BusinessController.class);
	
	@Autowired
	private BusinessService businessService;
	@Autowired
	private UserService userService;
	@Autowired
	private VerifyService verifyService;
	@Autowired
	private OperatorService operatorService;
	@Autowired
	private FileSyncTask fileSyncTask;
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="idcard",required=true)String idcard,
			@RequestParam(value="telephone",required=true)String telephone,
			@RequestParam(value="opeid",required=true)long opeid,
			@RequestParam(value="ismarry",required=true)int ismarry){
		List<Business> businesss=businessService.listByIdcardAndCurdate(idcard);
		if(!businesss.isEmpty()){
			//身份证当日不可重复
			//return new Result<Object>(501,"idcard in curdate cant be repeat",null);
		}
		Business business=new Business();
		business.setOpeid(opeid);
		business.setSurname(surname);
		business.setIdcard(idcard);
		business.setTelephone(telephone);
		business.setIsmarry(ismarry);
		business=businessService.add(business);
		return new Result<Object>(200,null,business);
	}
	/**
	 * 修改业务基本信息
	 * @return
	 */
	@RequestMapping(value="/upd_busid",method=RequestMethod.POST)
	public Result<Object> upd_busid(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="idcard",required=true)String idcard,
			@RequestParam(value="telephone",required=true)String telephone,
			@RequestParam(value="ismarry",required=true)int ismarry,
			@RequestHeader(value="token",required=true)String token){
		Business business=businessService.getByBusid(busid);
		Assert.notNull(business,"business cant be null");
		business.setSurname(surname);
		business.setIdcard(idcard);
		business.setTelephone(telephone);
		business.setIsmarry(ismarry);
		businessService.updByBusid(business);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value="/del_busid",method=RequestMethod.POST)
	public Result<Object> del_busid(@RequestParam(value="busid",required=true)long busid){
		businessService.delByBusid(busid);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/count_banid_state",method=RequestMethod.POST)
	public Result<Object> count_banid_state(@RequestParam(value="state",required=true)int state,
			HttpServletRequest request){
		long banid=0;
		if(StringUtils.isEmpty(request.getParameter("banid"))){
			User user=userService.getByUseid(Long.valueOf(request.getHeader("userid")));
			banid=user.getBanid();
		}else{
			banid=Long.valueOf(request.getParameter("banid"));
		}
		long count=businessService.countByBanidState(banid,state);
		return new Result<Object>(200,null,count);
	}
	@RequestMapping(value="/list_banid_state",method=RequestMethod.POST)
	public Result<List> list_banid_state(@RequestParam(value="state",required=true)int state,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size,
			HttpServletRequest request){
		long banid=0;
		if(StringUtils.isEmpty(request.getParameter("banid"))){
			User user=userService.getByUseid(Long.valueOf(request.getHeader("userid")));
			banid=user.getBanid();
		}else{
			banid=Long.valueOf(request.getParameter("banid"));
		}
		List<Business> businesss=businessService.listByBanidState(banid,state,page,size);
		return new Result<List>(200,null,businesss);
	}
	/**
	 * 根据状态获取业务列表
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/list_opeid_state",method=RequestMethod.POST)
	public Result<List> list_opeid_state(@RequestParam(value="opeid",required=true)long opeid,
			@RequestParam(value="state",required=true)int state,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Business> businesss=businessService.listByOpeidState(opeid,state,page,size);
		return new Result<List>(200,null,businesss);
	}
	
	@RequestMapping(value="/list_opeid_code_surname",method=RequestMethod.POST)
	public Result<List> list_opeid_code_surname(@RequestParam(value="opeid",required=true)long opeid,
			@RequestParam(value="code",required=true)String code,
			@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Business> businesss=businessService.listByOpeidCodeSurname(opeid,code,surname,page,size);
		return new Result<List>(200,null,businesss);
	}
	
	@RequestMapping(value="/list_opeid_idcard_surname",method=RequestMethod.POST)
	public Result<List> list_opeid_idcard_surname(@RequestParam(value="opeid",required=true)long opeid,
			@RequestParam(value="idcard",required=true)String idcard,
			@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Business> businesss=businessService.listByOpeidIdcardSurname(opeid,idcard,surname,page,size);
		return new Result<List>(200,null,businesss);
	}
	
	@RequestMapping(value="/get_busid",method=RequestMethod.POST)
	public Result<Object> get_busid(@RequestParam(value="busid",required=true)long busid){
		Business business=businessService.getByBusid(busid);
		return new Result<Object>(200,null,business);
	}
	/**
	 * 录入申请编号
	 * @return
	 */
	@UserAuthor(level=1)
	@RequestMapping(value="/upd_acode_busid",method=RequestMethod.POST)
	public Result<Object> upd_acode_busid(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="acode",required=true)String acode){
		List<Business> businesss=businessService.listByAcode(acode);
		for(Business business:businesss){
			if(!business.getBusid().equals(busid)){
				return new Result<Object>(500,"acode exist",null);
			}
		}
		businessService.updAcodeByBusid(busid,acode);
		return new Result<Object>(200,null,null);
	}
	/**
	 * @param busid
	 * @param state
	 * @param remark
	 * @param acode
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/upd_busid_state",method=RequestMethod.POST)
	public Result<Object> upd_busid_state(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="state",required=true)int state,
			@RequestParam(value="remark",required=false,defaultValue="0")String remark,
			@RequestParam(value="token",required=true)String token){
		businessService.updByBusidState(busid,state,remark,token);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/count_parid_state",method=RequestMethod.POST)
	public Result<Object> count_parid_state(@RequestParam(value="parid",required=true)long parid,
			@RequestParam(value="state",required=true)int state) {
		long count=businessService.countByParidState(parid, state);
		return new Result<Object>(200,null,count);
	}
	
	@RequestMapping(value="/list_parid_state",method=RequestMethod.POST)
	public Result<List> list_parid_state(@RequestParam(value="parid",required=true)long parid,
			@RequestParam(value="state",required=true)int state,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size) {
		List<Business> businesss=businessService.listByParidState(parid, state, page, size);
		return new Result<List>(200,null,businesss);
	}
	@UserAuthor(level=0)
	@RequestMapping(value="/count_state",method=RequestMethod.POST)
	public Result<Object> count_state(@RequestParam(value="state",required=true)int state) {
		long count=businessService.countByState(state);
		return new Result<Object>(200,null,count);
	}
	@UserAuthor(level=0)
	@RequestMapping(value="/list_state",method=RequestMethod.POST)
	public Result<List> list_state(@RequestParam(value="state",required=true)int state,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Business> businesss=businessService.listByState(state,page,size);
		return new Result<List>(200,null,businesss);
	}
	
	@UserAuthor(level=1)//因为是银行端初审带过去的token，所以需要验证用户为银行端用户
	@RequestMapping(value="/upd_code_pstate",method=RequestMethod.POST)
	public Result<Object> upd_code_pstate(@RequestParam(value="code",required=true)String code,
			@RequestParam(value="pstate",required=true)int pstate,
			@RequestParam(value="depict",required=true)String depict){
		try {
			businessService.updByCodePstate(code,pstate,depict);
		} catch (IllegalBusinessStateException e) {
			return new Result<Object>(500,e.getMessage(),null);
		}
		return new Result<Object>(200,null,null);
	}
	/**
	 * 合作商管理后台通过姓名或身份证号搜索
	 * @return
	 */
	@RequestMapping(value="/count_parid_state_surname_idcard",method=RequestMethod.POST)
	public Result<Long> count_parid_state_surname_idcard(@RequestHeader(value="userid",required=true)long parid,
			@RequestParam(value="state",required=true)int state,
			@RequestParam(value="surname",required=false)String surname,
			@RequestParam(value="idcard",required=false)String idcard){
		long count=businessService.countByParidAndStateAndSurnameAndIdcard(parid,state,surname,idcard);
		return new Result<Long>(200,null,count);
	}
	/**
	 * 合作商管理后台通过姓名或身份证号搜索
	 * @return
	 */
	@RequestMapping(value="/list_parid_state_surname_idcard",method=RequestMethod.POST)
	public Result<List> list_parid_state_surname_idcard(@RequestHeader(value="userid",required=true)long parid,
			@RequestParam(value="state",required=true)int state,
			@RequestParam(value="surname",required=false)String surname,
			@RequestParam(value="idcard",required=false)String idcard,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Business> businesss=businessService.listByParidAndStateAndSurnameAndIdcard(parid,state,surname,idcard,page,size);
		return new Result<List>(200,null,businesss);
	}
	/**
	 * 银行管理超级管理员后台根据状态，姓名，身份号搜索业务
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/count_state_surname_idcard",method=RequestMethod.POST)
	public Result<Long> count_state_surname_idcard(@RequestParam(value="state",required=true)int state,
			@RequestParam(value="surname",required=false)String surname,
			@RequestParam(value="idcard",required=false)String idcard){
		long count=businessService.countByStateAndSurnameAndIdcard(state,surname,idcard);
		return new Result<Long>(200,null,count);
	}
	/**
	 * 银行管理后台超级管理员根据状态，姓名，身份号搜索业务
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/list_state_surname_idcard",method=RequestMethod.POST)
	public Result<List> list_state_surname_idcard(@RequestParam(value="state",required=true)int state,
			@RequestParam(value="surname",required=false)String surname,
			@RequestParam(value="idcard",required=false)String idcard,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Business> businesss=businessService.listByStateAndSurnameAndIdcard(state,surname,idcard,page,size);
		return new Result<List>(200,null,businesss);
	}
	/**
	 * 银行管理普通管理员后台根据状态，姓名，身份号搜索业务
	 * @return
	 */
	@UserAuthor(level=1)
	@RequestMapping(value="/count_banid_state_surname_idcard",method=RequestMethod.POST)
	public Result<Long> count_banid_state_surname_idcard(@RequestHeader(value="userid",required=true)long useid,
			@RequestParam(value="state",required=true)int state,
			@RequestParam(value="surname",required=false)String surname,
			@RequestParam(value="idcard",required=false)String idcard){
		User user=userService.getByUseid(useid);
		long count=businessService.countByBanidAndStateAndSurnameAndIdcard(user.getBanid(),state,surname,idcard);
		return new Result<Long>(200,null,count);
	}
	/**
	 * 银行管理后台普通管理员根据状态，姓名，身份号搜索业务
	 * @return
	 */
	@UserAuthor(level=1)
	@RequestMapping(value="/list_banid_state_surname_idcard",method=RequestMethod.POST)
	public Result<List> list_banid_state_surname_idcard(@RequestHeader(value="userid",required=true)long useid,
			@RequestParam(value="state",required=true)int state,
			@RequestParam(value="surname",required=false)String surname,
			@RequestParam(value="idcard",required=false)String idcard,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		User user=userService.getByUseid(useid);
		List<Business> businesss=businessService.listByBanidAndStateAndSurnameAndIdcard(user.getBanid(),state,surname,idcard,page,size);
		return new Result<List>(200,null,businesss);
	}
	/**
	 * 文件驳回
	 * @return
	 */
	@UserAuthor(level=1)
	@RequestMapping(value="/upd_busid_isimgerr",method=RequestMethod.POST)
	public Result<Object> upd_busid_isimgerr(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="remark")String remark){
		businessService.updByBusidAndIsimgerr(busid,remark);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 导出，只有在可导出状态的业务
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/exports",method=RequestMethod.POST)
	public Result<List> exports(){
		List<Business> businesss=businessService.exports();
		return new Result<List>(200,null,businesss);
	}
	/**
	 * 同步文件
	 * @return
	 */
	@RequestMapping(value="/sync",method=RequestMethod.POST)
	public Result<Object> sync(){
		fileSyncTask.fileSync();
		return new Result<Object>(200,null,null);
	}
}
