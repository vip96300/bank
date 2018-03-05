package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankRepository extends JpaRepository<Bank, Long> {
	
	@Query(value="select code from bank_bank where to_number(code)=(select max(to_number(code)) from bank_bank)",nativeQuery=true)
	public String findByCodeMax();
	
	public Page<Bank> findByIsdel(int isdel,Pageable pageable);
	
	public List<Bank> findByBcodeAndIsdel(String bcode,int isdel);
}
