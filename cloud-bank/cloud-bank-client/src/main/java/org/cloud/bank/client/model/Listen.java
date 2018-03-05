package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_partner_operator_listen")
public class Listen extends BaseModel{
	
	/**
	 * 操作员接听变更记录
	 */
	private static final long serialVersionUID = -5700297096044838865L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_partner_operator_listen_g")  
	@SequenceGenerator(name="bank_partner_operator_listen_g", sequenceName="bank_partner_operator_listen_s")  
	private Long lisid;//
	@Column(nullable=false)
	private Long opeid;//操作员编号
	@Column(nullable=false,length=1)
	private Integer islisten;//是否接听
	@Column(nullable=false)
	private Long time;
	public Long getLisid() {
		return lisid;
	}
	public void setLisid(Long lisid) {
		this.lisid = lisid;
	}
	public Long getOpeid() {
		return opeid;
	}
	public void setOpeid(Long opeid) {
		this.opeid = opeid;
	}
	public Integer getIslisten() {
		return islisten;
	}
	public void setIslisten(Integer islisten) {
		this.islisten = islisten;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
}
