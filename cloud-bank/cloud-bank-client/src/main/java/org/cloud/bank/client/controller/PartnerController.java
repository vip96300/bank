package org.cloud.bank.client.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Partner;
import org.cloud.bank.client.service.PartnerService;
import org.cloud.bank.client.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value="/bank/bank/partner")
public class PartnerController {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerController.class);
	
	@Autowired
	private PartnerService partnerService;
	
	@RequestMapping(value="/count",method=RequestMethod.POST)
	public Result<Object> count(){
		long count=partnerService.count();
		return new Result<Object>(200,null,count);
	}
	/**
	 * 列表
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public Result<List> list(@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Partner> partners=partnerService.list(page,size);
		return new Result<List>(200,null,partners);
	}
	/**
	 * 根据银行编号获取合作商列表
	 * @return
	 */
	@RequestMapping(value="/list_banid",method=RequestMethod.POST)
	public Result<List> list_banid(@RequestParam(value="banid",required=true)long banid){
		List<Partner> partners=partnerService.listByBanid(banid);
		return new Result<List>(200,null,partners);
	}
	/**
	 * 添加合作商
	 * @param banid
	 * @param username
	 * @param surname
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="banid",required=true)long banid,
			@RequestParam(value="username",required=true)String username,
			@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="hostname",required=true)String hostname,
			@RequestParam(value="isproxy",required=true)int isproxy,
			@RequestParam(value="token",required=true)String token){
		Partner partner=new Partner();
		partner.setBanid(banid);
		partner.setUsername(username);
		partner.setSurname(surname);
		partner.setHostname(hostname);
		partner.setIsproxy(isproxy);
		boolean isenable=this.healthCheck(hostname, token);
		if(!isenable){
			return new Result<Object>(500,null,null);
		}
		partnerService.add(partner);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 添加或合作商时检查合作商服务器
	 * @param hostname
	 * @param token
	 * @return
	 */
	private boolean healthCheck(String hostname,String token){
		boolean isenable=false;
		Map<String,String> params=new HashMap<String,String>();
		params.put("token", token);
		try {
			String json=RequestUtil.request(hostname+"/partner/bank/bank/partner/health/check", params);
			if(new Gson().fromJson(json, HealthCheckResult.class).code==200){
				isenable=true;
			}
		} catch (Exception e) {
			logger.error("partner server not enable,hostname={}",hostname);
		}
		return isenable;
	}
	private class HealthCheckResult{
		private int code;
		private String depict;
		private Object data;
	}
	/**
	 * 启用/禁用
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/upd_parid_isenable",method=RequestMethod.POST)
	public Result<Object> upd_parid_isenable(@RequestParam(value="parid",required=true)long parid){
		partnerService.updByParidIsenable(parid);
		return new Result<Object>(200,null,null);
	}
	
	/**
	 * 重置密码
	 * @return
	 */
	@RequestMapping(value="/upd_parid_password",method=RequestMethod.POST)
	public Result<Object> upd_parid_password(@RequestParam(value="parid",required=true)long parid){
		partnerService.updByParidPassword(parid);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 修改信息
	 */
	@RequestMapping(value="/upd_parid",method=RequestMethod.POST)
	public Result<Object> upd_parid(@RequestParam(value="parid",required=true)long parid,
			@RequestParam(value="surname",required=true)String surname,
			@RequestParam(value="hostname",required=true)String hostname,
			@RequestParam(value="noticeurl",required=true)String noticeurl,
			@RequestParam(value="token",required=true)String token){
		Partner partner=new Partner();
		partner.setParid(parid);
		partner.setSurname(surname);
		partner.setHostname(hostname);
		partner.setNoticeurl(noticeurl);
		boolean isenable=this.healthCheck(hostname, token);
		if(!isenable){
			return new Result<Object>(500,null,null);
		}
		partnerService.updByParid(partner);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 删除
	 * @param parid
	 * @return
	 */
	@UserAuthor(level=0)
	@RequestMapping(value="/del_parid",method=RequestMethod.POST)
	public Result<Object> del_parid(@RequestParam(value="parid",required=true)long parid){
		partnerService.delByParid(parid);
		return new Result<Object>(200,null,null);
	}
   /**
    * 合作商登陆
    * @param username
    * @param password
    * @return
    */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Result<Object> login(@RequestParam(value="username",required=true)String username,
			@RequestParam(value="password",required=true)String password){
		String token=partnerService.login(username,password);
		return new Result<Object>(200,null,token);
	}
	/**
	 * 根据用户名和密码获取合作商
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/get_username_password",method=RequestMethod.POST)
	public Result<Object> get_username_password(@RequestParam(value="username",required=true)String username,
			@RequestParam(value="password",required=true)String password) {
		Partner partner=partnerService.getByUsernamePassword(username,password);
		return new Result<Object>(200,null,partner);
	}
	/**
	 * 修改密码
	 * @param username
	 * @param password
	 */
	@RequestMapping(value="/upd_username_password",method=RequestMethod.POST)
	public Result<Object> upd_username_password(@RequestParam(value="username",required=true)String username,
			@RequestParam(value="password",required=true)String password){
		partnerService.updByUsernamePassword(username, password);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/get_parid",method=RequestMethod.POST)
	public Result<Object> get_parid(@RequestParam(value="parid",required=true)long parid){
		Partner partner=partnerService.getByParid(parid);
		return new Result<Object>(200,null,partner);
	}
}
