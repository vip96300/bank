package org.cloud.bank.client.repository;

import org.cloud.bank.client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUsername(String username);
	
	@Query(value="select * from bank_user o where o.username=?1 and o.password=?2",nativeQuery=true)
	public User findByUsernameAndPassword(String username,String password);
	/**
	 * 获取最大编码的用户
	 * @param bcode 银行编码
	 * @return
	 */
	@Query(value="select code from bank_user where to_number(code)=(select max(to_number(code)) from bank_user where code like ?1%)",nativeQuery=true)
	public String findByCodeMax(String bcode);
}
