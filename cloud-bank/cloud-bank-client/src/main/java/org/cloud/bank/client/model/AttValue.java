package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_business_datum_attvalue")
public class AttValue extends BaseModel{

	/**
	 * 属性值
	 */
	private static final long serialVersionUID = -8039705000893339374L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_datum_attvalue_g")  
	@SequenceGenerator(name="bank_business_datum_attvalue_g", sequenceName="bank_business_datum_attvalue_s")  
	private Long valid;//
	@Column(nullable=false)
	private Long namid;//属性名编号
	@Column(nullable=true)
	private String fkvalue;//关联值
	@Column(nullable=false)
	private String value;//属性值。实际保存的
	@Column(nullable=false)
	private String depict;//值描述，显示给用户看的，
	@Column(nullable=false)
	private Long time;
	@Column(nullable=false)
	private Integer isdel;
	public Long getValid() {
		return valid;
	}
	public void setValid(Long valid) {
		this.valid = valid;
	}
	public Long getNamid() {
		return namid;
	}
	public void setNamid(Long namid) {
		this.namid = namid;
	}
	public String getFkvalue() {
		return fkvalue;
	}
	public void setFkvalue(String fkvalue) {
		this.fkvalue = fkvalue;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDepict() {
		return depict;
	}
	public void setDepict(String depict) {
		this.depict = depict;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	
	
	
}
