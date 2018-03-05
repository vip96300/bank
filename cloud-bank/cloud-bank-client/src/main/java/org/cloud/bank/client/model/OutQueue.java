package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_business_outqueue")
public class OutQueue extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 527591642546633136L;
	/**
	 * 文件导出队列
	 */
	
	public OutQueue(){
		
	}
	public OutQueue(long seq,String curdate,long menid,long time){
		this.seq=seq;
		this.curdate=curdate;
		this.menid=menid;
		this.time=time;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_outqueue_g")  
	@SequenceGenerator(name="bank_business_outqueue_g", sequenceName="bank_business_outqueue_s")  
	private Long outid;
	@Column(nullable=false)
	private Long seq;//当日队列501开始，每日不重复
	@Column(nullable=false)
	private String curdate;//当前日期
	@Column(nullable=false)
	private Long menid;//资料类型编号
	@Column(nullable=false)
	private Long time;//时间
	public Long getOutid() {
		return outid;
	}
	public void setOutid(Long outid) {
		this.outid = outid;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public String getCurdate() {
		return curdate;
	}
	public void setCurdate(String curdate) {
		this.curdate = curdate;
	}
	public Long getMenid() {
		return menid;
	}
	public void setMenid(Long menid) {
		this.menid = menid;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
}
