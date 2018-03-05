package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_user_message")
public class Message extends BaseModel{

	/**
	 * 消息
	 */
	private static final long serialVersionUID = -2753423915406436704L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_user_message_g")  
	@SequenceGenerator(name="bank_user_message_g", sequenceName="bank_user_message_s")
	private Long mesid;//
	@Column(nullable=false)
	private Long useid;//发件人编号
	@Column(nullable=false)
	private String addressor;//发件人
	@Column(nullable=false)
	private Long parid;//合作商编号
	@Column(nullable=false)
	private Long opeid;//操作员编号
	@Column(nullable=false)
	private String addressee;//收件人(合作商+操作员)
	@Column(nullable=false)
	private String title;//标题
	@Column(nullable=false,length=10000)
	private String content;//内容
	@Column(nullable=false)
	private Long time;//时间
	public Long getMesid() {
		return mesid;
	}
	public void setMesid(Long mesid) {
		this.mesid = mesid;
	}
	public String getAddressor() {
		return addressor;
	}
	public void setAddressor(String addressor) {
		this.addressor = addressor;
	}
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getUseid() {
		return useid;
	}
	public void setUseid(Long useid) {
		this.useid = useid;
	}
	public Long getParid() {
		return parid;
	}
	public void setParid(Long parid) {
		this.parid = parid;
	}
	public Long getOpeid() {
		return opeid;
	}
	public void setOpeid(Long opeid) {
		this.opeid = opeid;
	}

	

}
