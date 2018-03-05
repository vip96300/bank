package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name="bank_business_classify")
public class Classify extends BaseModel{

	/**
	 * 文件分类
	 */
	private static final long serialVersionUID = 1774689249047571618L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_classify_g")  
	@SequenceGenerator(name="bank_business_classify_g", sequenceName="bank_business_classify_s")
	private Long claid;
	@Column(nullable=false)
	private String name;//类型名称
	@Column(nullable=false)
	private Integer step;//步骤0开始
	@Column(nullable=false)
	private Integer isrequired;//是否必须
	@Column(nullable=false,length=1)@Max(value=1)@Min(value=0)
	private Integer isdel;//是否删除
	@Column(nullable=false)
	private Long time;
	public Long getClaid() {
		return claid;
	}
	public void setClaid(Long claid) {
		this.claid = claid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public Integer getIsrequired() {
		return isrequired;
	}
	public void setIsrequired(Integer isrequired) {
		this.isrequired = isrequired;
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
	/**
	 * 初审资料
	 */
	@Transient
	public static final int STEP0=0;
	/**
	 * 面签资料
	 */
	@Transient
	public static final int STEP1=1;
	/**
	 * 合作商资料
	 */
	@Transient
	public static final int STEP2=2;
	/**
	 * 资料驳回补录
	 */
	@Transient
	public static final int STEP3=3;

}
