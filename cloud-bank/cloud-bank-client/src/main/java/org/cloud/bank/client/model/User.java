package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_user")
public class User extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6113626987558797262L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_user_g")  
	@SequenceGenerator(name="bank_user_g", sequenceName="bank_user_s")
    private Long useid;
	@Column(nullable=false,unique=true)
    private String username;
	@Column(nullable=false)
    private String password;
	@Column(nullable=false)
	private String surname;//姓名
	@Column(nullable=false)
	private Long banid;//支行编号
	@Column(nullable=false)
	private String bankname;//支行名称
	@Column(nullable=false,unique=true)
	private String code;//编码
	@Column(nullable=false)
	private Integer isenable;//是否启用
	@Column(nullable=false,name="user_level")
	private Integer level;
	public Long getUseid() {
		return useid;
	}
	public void setUseid(Long useid) {
		this.useid = useid;
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
	public Integer getIsenable() {
		return isenable;
	}
	public void setIsenable(Integer isenable) {
		this.isenable = isenable;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
}
