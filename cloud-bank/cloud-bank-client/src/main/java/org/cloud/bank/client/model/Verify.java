package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_business_verify")
public class Verify extends BaseModel{

	/**
	 * 审核记录
	 */
	private static final long serialVersionUID = -5923304880466794474L;
	
	public Verify(){
		
	}
	
	public Verify(long busid,String username,String progress,String remark,long time){
		this.busid=busid;
		this.username=username;
		this.progress=progress;
		this.remark=remark;
		this.time=time;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_verify_g")  
	@SequenceGenerator(name="bank_business_verify_g", sequenceName="bank_business_verify_s")  
	private Long verid;
	@Column(nullable=false)
	private Long busid;//业务编号
	@Column(nullable=false)
	private String username;//处理人姓名
	@Column(nullable=false)
	private String progress;//进度说明
	@Column(nullable=false)
	private String remark;//备注
	@Column(nullable=false)
	private Long time;//处理时间
	public Long getVerid() {
		return verid;
	}
	public void setVerid(Long verid) {
		this.verid = verid;
	}
	public Long getBusid() {
		return busid;
	}
	public void setBusid(Long busid) {
		this.busid = busid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
}
