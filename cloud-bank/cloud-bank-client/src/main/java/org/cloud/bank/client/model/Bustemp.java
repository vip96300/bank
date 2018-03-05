package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_business_bustemp")
public class Bustemp extends BaseModel{

	/**
	 * 业务临时信息
	 */
	private static final long serialVersionUID = -3748028342733986240L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_temp_g")  
	@SequenceGenerator(name="bank_business_temp_g", sequenceName="bank_business_temp_s")
	private Long temid;//
	@Column(nullable=false,unique=true)
	private String code;//业务编码
	@Column(nullable=true,length=10000)
	private String urls;//放款审批额外的文件路径
	@Column(nullable=false)
	private Long time;//时间
	public Long getTemid() {
		return temid;
	}
	public void setTemid(Long temid) {
		this.temid = temid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrls() {
		return urls;
	}
	public void setUrls(String urls) {
		this.urls = urls;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
}
