package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Operator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperatorRepository extends JpaRepository<Operator, Long> {
	/**
	 * @param pcode 合作商code
	 * @return
	 */
	@Query(value="select code from bank_bank_partner_operator where to_number(code)=(select max(to_number(code)) from bank_bank_partner_operator where code like ?1%)",nativeQuery=true)
	public String findByCodeMax(String pcode);
	
	public Page<Operator> findByIsdel(int isdel,Pageable pageable);
	
	public long countByIsdel(int isdel);
	
	public Operator findByUsernameAndPassword(String username,String password);
	
	public Operator findByUsername(String username);
	
	public List<Operator> findByParidAndIsdel(long parid,int isdel);
	
	public List<Operator> findByBanidAndIsadmin(long banid,int isadmin);
}
