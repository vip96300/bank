package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_business_datum_menu")
public class Menu extends BaseModel{
	
	/**
	 * 菜单分类
	 */
	private static final long serialVersionUID = -6565492130420470219L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_datum_menu_g")  
	@SequenceGenerator(name="bank_business_datum_menu_g", sequenceName="bank_business_datum_menu_s")  
	private Long menid;//
	@Column(nullable=false)
	private String name;//菜单名称
	@Column(nullable=false)
	private Long pid;//无限级分类父编号,如果为0代表顶级菜单
	@Column(nullable=false)
	private Integer issku;//是否最小单元
	@Column(nullable=false)
	private Integer isdel;
	@Column(nullable=false)
	private Long time;
	public Long getMenid() {
		return menid;
	}
	public void setMenid(Long menid) {
		this.menid = menid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Integer getIssku() {
		return issku;
	}
	public void setIssku(Integer issku) {
		this.issku = issku;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
	
}
