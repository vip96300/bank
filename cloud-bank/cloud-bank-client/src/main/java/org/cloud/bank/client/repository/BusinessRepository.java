package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusinessRepository extends JpaRepository<Business, Long> {
	
	/**
	 * @param ocode 操作员编号
	 * @return
	 */
	@Query(value="select code from bank_business where to_number(code)=(select max(to_number(code)) from bank_business where code like ?1%)",nativeQuery=true)
	public String findByCodeMax(String ocode);
	/**
	 * 根据身份证号和非状态获取业务
	 * @param idcard
	 * @param state
	 * @return
	 */
	public List<Business> findByIdcardAndStateNotAndTimeGreaterThan(String idcard,int state,long time);
	
	public long countByBanidAndIsdel(long banid,int isdel);
	
	public long countByBanidAndStateAndIsdel(long banid,int state,int isdel);
	
	public Page<Business> findByBanidAndIsdel(long banid,int isdel,Pageable pageable);
	
	public Page<Business> findByBanidAndStateAndIsdel(long banid,int state,int isdel,Pageable pageable);
	
	public Page<Business> findByOpeidAndIsdel(long opeid,int isdel,Pageable pageable);
	
	public Page<Business> findByOpeidAndStateAndIsdel(long opeid,int state,int isdel,Pageable pageable);
	
	public Page<Business> findByOpeidAndCodeLikeAndIsdel(long opeid,String code,int isdel,Pageable pageable);
	
	public Page<Business> findByOpeidAndSurnameLikeAndIsdel(long opeid,String surname,int isdel,Pageable pageable);
	
	public Page<Business> findByOpeidAndIdcardLikeAndIsdel(long opeid,String surname,int isdel,Pageable pageable);
	
	public long countByParidAndIsdel(long parid,int isdel);
	
	public Page<Business> findByParidAndIsdel(long parid,int isdel,Pageable pageable);
	
	public long countByParidAndStateAndIsdel(long parid,int state,int isdel);
	
	public Page<Business> findByParidAndStateAndIsdel(long parid,int state,int isdel,Pageable pageable);
	
	public Business findByCode(String code);
	
	public long countByIsdel(int isdel);
	
	public long countByStateAndIsdel(int state,int isdel);
	
	public Page<Business> findByIsdel(int isdel,Pageable pageable);
	
	public List<Business> findByStateAndIsdel(int state,int isdel);
	
	public Page<Business> findByStateAndIsdel(int state,int isdel,Pageable pageable);
	
	public Page<Business> findByStateAndIssyncAndIsdel(int state,int issync,int isdel,Pageable pageable);
	//----------------------------------------合作商后台搜索业务---------------------------------------------------
	@Query(value="select count(*) from bank_business o where o.parid=?1 and (o.surname like %?2% or o.idcard like %?3%) and o.isdel=0",nativeQuery=true)
	public long countByParidAndSurnameLikeOrIdcardLike(long parid,String surname, String idcard);
	
	@Query(value="select * from bank_business o where o.parid=?1 and (o.surname like %?2% or o.idcard like %?3%) and o.isdel=0 order by o.time desc",nativeQuery=true)
	public List<Business> findByParidAndSurnameLikeOrIdcardLikeOrderByTimeDesc(long parid,String surname, String idcard);
	
	@Query(value="select count(*) from bank_business o where o.parid=?1 and o.state=?2 and (o.surname like %?3% or o.idcard like %?4%) and o.isdel=0",nativeQuery=true)
	public long countByParidAndStateAndSurnameLikeOrIdcardLike(long parid, int state,String surname, String idcard);
	
	@Query(value="select * from bank_business o where o.parid=?1 and o.state=?2 and (o.surname like %?3% or o.idcard like %?4%) and o.isdel=0 order by o.time desc",nativeQuery=true)
	public List<Business> findByParidAndStateAndSurnameLikeOrIdcardLikeOrderByTimeDesc(long parid, int state,String surname, String idcard);
	//--------------------------------------银行后台超级管理员搜索业务----------------------------------------------------
	@Query(value="select count(*) from bank_business o where o.isdel=0 and o.surname like %?1% or o.idcard like %?2%",nativeQuery=true)
	public long countBySurnameLikeOrIdcardLike(String surname,String idcard);
	
	@Query(value="select * from bank_business o where o.isdel=0 and (o.surname like %?1% or o.idcard like %?2%) order by o.time desc",nativeQuery=true)
	public List<Business> findBySurnameLikeOrIdcardLikeOrderByTimeDesc(String surname,String idcard);
	
	@Query(value="select count(*) from bank_business o where o.state=?1 and isdel=0 and (o.surname like %?2% or o.idcard like %?3%)",nativeQuery=true)
	public long countByStateAndSurnameLikeOrIdcardLike(int state,String surname, String idcard);
	
	@Query(value="select * from bank_business o where o.state=?1 and isdel=0 and (o.surname like %?2% or o.idcard like %?3%) order by o.time desc",nativeQuery=true)
	public List<Business> findByStateAndSurnameLikeOrIdcardLikeOrderByTimeDesc(int state,String surname, String idcard);
	//--------------------------------------银行后台普通管理员搜索业务-----------------------
	@Query(value="select count(*) from bank_business o where o.banid=?1 and isdel=0 and (o.surname like %?2% or o.idcard like %?3%)",nativeQuery=true)
	public long countByBanidAndSurnameLikeOrIdcardLike(long banid,String surname,String idcard);
	
	@Query(value="select * from bank_business o where o.banid=?1 and isdel=0 and (o.surname like %?2% or o.idcard like %?3%) order by o.time desc",nativeQuery=true)
	public List<Business> findByBanidAndSurnameLikeOrIdcardLikeOrderByTimeDesc(long banid,String surname,String idcard);
	
	@Query(value="select count(*) from bank_business o where o.banid=?1 and o.state=?2 and isdel=0 and (o.surname like %?3% or o.idcard like %?4%)",nativeQuery=true)
	public long countByBanidAndStateAndSurnameLikeOrIdcardLike(long banid,int state,String surname, String idcard);
	
	@Query(value="select * from bank_business o where o.banid=?1 and o.state=?2 and isdel=0 and (o.surname like %?3% or o.idcard like %?4%) order by o.time desc",nativeQuery=true)
	public List<Business> findByBanidAndStateAndSurnameLikeOrIdcardLikeOrderByTimeDesc(long banid,int state,String surname, String idcard);
	
	public List<Business> findByBusidInAndIsdel(List<Long> busids,int isdel);

	public List<Business> findByBusidInAndStateAndIssyncAndAcodeNotAndIsdel(List<Long> busids,int state,int issync,String acode,int isdel);
	
	public List<Business> findByAcode(String acode);
	
}
