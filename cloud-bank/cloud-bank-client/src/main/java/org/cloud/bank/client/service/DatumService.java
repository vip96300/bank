package org.cloud.bank.client.service;

import java.util.List;
import java.util.Map;

import org.cloud.bank.client.exception.IllegalDatumValueException;
import org.cloud.bank.client.model.Datum;
import org.springframework.transaction.annotation.Transactional;

public interface DatumService {

	public Map<String,List> mapByBusidAndMenid(long busid,long menid);
	
	public Map<String,List> listByBusidAndMenidAndIsget(long busid,long menid,int isget);
	
	public Map<String,List> listByBusidAndMenidAndIsrequired(long busid,long menid,int isrequired);
	
	@Transactional(rollbackFor=Exception.class)
	public void updByBatch(long busid,long menid,List<Datum> datums) throws IllegalDatumValueException;
	
	@Transactional(rollbackFor=Exception.class)
	public void updByBatchBank(long busid,long menid,List<Datum> datums) throws IllegalDatumValueException;
	
	public List<Datum> listByBusidAndMenid(long busid,long menid);
	
	public List<Datum> listByBusidAndIsget(long busid);
	
	public List<Datum> listByCodeAndIsget(String code);
	
	@Transactional(rollbackFor=Exception.class)
	public void leadin(String datumsJSON) throws IllegalDatumValueException;
	
	@Transactional(rollbackFor=Exception.class)
	public String leadout(String busids,String token);
	
	@Transactional(rollbackFor=Exception.class)
	public void leadback(List<String> lines,String filename);
	
	
}
