package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_bank_partner_operator")
public class Operator extends BaseModel{

	/**
	 * 合作商操作员
	 */
	private static final long serialVersionUID = -8116161680078224048L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_bank_partner_operator_g")  
	@SequenceGenerator(name="bank_bank_partner_operator_g", sequenceName="bank_bank_partner_operator_s")
	private Long opeid;
	@Column(nullable=false,unique=true)
	private String code;//编码（银行编码+合作商编码+操作员编码）
	@Column(nullable=false,unique=true)
	private String username;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private String surname;//姓名
	@Column(nullable=false)
	private Long banid;//银行编号
	@Column(nullable=false)
	private String bankname;//银行名称
	@Column(nullable=false)
	private Long parid;//合作商编号
	@Column(nullable=false)
	private String partnername;//合作商名称
	@Column(nullable=false)
	private Integer isenable;//是否启用
	@Column(nullable=false)
	private Integer isadmin;//是否是管理员
	@Column(nullable=false,length=1)
	private Integer islisten;//如果是管理员可设置是否接听
	@Column(nullable=false)
	private Integer isdel;//删除
	public Long getOpeid() {
		return opeid;
	}
	public void setOpeid(Long opeid) {
		this.opeid = opeid;
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
	public Long getBanid() {
		return banid;
	}
	public void setBanid(Long banid) {
		this.banid = banid;
	}
	public Long getParid() {
		return parid;
	}
	public void setParid(Long parid) {
		this.parid = parid;
	}
	public Integer getIsenable() {
		return isenable;
	}
	public void setIsenable(Integer isenable) {
		this.isenable = isenable;
	}
	public Integer getIsadmin() {
		return isadmin;
	}
	public void setIsadmin(Integer isadmin) {
		this.isadmin = isadmin;
	}
	public Integer getIslisten() {
		return islisten;
	}
	public void setIslisten(Integer islisten) {
		this.islisten = islisten;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getPartnername() {
		return partnername;
	}
	public void setPartnername(String partnername) {
		this.partnername = partnername;
	}

}
