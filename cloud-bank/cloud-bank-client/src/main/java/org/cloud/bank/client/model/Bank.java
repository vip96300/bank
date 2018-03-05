package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_bank")
public class Bank extends BaseModel{

	/**
	 * 支行
	 */
	private static final long serialVersionUID = 1352446057172429794L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_bank_g")  
	@SequenceGenerator(name="bank_bank_g", sequenceName="bank_bank_s")  
	private Long banid;
	@Column(nullable=false,unique=true)
	private String code;//银行编码
	@Column(nullable=false,unique=true)
	private String name;//银行名称
	@Column(nullable=true)
	private String bcode;//支行号也是网点号
	@Column(nullable=true)
	private String wcode;//网点号
	@Column(nullable=false)
	private Integer isdel;//删除
	public Long getBanid() {
		return banid;
	}
	public void setBanid(Long banid) {
		this.banid = banid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBcode() {
		return bcode;
	}
	public void setBcode(String bcode) {
		this.bcode = bcode;
	}
	public String getWcode() {
		return wcode;
	}
	public void setWcode(String wcode) {
		this.wcode = wcode;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	
	

}
