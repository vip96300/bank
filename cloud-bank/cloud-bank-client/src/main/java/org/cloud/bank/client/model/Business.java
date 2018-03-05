package org.cloud.bank.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="bank_business")
public class Business extends BaseModel{

	/**
	 * 业务
	 */
	private static final long serialVersionUID = 6516002755290217964L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="bank_business_g")  
	@SequenceGenerator(name="bank_business_g", sequenceName="bank_business_s")  
	private Long busid;
	@Column(nullable=false)
	private Long opeid;//操作员编号
	@Column(nullable=false)
	private Long parid;//合作商编号
	@Column(nullable=true)
	private String parname;//合作商名称
	@Column(nullable=false)
	private Long banid;//支行编号
	@Column(nullable=true)
	private String banname;//支行名称
	@Column(nullable=false)
	private String surname;//姓名
	@Column(nullable=false)
	private String idcard;//身份证
	@Column(nullable=false)
	private String telephone;//手机号
	@Column(nullable=false)
	private Integer ismarry;//是否结婚
	@Column(nullable=false,unique=true)
	private String code;//业务编码
	@Column(nullable=false,unique=true)
	private String acode;//申请编号，由银行系统分发的
	@Column(nullable=false)
	private Integer state;//状态
	@Column(nullable=false,length=1)
	private Integer issync;//文件是否同步，用户业务状态为审批已提交的业务同步文件
	@Column(nullable=false)
	private Integer pstate;//普京审核状态
	@Column(nullable=true)
	private Integer isimgerr;//是否影像驳回
	@Column()
	private String remark;//备注
	@Column(nullable=false,length=1)
	private Integer isdel;//删除
	@Column(nullable=false)
	private Long time;//
	
	public Long getBusid() {
		return busid;
	}
	public void setBusid(Long busid) {
		this.busid = busid;
	}
	public Long getOpeid() {
		return opeid;
	}
	public void setOpeid(Long opeid) {
		this.opeid = opeid;
	}
	public Long getParid() {
		return parid;
	}
	public void setParid(Long parid) {
		this.parid = parid;
	}
	public String getParname() {
		return parname;
	}
	public void setParname(String parname) {
		this.parname = parname;
	}
	public Long getBanid() {
		return banid;
	}
	public void setBanid(Long banid) {
		this.banid = banid;
	}
	public String getBanname() {
		return banname;
	}
	public void setBanname(String banname) {
		this.banname = banname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public Integer getIsmarry() {
		return ismarry;
	}
	public void setIsmarry(Integer ismarry) {
		this.ismarry = ismarry;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAcode() {
		return acode;
	}
	public void setAcode(String acode) {
		this.acode = acode;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getIssync() {
		return issync;
	}
	public void setIssync(Integer issync) {
		this.issync = issync;
	}
	public Integer getPstate() {
		return pstate;
	}
	public void setPstate(Integer pstate) {
		this.pstate = pstate;
	}
	public Integer getIsimgerr() {
		return isimgerr;
	}
	public void setIsimgerr(Integer isimgerr) {
		this.isimgerr = isimgerr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * 待提交
	 */
	@Transient
	public static final int STATE_1=1;
	/**
	 * 待审核
	 */
	@Transient
	public static final int STATE_2=2;
	/**
	 * 通过
	 */
	@Transient
	public static final int STATE_3=3;
	/**
	 * 驳回
	 */
	@Transient
	public static final int STATE_4=4;
	/**
	 * 拒绝
	 */
	@Transient
	public static final int STATE_5=5;
	/**
	 * 面签已提交，待审核
	 */
	@Transient
	public static final int STATE_STEP1_6=6;
	/**
	 * 面签审核通过
	 */
	@Transient
	public static final int STATE_STEP1_7=7;
	/**
	 * 面签驳回
	 */
	@Transient
	public static final int STATE_STEP1_8=8;
	/**
	 * 面签拒绝
	 */
	@Transient
	public static final int STATE_STEP1_9=9;
	/**
	 * 放款资料待审核
	 */
	@Transient
	public static final int STATE_STEP2_10=10;
	/**
	 * 上传开户中
	 */
	@Transient
	public static final int STATE_STEP2_11=11;
	/**
	 * 放款驳回
	 */
	@Transient
	public static final int STATE_STEP2_12=12;
	/**
	 * 放款拒绝
	 */
	@Transient
	public static final int STATE_STEP2_13=13;
	
	/**
	 * 已经放款
	 */
	@Transient
	public static final int STATE_DONE=14;
	
}
