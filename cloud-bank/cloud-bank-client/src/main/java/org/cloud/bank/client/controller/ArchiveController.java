package org.cloud.bank.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.cloud.bank.client.dto.Result;
import org.cloud.bank.client.exception.IllegalBusinessStateException;
import org.cloud.bank.client.model.Archive;
import org.cloud.bank.client.model.Business;
import org.cloud.bank.client.model.Classify;
import org.cloud.bank.client.service.ArchiveService;
import org.cloud.bank.client.service.BusinessService;
import org.cloud.bank.client.service.ClassifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping(value="/bank/business/archive")
public class ArchiveController {
	
	private static final Logger log= LoggerFactory.getLogger(ArchiveController.class);
	
	@Autowired
	private ArchiveService archiveService;
	
	@Autowired
	private ClassifyService classifyService;
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public Result<Object> add(@RequestParam(value="archive",required=true)String archiveJSON){
		Archive archive=new Gson().fromJson(archiveJSON, Archive.class);
		archiveService.add(archive);
		return new Result<Object>(200,null,null);
	}
	@RequestMapping(value="/add_batch",method=RequestMethod.POST)
	public Result<Object> add_batch(@RequestParam(value="archives",required=true)String archives){
		archiveService.add_batch(archives);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/add_batch_step1",method=RequestMethod.POST)
	public Result<Object> add_batch_step1(@RequestParam(value="archives",required=true)String archives){
		try {
			archiveService.add_batch_step1(archives);
		} catch (IllegalBusinessStateException e) {
			return new Result<Object>(500,e.getMessage(),null);
		}
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/add_batch_step3",method=RequestMethod.POST)
	public Result<Object> add_batch_step3(@RequestParam(value="archives",required=true)String archives){
		archiveService.add_batch_step3(archives);
		return new Result<Object>(200,null,null);
	}
	
	@RequestMapping(value="/list_busid_step",method=RequestMethod.POST)
	public Result<List> list_busid_step(@RequestParam(value="busid",required=true)long busid,
			@RequestParam(value="step",required=true)int step) {
		List<Archive> archives=archiveService.listByBusidStep(busid,step);
		return new Result<List>(200,null,archives);
	}
	
	@RequestMapping(value="/list_busid",method=RequestMethod.POST)
	public Result<List> list_busid(@RequestParam(value="busid",required=true)long busid) {
		List<Archive> archives=archiveService.listByBusid(busid);
		return new Result<List>(200,null,archives);
	}
	
	@RequestMapping(value="/classify_archives_busid",method=RequestMethod.POST)
	public Result<Map> classify_archives_busid(@RequestParam(value="busid",required=true)long busid){
		List<Archive> archives=archiveService.listByBusid(busid);
		List<Classify> classifys=classifyService.list();
		Map<String,List<Archive>> result=new LinkedHashMap<String,List<Archive>>();
		classifys.forEach(classify -> {
			List<Archive> archivesByClassify=new ArrayList<Archive>();
			archives.forEach(archive -> {
				if(classify.getClaid().longValue()==archive.getClaid().longValue()){
					archivesByClassify.add(archive);
				}
			});
			result.put(new Gson().toJson(classify),archivesByClassify);
		});
		return new Result<Map>(200,null,result);
	}
}
