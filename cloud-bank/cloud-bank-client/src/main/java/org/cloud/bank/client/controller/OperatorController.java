package org.cloud.bank.client.controller;

import java.util.List;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Operator;
import org.cloud.bank.client.service.OperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/bank/partner/operator")
public class OperatorController {
	
	private static final Logger logger = LoggerFactory.getLogger(OperatorController.class);
	
	@Autowired
	private OperatorService operatorService;
	
	@UserAuthor(level=0)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="username",required=true)String username,
			@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="banid",required=true)long banid,
			@RequestParam(value="parid",required=true)long parid,
			@RequestParam(value="isadmin",required=true)int isadmin,
			@RequestParam(value="token",required=true)String token){
		Operator operator=new Operator();
		operator.setUsername(username);
		operator.setSurname(surname);
		operator.setBanid(banid);
		operator.setParid(parid);
		operator.setIsadmin(isadmin);
		try {
			operatorService.add(operator,token);
		} catch (Exception e) {
			return new Result<Object>(500,"add operator error",null);
		}
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/count",method=RequestMethod.POST)
	public Result<Object> count(){
		long count=operatorService.count();
		return new Result<Object>(200,null,count);
	}
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public Result<List> list(@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Operator> operators=operatorService.list(page,size);
		return new Result<List>(200,null,operators);
	}
	/**
	 * 根据合作商编号获取操作员列表
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/list_parid",method=RequestMethod.POST)
	public Result<List> list_parid(@RequestParam(value="parid",required=true)long parid){
		List<Operator> operators=operatorService.listByParid(parid);
		return new Result<List>(200,null,operators);
	}
	
	/**
	 * 启用/禁用
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/upd_opeid_isenable",method=RequestMethod.POST)
	public Result<Object> upd_opeid_isenable(@RequestParam(value="opeid",required=true)long opeid){
		operatorService.updByOpeidIsenable(opeid);
		return new Result<Object>(200,null,null);
	}
	
	/**
	 * 重置密码
	 * @return
	 */
	@RequestMapping(value="/upd_opeid_password",method=RequestMethod.POST)
	public Result<Object> upd_opeid_password(@RequestParam(value="opeid",required=true)long opeid){
		operatorService.updByOpeidPassword(opeid);
		return new Result<Object>(200,null,null);
	}
	
	/**
	 * 删除
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/del_opeid",method=RequestMethod.POST)
	public Result<Object> del_opeid(@RequestParam(value="opeid",required=true)long opeid){
		operatorService.delByOpeid(opeid);
		return new Result<Object>(200,null,null);
	}
	
	/**
	 * 修改
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/upd_opeid",method=RequestMethod.POST)
	public Result<Object> upd_opeid(@RequestParam(value="opeid",required=true)long opeid,
			@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="isadmin",required=true)int isadmin){
		Operator operator=new Operator();
		operator.setOpeid(opeid);
		operator.setSurname(surname);
		operator.setIsadmin(isadmin);
		operatorService.updByOpeid(operator);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 登陆
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Result<Object> login(@RequestParam(value="username",required=true)String username,
			@RequestParam(value="password",required=true)String password){
		String token=operatorService.login(username,password);
		return new Result<Object>(200,null,token);
	}
	
	@RequestMapping(value="/get_username_password",method=RequestMethod.POST)
	public Result<Object> get_username_password(@RequestParam(value="username",required=true)String username,
			@RequestParam(value="password",required=true)String password){
		Operator operator=operatorService.getByUsernamePassword(username,password);
		return new Result<Object>(200,null,operator);
	}
	
	@RequestMapping(value="/upd_username_password",method=RequestMethod.POST)
	public Result<Object> upd_username_password(@RequestParam(value="username",required=true)String username,
			@RequestParam(value="password",required=true)String password){
		operatorService.updByUsernamePassword(username,password);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/get_opeid",method=RequestMethod.POST)
	public Result<Object> get_opeid(@RequestParam(value="opeid",required=true)long opeid){
		Operator operator=operatorService.getByOpeid(opeid);
		return new Result<Object>(200,null,operator);
	}
	
	/**
	 * 获取操作管理员列表
	 * @return
	 */
	@RequestMapping(value="/list_banid_isadmin",method=RequestMethod.POST)
	public Result<List> list_banid_isadmin(@RequestParam(value="banid",required=true)long banid,
			@RequestParam(value="isadmin",required=true)int isadmin){
		List<Operator> operators=operatorService.listByBanidAndIsadmin(banid,isadmin);
		return new Result<List>(200,null,operators);
	}
	/**
	 * 修改操作员是否接听
	 * @param opeid
	 * @return
	 */
	@RequestMapping(value="/upd_islisten_opeid",method=RequestMethod.POST)
	public Result<Object> upd_islisten_opeid(@RequestParam(value="opeid",required=true)long opeid){
		operatorService.updIslistenByOpeid(opeid);
		return new Result<Object>(200,null,null);
	}
}
