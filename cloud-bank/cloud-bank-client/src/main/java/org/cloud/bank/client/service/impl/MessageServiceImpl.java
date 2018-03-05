package org.cloud.bank.client.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloud.bank.client.model.Message;
import org.cloud.bank.client.model.Operator;
import org.cloud.bank.client.model.Partner;
import org.cloud.bank.client.model.User;
import org.cloud.bank.client.repository.MessageRepository;
import org.cloud.bank.client.repository.OperatorRepository;
import org.cloud.bank.client.repository.PartnerRepository;
import org.cloud.bank.client.repository.UserRepository;
import org.cloud.bank.client.service.MessageService;
import org.cloud.bank.client.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MessageServiceImpl implements MessageService{
	
	private static final Logger log=LoggerFactory.getLogger(MessageServiceImpl.class);
	
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PartnerRepository partnerRepository;
	@Autowired
	private OperatorRepository operatorRepository;

	@Override
	public void add(Message message,String token) {
		User user=userRepository.findOne(message.getUseid());
		if(StringUtils.isEmpty(user)){
			return;
		}
		message.setAddressor(user.getSurname());
		Partner partner=partnerRepository.findOne(message.getParid());
		if(StringUtils.isEmpty(partner)){
			return;
		}
		Operator operator=operatorRepository.findOne(message.getOpeid());
		String addressee=partner.getSurname();
		if(!StringUtils.isEmpty(operator)){
			addressee+=operator.getSurname();
		}
		message.setAddressee(addressee);
		message.setTime(System.currentTimeMillis());
		Map<String,String> params=new HashMap<String,String>();
		params.put("useid",String.valueOf(message.getUseid()));
		params.put("parid",String.valueOf(message.getParid()));
		params.put("opeid",String.valueOf(message.getOpeid()));
		params.put("addressor",message.getAddressor());
		params.put("title",message.getTitle());
		params.put("content",message.getContent());
		params.put("token",token);
		log.debug(RequestUtil.request(partner.getHostname()+"/partner/bank/user/message/push", params));
		messageRepository.save(message);
	}

	@Override
	public Message getByMesid(long mesid) {
		Message message=messageRepository.findOne(mesid);
		return message;
	}
	
	@Override
	public long countByUseid(long useid) {
		long count=messageRepository.countByUseid(useid);
		return count;
	}
	
	@Override
	public List<Message> listByUseid(long useid, int page, int size) {
		List<Message> messages=messageRepository.findByUseidOrderByTimeDesc(useid, new PageRequest(page, size)).getContent();
		return messages;
	}

	@Override
	public void delByMesid(long mesid) {
		messageRepository.delete(mesid);
	}
	@Override
	public long countByParid(long parid) {
		long count=messageRepository.countByParidAndOpeid(parid,0);
		return count;
	}
	@Override
	public List<Message> listByParid(long parid, int page, int size) {
		List<Message> messages=messageRepository.findByParidAndOpeidOrderByTimeDesc(parid,0,new PageRequest(page, size)).getContent();
		return messages;
	}

	@Override
	public List<Message> listByOpeid(long opeid, int page, int size) {
		List<Message> messages=messageRepository.findByOpeidOrderByTimeDesc(opeid,new PageRequest(page, size)).getContent();
		return messages;
	}

}
