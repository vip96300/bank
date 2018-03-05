package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name="bank_bank_partner")
public class Partner extends BaseModel{

	/**
	 * 合作商
	 */
	private static final long serialVersionUID = 2318465167983541177L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_bank_partner_g")  
	@SequenceGenerator(name="bank_bank_partner_g", sequenceName="bank_bank_partner_s")
	private Long parid;
	@Column(nullable=false)
	private Long banid;//银行编号
	@Column(nullable=false)
	private String bankname;//所属银行名称 
	@Column(nullable=false,unique=true)
	private String code;//编码(银行编码+合作商编码)
	@Column(nullable=false,unique=true)
	private String username;//用户名
	@Column(nullable=false)
	private String password;//密码
	@Column(nullable=false,unique=true)
	private String surname;//合作商姓名
	@Column(nullable=false)
	private String hostname;//ip地址
	@Column()
	private String noticeurl;//初审通知URL
	@Column(nullable=false,length=1)@Max(value=1)@Min(value=0)
	private Integer isproxy;//是否是代理合作商
	@Column(nullable=false,length=1)
	private Integer isenable;
	@Column(nullable=false)
	private Integer isdel;
	public Long getParid() {
		return parid;
	}
	public void setParid(Long parid) {
		this.parid = parid;
	}
	public Long getBanid() {
		return banid;
	}
	public void setBanid(Long banid) {
		this.banid = banid;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getNoticeurl() {
		return noticeurl;
	}
	public void setNoticeurl(String noticeurl) {
		this.noticeurl = noticeurl;
	}
	public Integer getIsproxy() {
		return isproxy;
	}
	public void setIsproxy(Integer isproxy) {
		this.isproxy = isproxy;
	}
	public Integer getIsenable() {
		return isenable;
	}
	public void setIsenable(Integer isenable) {
		this.isenable = isenable;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	
	

}
