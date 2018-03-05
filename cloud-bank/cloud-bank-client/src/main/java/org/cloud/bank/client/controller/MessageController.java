package org.cloud.bank.client.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.cloud.bank.client.annotation.UserAuthor;
import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.model.Message;
import org.cloud.bank.client.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/bank/user/message")
public class MessageController {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private MessageService messageService;
	
	/**
	 * 发送消息
	 * @param type 0合作商，1操作员
	 * @param addressorid
	 * @param addresseeid
	 * @param title
	 * @param content
	 * @return
	 */
	@UserAuthor(level=1)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="parid",required=true)long parid,
			@RequestParam(value="opeid",required=true)long opeid,
			@RequestParam(value="title",required=true)String title,
			@RequestParam(value="content",required=true)String content,
			@RequestParam(value="token",required=true)String token,
			HttpServletRequest request){
		Message message=new Message();	
		message.setUseid(Long.valueOf(request.getHeader("userid")));
		message.setParid(parid);
		message.setOpeid(opeid);
		message.setTitle(title);
		message.setContent(content);
		messageService.add(message,token);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 根据编号获取消息
	 * @return
	 */
	@RequestMapping(value="/get_mesid",method=RequestMethod.POST)
	public Result<Object> get_mesid(@RequestParam(value="mesid",required=true)long mesid){
		Message message=messageService.getByMesid(mesid);
		return new Result<Object>(200,null,message);
	}
	/**
	 * 根据用户编号统计消息数量
	 * @return
	 */
	@RequestMapping(value="/count_useid",method=RequestMethod.POST)
	public Result<Object> count_useid(HttpServletRequest request){
		long count=messageService.countByUseid(Long.valueOf(request.getHeader("userid")));
		return new Result<Object>(200,null,count);
	}
	/**
	 * 根据用户编号获取邮件列表
	 * @return
	 */
	@RequestMapping(value="/list_useid",method=RequestMethod.POST)
	public Result<List> list_useid(@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size,
			HttpServletRequest request){
		List<Message> messages=messageService.listByUseid(Long.valueOf(request.getHeader("userid")),page,size);
		return new Result<List>(200,null,messages);
	}
	/**
	 * 删除消息
	 * @param mesid
	 * @return
	 */
	@RequestMapping(value="/del_mesid",method=RequestMethod.POST)
	public Result<Object> del_mesid(@RequestParam(value="mesid",required=true)long mesid){
		messageService.delByMesid(mesid);
		return new Result<Object>(200,null,null);
	}
	/**
	 * 根据合作商获编号取消息数量
	 * @return
	 */
	@RequestMapping(value="/count_parid",method=RequestMethod.POST)
	public Result<Object> count_parid(@RequestParam(value="parid",required=true)long parid){
		long count=messageService.countByParid(parid);
		return new Result<Object>(200,null,count);
	}
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="/list_parid",method=RequestMethod.POST)
	public Result<List> list_parid(@RequestParam(value="parid",required=true)long parid,
			@RequestParam(value="page",required=true)int page,
			@RequestParam(value="size",required=true)int size){
		List<Message> messages=messageService.listByParid(parid,page,size);
		return new Result<List>(200,null,messages);
	}
	
	@RequestMapping(value="/list_opeid",method=RequestMethod.POST)
	public Result<List> list_opeid(@RequestParam(value="opeid",required=true)long opeid,
			@RequestParam(value="page")int page,
			@RequestParam(value="size")int size) {
		List<Message> messages=messageService.listByOpeid(opeid,page,size);
		return new Result<List>(200,null,messages);
	}
}
