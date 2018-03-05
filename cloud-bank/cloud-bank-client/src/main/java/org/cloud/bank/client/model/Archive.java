package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_business_archive")
public class Archive extends BaseModel{

	/**
	 * 业务档案
	 */
	private static final long serialVersionUID = 6516002755290217964L;
	
	public static Archive getInstance(){
		return new Archive();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_archive_g")  
	@SequenceGenerator(name="bank_business_archive_g", sequenceName="bank_business_archive_s")  
	private Long arcid;//
	@Column(nullable=false)
	private Long busid;//业务编号
	@Column(nullable=false)
	private Long claid;//类型编号
	@Column(nullable=false)
	private Integer step;//步骤
	@Column(nullable=false)
	private String position;//位置信息
	@Column(nullable=false)
	private Integer type;//数据类型0图片，1：视频
	@Column(nullable=false)
	private String content;//数据内容
	@Column(nullable=false)
	private Long time;//时间
	public Long getArcid() {
		return arcid;
	}
	public void setArcid(Long arcid) {
		this.arcid = arcid;
	}
	public Long getBusid() {
		return busid;
	}
	public void setBusid(Long busid) {
		this.busid = busid;
	}
	public Long getClaid() {
		return claid;
	}
	public void setClaid(Long claid) {
		this.claid = claid;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	
	

}
