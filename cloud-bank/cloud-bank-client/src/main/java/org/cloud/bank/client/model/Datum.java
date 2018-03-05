package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="bank_business_datum")
public class Datum extends BaseModel{
	
	/**
	 * 客户数据
	 */
	private static final long serialVersionUID = -6175243253244212151L;
	
	public static Datum getInstance(){
		return new Datum();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_datum_g")  
	@SequenceGenerator(name="bank_business_datum_g", sequenceName="bank_business_datum_s")  
	private Long datid;
	@Column(nullable=false)
	private Long busid;//业务编号
	@Column(nullable=false)
	private Long namid;
	@Column(nullable=false)
	private String name;//名称
	@Column(nullable=true)
	private String value;//值
	@Column()
	private String reg;//正则
	@Column(nullable=false)
	private Long menid;//分类编号
	@Column(nullable=false)
	private String namedepict;//属性名字段描述
	@Column(nullable=true)
	private String valuedepict;//属性值
	@Column(nullable=false)
	private Integer length;//数据长度
	@Column(nullable=false)
	private Integer isrequired;//是否必须
	@Column(nullable=false)
	private Integer intype;//输入类型
	@Column(nullable=true)
	private String defvalue;//默认值
	@Column(nullable=false,length=1)
	private String cover;//补位规则
	@Column(nullable=true)
	private String unit;//单位
	@Column(nullable=true,length=1)
	private Integer ischange;//是否能修改
	@Column(nullable=true,length=1)
	private Integer iscover;//是否需要金额元转换为分
	@Column(nullable=true)
	private String datatype;//数据类型，string,int,date
	@Column(nullable=false)
	private Long sort;//排序
	@Column(nullable=false,length=1000)
	private String remark;//备注
	@Column(nullable=false)
	private Integer isget;//是否需要面签录入（目前还不知道含义）
	@Column(nullable=false,length=1)
	private Integer isexport;//是否导出
	@Column(nullable=false)
	private Long time;
	public Long getDatid() {
		return datid;
	}
	public void setDatid(Long datid) {
		this.datid = datid;
	}
	public Long getBusid() {
		return busid;
	}
	public void setBusid(Long busid) {
		this.busid = busid;
	}
	public Long getNamid() {
		return namid;
	}
	public void setNamid(Long namid) {
		this.namid = namid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public Long getMenid() {
		return menid;
	}
	public void setMenid(Long menid) {
		this.menid = menid;
	}
	public String getNamedepict() {
		return namedepict;
	}
	public void setNamedepict(String namedepict) {
		this.namedepict = namedepict;
	}
	public String getValuedepict() {
		return valuedepict;
	}
	public void setValuedepict(String valuedepict) {
		this.valuedepict = valuedepict;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getIsrequired() {
		return isrequired;
	}
	public void setIsrequired(Integer isrequired) {
		this.isrequired = isrequired;
	}
	public Integer getIntype() {
		return intype;
	}
	public void setIntype(Integer intype) {
		this.intype = intype;
	}
	public String getDefvalue() {
		return defvalue;
	}
	public void setDefvalue(String defvalue) {
		this.defvalue = defvalue;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getIschange() {
		return ischange;
	}
	public void setIschange(Integer ischange) {
		this.ischange = ischange;
	}
	public Integer getIscover() {
		return iscover;
	}
	public void setIscover(Integer iscover) {
		this.iscover = iscover;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsget() {
		return isget;
	}
	public void setIsget(Integer isget) {
		this.isget = isget;
	}
	public Integer getIsexport() {
		return isexport;
	}
	public void setIsexport(Integer isexport) {
		this.isexport = isexport;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	
	
}
