package org.cloud.bank.client.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.User;
import org.cloud.bank.client.service.UserService;
import org.cloud.bank.client.util.JwtUtil;
import org.cloud.bank.client.util.Md5Util;
import org.cloud.bank.client.util.ValidUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserService userService;

    @RequestMapping(value="/login",method=RequestMethod.POST)
    public Result<Object> login(@RequestParam(value="username",required=true)String username,
    		@RequestParam(value="password",required=true)String password) {
    	User user=userService.getByUsernamePassword(username, password);
    	if(!ValidUtil.isValid(user)){
    		System.out.println("user is null");
    		return new Result<Object>(500,"username or password error",null);
    	}
    	if(user.getIsenable()==0){
    		return new Result<Object>(501,"user not enable",null);
    	}
    	Map<String,Object> headerParams=new HashMap<String,Object>();
    	headerParams.put("alg", "HS256");
    	headerParams.put("type", "jwt");
    	Map<String,Object> claims=new HashMap<String,Object>();
    	claims.put("username",user.getUsername());
    	claims.put("password",user.getPassword());
    	claims.put("userid", user.getUseid());
    	DateTime dt = new DateTime();
		Date expiration = dt.plusDays(7).toDate();
    	claims.put("expiration", expiration);
    	String token=JwtUtil.tokenGenerator(headerParams, claims);
    	return new Result<Object>(200,null,token);
    }
   
    @RequestMapping(value="/get_useid",method=RequestMethod.POST)
    public Result<Object> get_useid(HttpServletRequest request){
    	User user=userService.getByUseid(Long.valueOf(request.getHeader("userid")));
    	return new Result<Object>(200,null,user);
    }
    @RequestMapping(value="/count",method=RequestMethod.POST)
    public Result<Object> count(){
    	long count=userService.count();
    	return new Result<Object>(200,null,count);
    }
    
    @RequestMapping(value="/list",method=RequestMethod.POST)
    public Result<List> list(@RequestParam(value="page",required=true)int page,
    		@RequestParam(value="size",required=true)int size){
    	List<User> users=userService.list(page,size);
    	return new Result<List>(200,null,users);
    }

    @RequestMapping(value="/logout",method=RequestMethod.POST)
    public Result<Object> logout() {
    	logger.info("logout");
        return new Result<Object>(200,null,null);
    }
    /**
     * 禁用/启用
     * @param useid
     * @return
     */
    @org.cloud.bank.client.annotation.UserAuthor(level=0)
    @RequestMapping(value="/upd_useid_isenable",method=RequestMethod.POST)
    public Result<Object> upd_useid_isenable(@RequestParam(value="useid",required=true)long useid){
    	userService.updByUseidIsenable(useid);
    	return new Result<Object>(200,null,null);
    }
    /**
     * 重置密码
     * @param useid
     * @return
     */
    @RequestMapping(value="/upd_useid_password",method=RequestMethod.POST)
    public Result<Object> upd_useid_password(@RequestParam(value="useid",required=true)long useid){
    	userService.updByUseidPassword(useid);
    	return new Result<Object>(200,null,null);
    }
    
    /**
     * 删除用户
     * @param useid
     * @return
     */
    @org.cloud.bank.client.annotation.UserAuthor(level=0)
    @RequestMapping(value="/del_useid",method=RequestMethod.POST)
    public Result<Object> del_useid(@RequestParam(value="useid",required=true)long useid){
    	userService.delByUseid(useid);
    	return new Result<Object>(200,null,null);
    }
    /**
     * 添加用户
     * @param username
     * @return
     */
    @org.cloud.bank.client.annotation.UserAuthor(level=0)
    @RequestMapping(value="/add",method=RequestMethod.POST)
    public Result<Object> add(@RequestParam(value="username",required=true)String username,
    		@RequestParam(value="surname",required=true)String surname,
    		@RequestParam(value="banid",required=true)long banid){
    	User user=new User();
    	user.setUsername(username);
    	user.setSurname(surname);
    	user.setBanid(banid);
    	try {
    		userService.add(user);
		} catch (Exception e) {
			return new Result<Object>(500,"username already exist",null);
		}
    	return new Result<Object>(200,null,null);
    }
    /**
     * 修改信息
     * @return
     */
    @org.cloud.bank.client.annotation.UserAuthor(level=0)
    @RequestMapping(value="/upd_useid",method=RequestMethod.POST)
    public Result<Object> upd_useid(@RequestParam(value="useid",required=true)long useid,
    		@RequestParam(value="username",required=true)String username,
    		@RequestParam(value="surname",required=true)String surname){
    	User user=new User();
    	user.setUseid(useid);
    	user.setUsername(username);
    	user.setSurname(surname);
    	try {
    		userService.updByUseid(user);
		} catch (SQLIntegrityConstraintViolationException e) {
			return new Result<Object>(500,e.getMessage(),null);
		}
    	return new Result<Object>(200,null,null);
    }
    
    /**
     * 根据用户名修改密码
     * @return
     */
    @RequestMapping(value="/upd_username_password",method=RequestMethod.POST)
    public Result<Object> upd_username_password(@RequestParam(value="username",required=true)String username,
    		@RequestParam(value="password",required=true)String password,
    		@RequestParam(value="newpassword",required=true)String newpassword){
    	User user=userService.getByUsernamePassword(username, password);
    	if(StringUtils.isEmpty(user)){
    		return new Result<Object>(500,"username or password error",null);
    	}
    	userService.updByUsernamePassword(username, newpassword);
    	return new Result<Object>(200,null,null);
    }

}
