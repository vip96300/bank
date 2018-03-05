package org.cloud.bank.client.repository;

import java.util.List;

import org.cloud.bank.client.model.Datum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DatumRepository extends JpaRepository<Datum, Long> {

	public List<Datum> findByBusidAndMenidOrderBySort(long busid,long menid);
	
	public List<Datum> findByBusidAndMenidAndIsgetOrderBySort(long busid,long menid,int isget);
	
	public List<Datum> findByBusidAndMenidAndIsrequiredOrderBySort(long busid,long menid,int isrequired);
	
	public List<Datum> findByBusidAndIsgetOrderBySort(long busid,int isget);
	
	@Query(value="select * from bank_business_datum o where o.busid=(select busid from bank_business b where b.code=?1) and o.isget=?2 order by o.sort",nativeQuery=true)
	public List<Datum> findByCodeAndIsgetOrderBySort(String code,int isget);
	
	public List<Datum> findByMenidAndBusidAndIsexportOrderBySort(long menid,long busid,int isexport);
	
	public Datum findByBusidAndName(long busid,String name);
	
	public List<Datum> findByMenidAndName(long menid,String name);
	
	public List<Datum> findByBusid(long busid);
	
	public List<Datum> findByBusidAndMenidAndName(long busid,long menid,String name);
	
	@Query(value="select * from bank_business_datum o where o.busid=?1 and o.name like %?2%",nativeQuery=true)
	public List<Datum> findByBusidAndNameLike(long busid,String name);

	
}
