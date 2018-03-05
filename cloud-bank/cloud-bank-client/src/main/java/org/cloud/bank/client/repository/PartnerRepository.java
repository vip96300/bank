package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
	
	/**
	 * 银行code
	 * @param bcode
	 * @return
	 */
	@Query(value="select code from bank_bank_partner where to_number(code)=(select max(to_number(code)) from bank_bank_partner where code like ?1%)" ,nativeQuery=true)
	public String findByCodeMax(String bcode);
	
	public long countByIsdel(int isdel);
	
	public Page<Partner> findByIsdel(int isdel,Pageable pageable);
	
	public Partner findByBanidAndParid(long banid,long parid);
	
	public List<Partner> findByBanidAndIsdel(long banid,int isdel);
	
	public Partner findByUsername(String username);
	
	public Partner findByUsernameAndPassword(String username,String password);
}
